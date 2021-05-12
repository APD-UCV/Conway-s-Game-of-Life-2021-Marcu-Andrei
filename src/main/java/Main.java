import algorithm.*;
import bitstorage.*;
import bitstorage.BitStorage.StorageConstructor;
import renderer.FrameRenderer;
import renderer.Renderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;


@SuppressWarnings({"RedundantSuppression","RedundantThrows"})
public class Main{
	static BitStorage storage;

	static StorageConstructor CDSconstr = CUDAStorage::new; //((width, height) -> new SplitBitStorage(width,height, BooleanStorage::new));

	@SuppressWarnings("rawtypes")
	static Algorithm algorithm;

	//static Renderer renderer;

	public static void main(String[] args) throws InterruptedException, FileNotFoundException{
		if(args.length != 1){
			System.err.println("Path of the folder of inputs must be specified!");
			System.exit(0);
		}

		CUDAAlgorithm.initCUDA();
		//renderer = new FrameRenderer();

		File folder = new File(args[0]);
		for(File file : Objects.requireNonNull(folder.listFiles())){
			try{
				float ratio = populate(file, CDSconstr);

				//algorithm = new SimpleAlgorithm(storage);
				//algorithm = new ParallelAlgorithm((SplitBitStorage)storage);
				algorithm = new CUDAAlgorithm((CUDAStorage)storage);

				run(ratio);
			} finally{
				algorithm.close();
			}
		}/*
		try{
			float ratio = populate(0, CDSconstr);
			//algorithm = new SimpleAlgorithm(storage);
			//algorithm = new ParallelAlgorithm((SplitBitStorage)storage);
			algorithm = new CUDAAlgorithm((CUDAStorage)storage);
			run(ratio);
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			algorithm.close();
		}*/

		System.exit(0);
	}

	static float populate(File file, StorageConstructor storageConstructor) throws FileNotFoundException{
		Scanner scanner = new Scanner(file);
		String fileName = file.getName();
		int begin = fileName.indexOf('x') + 1;
		int end = fileName.indexOf(".txt");

		int size = Integer.parseInt(fileName.substring(begin, end));

		//System.out.println(size);

		storage = storageConstructor.construct(size,size);

		Iterator<Boolean> chars = scanner.nextLine().chars()
			.mapToObj(x -> x == '1').iterator();
		storage.populate(chars::next);

		begin = fileName.indexOf('[') + 1;
		end = fileName.indexOf(",");

		return Float.parseFloat(fileName.substring(begin, end));
	}

	static float populate(long seed, StorageConstructor storageConstructor){
		storage = storageConstructor.construct(Config.MATRIX_WIDTH, Config.MATRIX_HEIGHT);
		storage.populate(new Random(seed)::nextBoolean);
		return 0.5f;
	}

	static void run(float ratio) throws InterruptedException{
		int iterations = 0;
		long computeTime = 0;
		//noinspection InfiniteLoopStatement
		for(int i = 0; i < Config.RUNS[Config.RUNS.length-1]; i++)
		{
			//renderer.render(algorithm.getStorage());

			//Thread.sleep(100);

			long start = System.nanoTime();
			algorithm.compute();
			long end = System.nanoTime();

			computeTime += (end - start);

			algorithm.swap();

			if(i == Config.RUNS[iterations] - 1){
				printTime(Config.RUNS[iterations], computeTime, ratio);
				iterations++;
			}
		}
	}

	static void printTime(int iterations, long computeTime, float ratio){
		System.out.print(Float.toString(ratio) + ',' + storage.width + ',' + storage.height + ',' + iterations + ',' + (computeTime / iterations) + '\n');
	}
}
