package RayTracing;

public class Ray {
	
	private Vector origin;
	private Vector vector;

	public Ray(Vector origin, Vector vector)
	{
		this.origin = new Vector(origin);
		this.vector = vector.toUnit();
	}
	
	public Vector getOrigin()
	{
		return new Vector(origin);
	}
	
	public Vector getVector()
	{
		return new Vector(vector);
	}
	
	public Vector getUnitTimes(double d)
	{
		return getVector().toUnit().timesScalar(d);
	}
	
	@Override
	public String toString() {
		return "r(" + origin + " + t*" + vector + ")";
	}
}

