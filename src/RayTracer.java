import java.util.concurrent.TimeUnit;

public class RayTracer {

	public static void main(String[] args)
	{
		System.out.println("Hello Ray Tracers!");
		long startTime = System.nanoTime();
		
		int numRows = 20;
		int numCols = 20;
		int numOfWorkers = 10;
		int[][] pixels = new int[numRows][numCols];
		ThreadPool rayTracingJobs = new ThreadPool(numOfWorkers, pixels);
		rayTracingJobs.render(numRows, numCols);
		
		System.out.printf("Made %d calculations, ~0.5 sec each. Elapsed Time = %,d msecs",
				numRows * numCols, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)); 
	}
}
