package bitstorage;

import algorithm.Config;

import java.util.Random;

public class BooleanStorage extends BitStorage{
	protected final boolean[][] storage;

	public BooleanStorage(int width, int height){
		super(width, height);
		storage = new boolean[height][width];
	}

	@Override
	public BooleanStorage makeClone(){
		return new BooleanStorage(width, height);
	}

	@Override
	public boolean get(int row, int column){
		return storage[row][column];
	}

	@Override
	public void set(int row, int column, boolean living){
		storage[row][column] = living;
	}
}
