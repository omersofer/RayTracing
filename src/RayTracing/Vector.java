package RayTracing;

public class Vector {
	
	double x;
	double y;
	double z;
	
	public Vector(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector(Vector v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public static double dotProduct(Vector u, Vector v)
	{
		return u.x * v.x + u.y * v.y + u.z * v.z;
	}
	
	public double dotProduct(Vector v)
	{
		return dotProduct(this, v);
	}
	
	public static Vector crossProduct(Vector u, Vector v)
	{
		return new Vector(u.y * v.z - u.z * v.y,
				u.z * v.x - u.x * v.z,
				u.x * v.y - u.y * v.x);
	}
	
	public Vector crossProduct(Vector v)
	{
		return crossProduct(this, v);
	}
	
	
	public double magnitude()
	{
		return Math.sqrt(this.x * this.x +
				this.y * this.y + 
				this.z * this.z);
	}
	
	public void normalize()
	{
		double n = magnitude();
		x = x / n;
		y = y / n;
		z = z / n;
	}
	
	public static Vector toUnit(Vector v)
	{
		Vector u = new Vector(v);
		u.normalize();
		return u;
	}
	
	 @Override
    public String toString(){
        return String.format("v(%f,%f,%f)", x, y, z);
    }
}
