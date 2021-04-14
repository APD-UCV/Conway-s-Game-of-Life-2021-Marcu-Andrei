package algorithm;

import bitstorage.BitStorage;

public class SimpleAlgorithm extends Algorithm<BitStorage>{

	public SimpleAlgorithm(BitStorage storage){
		super(storage);
	}

	@Override
	public void compute(){
		for(int row = 0; row < current.height; row++){
			for(int column = 0; column < current.width; column++){
				//count cells
				int cells = 0;
				for(int nRow = row - 1; nRow <= row + 1; nRow++){
					for(int nColumn = column - 1; nColumn <= column + 1; nColumn++){
						if(current.getSafe(nRow, nColumn)){
							cells++;
						}
					}
				}
				switch(cells){
					case 3:
						next.set(row, column,true);
						break;
					case 4:
						next.set(row, column, current.get(row,column));
						break;
					default:
						next.set(row, column,false);
				}
			}
		}
	}
}
