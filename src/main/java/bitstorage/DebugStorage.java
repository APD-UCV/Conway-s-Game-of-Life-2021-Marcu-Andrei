package bitstorage;

import java.util.function.BooleanSupplier;

public class DebugStorage extends BitStorage{
	final boolean value;

	public DebugStorage(int width, int height, boolean value){
		super(width, height);
		this.value = value;
	}

	@Override
	public DebugStorage makeClone(){
		return new DebugStorage(width, height, value);
	}

	@Override
	public boolean get(int row, int column){
		return value;
	}

	@Override
	public void set(int row, int column, boolean living){}

	@Override
	public void populate(BooleanSupplier supplier){}
}
