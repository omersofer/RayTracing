package RayTracing;

public class Color {
	/**
	 * This class only supports element-wise operation.
	 */
	
	private Vector rgb;
	
	public Color(double r, double g, double b)
	{
		rgb = new Vector(r,g,b);
	}
	
	public Color(Color otherColor)
	{
		this(otherColor.getR(), otherColor.getG(), otherColor.getB());
	}
	
	public void add(Color otherColor)
	{
		rgb = rgb.add(otherColor.rgb);
	}
	
	public void multiply(Color otherColor)
	{
		rgb = rgb.timesVector(otherColor.rgb);
	}
	
	public void trim()
	{
		if (rgb.x > 1) rgb.x = 1;
		if (rgb.y > 1) rgb.y = 1;
		if (rgb.z > 1) rgb.z = 1;
	}
	
	public double getR()
	{
		return rgb.x;
	}
	
	public double getG()
	{
		return rgb.y;
	}
	
	public double getB()
	{
		return rgb.z;
	}

}
