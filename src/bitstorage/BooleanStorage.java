package bitstorage;

import algorithm.Config;

import java.util.Random;

public class BooleanStorage implements BitStorage{
	protected final boolean[][] storage = new boolean[Config.MATRIX_HEIGHT][Config.MATRIX_WIDTH];

	@Override
	public boolean get(int row, int column){
		return storage[row][column];
	}

	@Override
	public void set(int row, int column, boolean living){
		storage[row][column] = living;
	}
}
