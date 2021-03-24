package bitstorage;

import algorithm.Config;

import java.util.Random;

public interface BitStorage{

	boolean get(int row, int column);

	default boolean getSafe(int row, int column){
		if(row < 0 || column < 0 || row >= Config.MATRIX_HEIGHT || column >= Config.MATRIX_WIDTH)
			return false;
		return get(row, column);
	}

	void set(int row, int column, boolean living);

	default void randomize(long seed){
		Random random = new Random(seed);
		for(int i = 0; i < Config.MATRIX_HEIGHT; i++){
			for(int j = 0; j < Config.MATRIX_WIDTH; j++){
				set(i,j,random.nextBoolean());
			}
		}
	}
}
