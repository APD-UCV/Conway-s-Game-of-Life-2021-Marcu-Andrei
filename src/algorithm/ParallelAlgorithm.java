package algorithm;

import bitstorage.BitStorage;
import bitstorage.SplitBitStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static algorithm.Config.THREADS;

public class ParallelAlgorithm extends Algorithm<SplitBitStorage>{
	Collection<PAThread> threads = new ArrayList<>();
	ExecutorService executorService;

	public ParallelAlgorithm(SplitBitStorage storage){
		super(storage);
		for(int i = 0; i < THREADS; i++){
			threads.add(new PAThread(i));
		}
		executorService = Executors.newFixedThreadPool(THREADS);
	}

	@Override
	public void compute(){
		try{
			executorService.invokeAll(threads);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	@Override
	public void swap(){
		super.swap();
		for(PAThread thread : threads){
			thread.setup();
		}
	}

	class PAThread implements Callable<Void>{
		volatile BitStorage storage;
		volatile BitStorage nextStorage;
		volatile BitStorage leftNeighbor;
		volatile BitStorage rightNeighbor;

		final int index;

		public PAThread(int index){
			this.index = index;
			setup();
		}

		void setup(){
			storage = current.storages[index];
			nextStorage = next.storages[index];
			if(index != 0){
				leftNeighbor = current.storages[index - 1];
			}
			if(index != THREADS - 1){
				rightNeighbor = current.storages[index + 1];
			}
		}

		@Override
		public Void call(){
			for(int row = 0; row < storage.height; row++){
				for(int column = 0; column < storage.width; column++){
					//count cells
					int cells = 0;
					for(int nRow = row - 1; nRow <= row + 1; nRow++){
						for(int nColumn = column - 1; nColumn <= column + 1; nColumn++){
							boolean cell = false;
							if(nRow < 0 || nRow >= storage.height){
								continue;
							}

							if(nColumn < 0){
								cell = leftNeighbor != null && leftNeighbor.get(nRow,leftNeighbor.width - 1);
							}
							else if(nColumn >= storage.width){
								cell = rightNeighbor != null && rightNeighbor.get(nRow, 0);
							}
							else {
								cell = storage.get(nRow,nColumn);
							}

							if(cell){
								cells++;
							}
						}
					}
					switch(cells){
						case 3:
							nextStorage.set(row,column,true);
							break;
						case 4:
							nextStorage.set(row,column,storage.get(row,column));
							break;
						default:
							nextStorage.set(row,column,false);
					}
				}
			}
			return null;
		}
	}

}
