package RayTracing;

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
        //System.out.println(Thread.currentThread().getName() + " Start. Pixel = (" + i +"," + j + ")");
        renderOnePixel();
        //System.out.println(Thread.currentThread().getName() + " End.");
    }
    
    /**
     * Main Ray-Tracing function 
     */
    private void renderOnePixel()
    {
    	//-- decide about view plane grid
    	//-- and make a ray
    	Ray ray = cam.constructRayThroughPixel(i,j);
    	
    	//-- ray trace
    	Color c = rayTrace(ray);
    	rgbData[(i*cam.getCols() + j)*3 + 0] = (byte)(c.getR()*255);
    	rgbData[(i*cam.getCols() + j)*3 + 1] = (byte)(c.getG()*255);
    	rgbData[(i*cam.getCols() + j)*3 + 2] = (byte)(c.getB()*255);
    }

    private Color rayTrace(Ray ray) 
    {
		Color col = set.getColorAtIntersectionOfRay(ray, 0);
		return col;
	}

	@Override
    public String toString(){
        return String.format("(%d,%d)", i, j);
    }
}