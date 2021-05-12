package bitstorage;

import java.util.function.BooleanSupplier;

//NOTE: make sure the storage is volatile
public abstract class BitStorage{
	public final int width, height;

	public BitStorage(int width, int height){
		this.width = width;
		this.height = height;
	}

	public abstract BitStorage makeClone();

	public abstract boolean get(int row, int column);

	public boolean getSafe(int row, int column){
		if(row < 0 || column < 0 || row >= height || column >= width)
			return false;
		return get(row, column);
	}

	public abstract void set(int row, int column, boolean living);

	public void populate(BooleanSupplier supplier){
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				set(i,j,supplier.getAsBoolean());
			}
		}
	}

	public void close(){}

	public interface StorageConstructor{
		BitStorage construct(int width, int height);
	}
}
