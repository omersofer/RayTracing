import java.util.concurrent.TimeUnit;

public class RayTracer {

	public static void main(String[] args)
	{
		System.out.println("Hello Ray Tracers!");
		long startTime = System.nanoTime();
		
		int numRows = 5;
		int numCols = 6;
		int numOfWorkers = 10;
		int[][] pixels = new int[numRows][numCols];
		ThreadPool rayTracingJobs = new ThreadPool(numOfWorkers, pixels);
		rayTracingJobs.render(numRows, numCols);
		
		System.out.printf("Made %d calculations, ~5 sec each. Elapsed Time = %d secs",
				numRows * numCols, TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime)); 
	}
}
