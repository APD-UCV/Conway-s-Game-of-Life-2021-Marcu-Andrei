package algorithm;

import bitstorage.CUDAStorage;
import jcuda.Pointer;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import jcuda.driver.JCudaDriver;

import static algorithm.Config.*;
import static jcuda.driver.JCudaDriver.*;
import static jcuda.driver.JCudaDriver.cuInit;

public class CUDAAlgorithm extends Algorithm<CUDAStorage>{
	static CUfunction function;

	public final int gridSizeX, gridSizeY;

	protected Pointer kernelParameters;

	public static void initCUDA() {
		JCudaDriver.setExceptionsEnabled(true);

		// Initialize the driver and create a context for the first device.
		cuInit(0);
		CUdevice device = new CUdevice();
		cuDeviceGet(device, 0);
		CUcontext context = new CUcontext();
		cuCtxCreate(context, 0, device);

		// Load the ptx file. Obtain it by running nvcc -ptx GOLKernel.cu in the kernels folder
		CUmodule module = new CUmodule();
		cuModuleLoad(module, ptxFileName);

		// Obtain a function pointer to the "add" function.
		function = new CUfunction();
		cuModuleGetFunction(function, module, "compute");
	}

	public CUDAAlgorithm(CUDAStorage storage){
		super(storage);

		kernelParameters = Pointer.to(
			Pointer.to(new int[]{storage.width}),
			Pointer.to(new int[]{storage.height}),
			Pointer.to(current.deviceStorage),
			Pointer.to(next.deviceStorage)
		);
		gridSizeX = (int) Math.ceil((double) current.width / blockSizeX);
		gridSizeY = (int) Math.ceil((double) current.height / blockSizeY);
	}

	@Override
	public void compute(){
		cuLaunchKernel(function,
			gridSizeX, gridSizeY, 1,		// Grid dimension
			blockSizeX, blockSizeY, 1,	// Block dimension
			0, null,		// Shared memory size and stream
			kernelParameters, null			// Kernel- and extra parameters
		);
		cuCtxSynchronize();
	}

	@Override
	public void swap(){
		super.swap();
		kernelParameters = Pointer.to(
			Pointer.to(new int[]{current.width}),
			Pointer.to(new int[]{current.height}),
			Pointer.to(current.deviceStorage),
			Pointer.to(next.deviceStorage)
		);
	}

	@Override
	public CUDAStorage getStorage(){
		current.receive();
		return current;
	}
}
