package bitstorage;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.CUdeviceptr;

import java.util.function.BooleanSupplier;

import static jcuda.driver.JCudaDriver.*;

public class CUDAStorage extends BitStorage{
	public final CUdeviceptr deviceStorage;

	final byte[] hostStorage;

	public CUDAStorage(int width, int height){
		super(width, height);

		hostStorage = new byte[width * height];

		deviceStorage = new CUdeviceptr();
		cuMemAlloc(deviceStorage, width * height * Sizeof.BYTE);
	}

	@Override
	public CUDAStorage makeClone(){
		return new CUDAStorage(width, height);
	}

	@Override
	public boolean get(int row, int column){
		return hostStorage[row * width + column] == 1;
	}

	@Override
	public void set(int row, int column, boolean living){
		hostStorage[row * width + column] = (byte)(living ? 0x1 : 0x0);
	}

	public void send(){
		cuMemcpyHtoD(deviceStorage, Pointer.to(hostStorage), width * height * Sizeof.BYTE);
	}

	public void receive(){
		cuMemcpyDtoH(Pointer.to(hostStorage), deviceStorage, width * height * Sizeof.BYTE);
	}

	@Override
	public String toString(){
		return "CUDAStorage{" +
			"devicePixels=" + deviceStorage +
			", storage=" + Pointer.to(hostStorage).getByteBuffer() +
			'}';
	}

	@Override
	public void populate(BooleanSupplier supplier){
		super.populate(supplier);
		send();
	}

	@Override
	public void close(){
		cuMemFree(deviceStorage);
	}
}
