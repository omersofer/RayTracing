package RayTracing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

	ExecutorService executor;
	int[][] pixels;
	
	public ThreadPool(int numOfWorkers, int[][] pixels)
	{
		executor = Executors.newFixedThreadPool(numOfWorkers);
		this.pixels = pixels;
	}
	
    public void render(int numRows, int numCols) 
    {
    	pixels = new int[numRows][numCols];
    	
        for (int i = 0; i < numRows	; i++) 
        {
        	for (int j = 0; j < numCols; j++)
        	{
        		Runnable worker = new WorkerThread(i, j, pixels);
        		executor.execute(worker);        		
        	}
        }
        executor.shutdown();
        while (!executor.isTerminated()) 
        {
        }
        System.out.println("Finished all threads. Printing resulting image:\n");
        for (int i = 0; i < numRows; i++)
        {
        	String row = "";
        	for (int j = 0; j < numCols; j++)
        	{
        		row = row + pixels[i][j];
        	}
        	System.out.println(row);
        }
        System.out.println("Bye");
    }
}