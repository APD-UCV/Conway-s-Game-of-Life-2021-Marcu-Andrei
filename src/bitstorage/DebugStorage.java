package bitstorage;

public class DebugStorage implements BitStorage{
	final boolean value;

	public DebugStorage(boolean value){
		this.value = value;
	}

	@Override
	public boolean get(int row, int column){
		return value;
	}

	@Override
	public void set(int row, int column, boolean living){}

	@Override
	public void randomize(long seed){}
}
