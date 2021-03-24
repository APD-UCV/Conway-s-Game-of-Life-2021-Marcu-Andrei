import algorithm.Algorithm;
import algorithm.Config;
import algorithm.SimpleAlgorithm;
import bitstorage.BitStorage;
import bitstorage.BooleanStorage;
import renderer.FrameRenderer;
import renderer.Renderer;

public class Main{
	static BitStorage current = new BooleanStorage();
	static BitStorage next = new BooleanStorage();

	static final Algorithm algorithm = new SimpleAlgorithm();

	static long computeTime = 0, renderTime = 0;

	@SuppressWarnings({"RedundantThrows", "RedundantSuppression"})
	public static void main(String[] args) throws InterruptedException{
		Renderer renderer = new FrameRenderer();

		current.randomize(0/*System.nanoTime()*/);

		//noinspection InfiniteLoopStatement
		for(int i = 0; i < Config.RUNS; i++)
		{
			renderer.render(current);

			//Thread.sleep(100);

			long start = System.nanoTime();
			algorithm.compute(current, next);
			long end = System.nanoTime();

			computeTime += (end - start);

			BitStorage swap = current;
			current = next;
			next = swap;
		}
		System.out.println("Average compute time: " + computeTime / Config.RUNS + " ns");
		System.out.println("Average render time: " + renderer.getAvgTime() + " ns");
	}
}
