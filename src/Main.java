import algorithm.Algorithm;
import algorithm.Config;
import algorithm.ParallelAlgorithm;
import algorithm.SimpleAlgorithm;
import bitstorage.BitStorage;
import bitstorage.BitStorage.StorageConstructor;
import bitstorage.BooleanStorage;
import bitstorage.SplitBitStorage;
import renderer.FrameRenderer;
import renderer.Renderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;


@SuppressWarnings("RedundantSuppression")
public class Main{
	static BitStorage storage;

	static StorageConstructor SCSconstr =  ((width, height) -> new SplitBitStorage(width,height, BooleanStorage::new));

	@SuppressWarnings("rawtypes")
	static Algorithm algorithm;

	//static Renderer renderer = new FrameRenderer();

	@SuppressWarnings("RedundantThrows")
	public static void main(String[] args) throws InterruptedException, FileNotFoundException{
		if(args.length != 1){
			System.out.println("Path of the folder of inputs must be specified!");
			return;
		}

		File folder = new File(args[0]);
		for(File file : folder.listFiles()){
			float ratio = populate(file, SCSconstr);

			//algorithm = new SimpleAlgorithm(storage);
			algorithm = new ParallelAlgorithm((SplitBitStorage)storage);

			run(ratio);
		}
		/*
		float ratio = populate(0, SCSconstr);
		algorithm = new SimpleAlgorithm(storage);
		//algorithm = new ParallelAlgorithm((SplitBitStorage)storage);
		run(ratio);
		 */
		System.exit(0);
	}

	static float populate(File file, StorageConstructor storageConstructor) throws FileNotFoundException{
		Scanner scanner = new Scanner(file);
		String fileName = file.getName();
		int begin = fileName.indexOf('x') + 1;
		int end = fileName.indexOf(".txt");

		int size = Integer.parseInt(fileName.substring(begin, end));

		//System.out.println(size);

		storage = storageConstructor.construct(size,size);

		Iterator<Boolean> chars = scanner.nextLine().chars()
			.mapToObj(x -> x == '1').iterator();
		storage.populate(chars::next);

		begin = fileName.indexOf('[') + 1;
		end = fileName.indexOf(",");

		return Float.parseFloat(fileName.substring(begin, end));
	}

	static float populate(long seed, StorageConstructor storageConstructor){
		storage = storageConstructor.construct(Config.MATRIX_WIDTH, Config.MATRIX_HEIGHT);
		storage.populate(new Random(seed)::nextBoolean);
		return 0.5f;
	}

	static void run(float ratio) throws InterruptedException{
		int iterations = 0;
		long computeTime = 0;
		//noinspection InfiniteLoopStatement
		for(int i = 0; i < Config.RUNS[Config.RUNS.length-1]; i++)
		{
			//renderer.render(algorithm.getStorage());

			//Thread.sleep(100);

			long start = System.nanoTime();
			algorithm.compute();
			long end = System.nanoTime();

			computeTime += (end - start);

			algorithm.swap();

			if(i == Config.RUNS[iterations] - 1){
				printTime(Config.RUNS[iterations], computeTime, ratio);
				iterations++;
			}
		}
	}

	static void printTime(int iterations, long computeTime, float ratio){
		System.out.print(Float.toString(ratio) + ',' + storage.width + ',' + storage.height + ',' + iterations + ',' + (computeTime / iterations) + '\n');
	}
}
