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
	
	public Color(Vector c)
	{
		this(c.x, c.y, c.z);
	}
	
	public Color add(Color otherColor)
	{
		return new Color(rgb.add(otherColor.rgb));
	}
	
	public Color multiply(Color otherColor)
	{
		return new Color(rgb.timesVector(otherColor.rgb));
	}
	
	public Color scalarMultipy(double factor)
	{
		return new Color(rgb.timesScalar(factor));
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
	
	@Override
	public String toString() {
		return String.format("rgb(%.2f,%.2f,%.2f)", rgb.x, rgb.y, rgb.z);
	}

}
