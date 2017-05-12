package RayTracing;

public class Vector {
	
	double x;
	double y;
	double z;
	
	/**
	 * ctor
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * copy ctor
	 * @param v - other vector
	 */
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
	
	/**
	 * Doesn't change original Vector
	 * @return new Vector with magnitude 1.0
	 */
	public Vector toUnit()
	{
		Vector u = new Vector(this);
		u.normalize();
		return u;
	}
	
	public static Vector add(Vector u, Vector v)
	{
		return new Vector(u.x + v.x,
				u.y + v.y,
				u.z + v.z);
	}
	
	public Vector add(Vector v)
	{
		return add(this, v);
	}
	
	public static Vector substract(Vector u, Vector v)
	{
		return new Vector(u.x - v.x,
				u.y - v.y,
				u.z - v.z);
	}
	
	public Vector substract(Vector v)
	{
		return substract(this, v);
	}
	
	/**
	 * @param d - lengthening factor.
	 * @return new Vector longer by factor d than original Vector.
	 */
	public Vector timesScalar(double d)
	{
		return new Vector(this.x * d,
				this.y * d, this.z * d);
	}
	
	public Vector timesVector(Vector v)
	{
		return new Vector(this.x * v.x,
				this.y * v.y,
				this.z * v.z);
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getZ()
	{
		return z;
	}
	
	 @Override
    public String toString(){
        return String.format("v(%.2f,%.2f,%.2f)", x, y, z);
    }
}
