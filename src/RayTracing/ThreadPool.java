package RayTracing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

	ExecutorService executor;
	byte[] rgbData;
	Set set;
	Camera cam;
	
	public ThreadPool(int numOfWorkers, byte[] rgbData, Set set, Camera cam)
	{
		executor = Executors.newFixedThreadPool(numOfWorkers);
		this.rgbData = rgbData;
		this.set = set;
		this.cam = cam;
	}
	
    public void render(int numRows, int numCols) 
    {
    	cam.printViewingPlane();
        for (int i = 0; i < numRows	; i++) 
        {
        	for (int j = 0; j < numCols; j++)
        	{
        		Runnable worker = new WorkerThread(i, j, rgbData, set, cam);
        		//worker.run();
        		executor.execute(worker);        		
        	}
        }
        executor.shutdown();
        while (!executor.isTerminated()) 
        {
        }
        System.out.println("Finished all threads. Printing resulting image:\n");
    }
}