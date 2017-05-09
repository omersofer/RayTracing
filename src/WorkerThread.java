public class WorkerThread implements Runnable {
  
    private int i;
    private int j;
    private int[][] pixels;
    
    public WorkerThread(int i, int j, int[][] pixels)
    {
    	this.i = i;
    	this.j = j;
    	this.pixels = pixels;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start. Pixel = (" + i +"," + j + ")");
        processCommand();
        System.out.println(Thread.currentThread().getName() + " End.");
    }

    private void processCommand() {
        try {
            Thread.sleep(5000);
            pixels[i][j] = i + j;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return String.format("(%d,%d)", i, j);
    }
}