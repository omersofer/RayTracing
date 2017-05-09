package RayTracing;

import RayTracing.RayTracer.RayTracerException;

public class Sphere extends Primitive {

	private Vector origin;
	private double radius;
	
	public Sphere(Material material, Vector origin, double radius)
	{
		super(material);
		this.origin = origin;
		this.radius = radius;
	}

	public Vector[] instersections(Ray r) throws RayTracerException 
	{
		
		if (origin.substract(r.getOrigin()).magnitude() < radius)
		{
			// means ray's origin inside sphere!
			throw new RayTracerException("Unsupported Intersection calculation: Ray starts inside sphere");
		}
		
		Vector L = origin.substract(r.getOrigin());
		double t_ca = L.dotProduct(r.getVector());
		if (t_ca < 0)
			return null;
		
		double d_squared = Vector.dotProduct(L, L) - t_ca * t_ca;
		if (d_squared > radius * radius)
			return null;
		
		double t_hc = Math.sqrt(radius * radius - d_squared);
		double t1 = t_ca - t_hc;
		double t2 = t_ca + t_hc;

		Vector[] intersection_points = new Vector[2];
		intersection_points[0] = r.getOrigin().add(r.getUnitTimes(t1));
		intersection_points[1] = r.getOrigin().add(r.getUnitTimes(t2));
		
		return intersection_points;
	}

	@Override
	public Vector closerIntersectionPoint(Ray r) throws RayTracerException
	{
		Vector[] intersections = this.instersections(r);
		if (intersections[0] == null && intersections[1] == null)
		{
			return null;
		}
		else if (intersections[0] == null)
			return intersections[1];
		else if (intersections[1] == null)
			return intersections[0];
		
		if (intersections[0].substract(r.getOrigin()).magnitude() <
				intersections[1].substract(r.getOrigin()).magnitude())
		{
			return intersections[0];
		}
		return intersections[1];
	}

	@Override
	public Vector normalAtIntersection(Ray r) throws RayTracerException 
	{
		Vector P = closerIntersectionPoint(r);
		Vector N = P.substract(r.getOrigin());
		N.normalize();
		return N;
	}
	
	@Override
	public String toString() {
		return "s(" + origin + ", " + radius + ")";
	}
	
	

}
