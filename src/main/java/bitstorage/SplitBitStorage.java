package bitstorage;

import algorithm.Config;

import static algorithm.Config.THREADS;

public class SplitBitStorage extends BitStorage{
	public final BitStorage[] storages;
	public final int[] offsets;
	public final int[] references;
	StorageConstructor constructor;

	public SplitBitStorage(int width, int height, StorageConstructor constructor){
		super(width, height);
		this.constructor = constructor;
		storages = new BitStorage[THREADS];
		offsets = new int[THREADS];
		references = new int[width];
		int lastOffset = 0;

		for(int i = 0; i < THREADS; i++){
			int offset = width / THREADS;
			if(i < width % THREADS)
				offset++;

			for(int j = 0; j < offset; j++){
				references[lastOffset + j] = i;
			}

			offsets[i] = lastOffset;

			storages[i] = constructor.construct(offset, height);

			lastOffset += offset;
		}
	}

	@Override
	public SplitBitStorage makeClone(){
		return new SplitBitStorage(width, height, constructor);
	}

	@Override
	public boolean get(int row, int column){
		int pos = references[column];
		return storages[pos].get(row, column - offsets[pos]);
	}

	@Override
	public void set(int row, int column, boolean living){
		int pos = references[column];
		storages[pos].set(row, column - offsets[pos], living);
	}
}
