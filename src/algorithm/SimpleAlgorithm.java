package algorithm;

import bitstorage.BitStorage;

public class SimpleAlgorithm implements Algorithm{
	@Override
	public void compute(BitStorage current, BitStorage next){
		for(int i = 0; i < Config.MATRIX_HEIGHT; i++){
			for(int j = 0; j < Config.MATRIX_WIDTH; j++){
				//count cells
				int cells = 0;
				for(int y = i - 1; y <= i + 1; y++){
					for(int x = j - 1; x <= j + 1; x++){
						if(current.getSafe(y, x)){
							cells++;
						}
					}
				}
				switch(cells){
					case 3:
						next.set(i,j,true);
						break;
					case 4:
						next.set(i,j,current.get(i,j));
						break;
					default:
						next.set(i,j,false);
				}
			}
		}
	}
}
