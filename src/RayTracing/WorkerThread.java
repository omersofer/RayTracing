package RayTracing;

import java.util.ArrayList;

public class WorkerThread implements Runnable {
  
    private int i;
    private int j;
    private byte[] rgbData;
    private Set set;
    private Camera cam;
    
    public WorkerThread(int i, int j, byte[] rgbData, Set set, Camera cam)
    {
    	this.i = i;
    	this.j = j;
    	this.rgbData = rgbData;
    	this.set = set;
    	this.cam = cam;
    }

    @Override
    public void run() {
        renderOnePixel();
    }
    
    /**
     * Main Ray-Tracing function 
     */
    private void renderOnePixel()
    {
    	//-- decide about view plane grid
    	//-- and make a ray
    	ArrayList<Ray> rays = cam.constructRaysThroughPixel(i, j, set.getSuper_sampling_lvl());
    	
    	//-- ray trace
    	ArrayList<Color> colors = rayTrace(rays);
    	
    	Color c = Color.averageColor(colors);
    	
    	rgbData[((cam.getRows()-1 - i)*cam.getCols() + (cam.getCols()-1 - j))*3 + 0] = (byte)(c.getR()*255);
    	rgbData[((cam.getRows()-1 - i)*cam.getCols() + (cam.getCols()-1 - j))*3 + 1] = (byte)(c.getG()*255);
    	rgbData[((cam.getRows()-1 - i)*cam.getCols() + (cam.getCols()-1 - j))*3 + 2] = (byte)(c.getB()*255);
    }

    private ArrayList<Color> rayTrace(ArrayList<Ray> rays) 
    {
    	ArrayList<Color> colors = new ArrayList<>();
    	for (Ray ray : rays)
    	{
    		Color col = set.getColorAtIntersectionOfRay(ray, 0);
    		colors.add(col);
    	}
		return colors;
	}

	@Override
    public String toString(){
        return String.format("(%d,%d)", i, j);
    }
}
