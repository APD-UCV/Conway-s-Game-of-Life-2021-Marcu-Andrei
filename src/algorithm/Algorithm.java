package algorithm;

import bitstorage.BitStorage;

public abstract class Algorithm<T extends BitStorage>{
	T current,next;

	public Algorithm(T storage){
		current = storage;
		//noinspection unchecked
		next = (T)storage.makeClone();
	}

	public abstract void compute();

	public void swap(){
		T swap = current;
		current = next;
		next = swap;
	}

	public T getStorage(){
		return current;
	}
}
