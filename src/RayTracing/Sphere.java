package RayTracing;

import RayTracing.RayTracer.RayTracerException;

public class Sphere extends Primitive {

	private Vector origin;
	private double radius;
	
	public Sphere(Material material, Vector origin, double radius)
	{
		super(material);
		this.origin = new Vector(origin);
		this.radius = radius;
	}

	public Vector[] calculate_intersections(Ray r) throws RayTracerException
	{

		//Calculations were took from Dani's presentation.
		Vector L = origin.substract(r.getOrigin());
		double t_ca = L.dotProduct(r.getVector());
		if (t_ca < 0)// && (origin.substract(r.getOrigin()).magnitude() > radius))
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

		//Act differently when ray's origin is inside sphere -
		//take only one solution: (t_hc - t_ca) or (t_hc + t_ca) (calcs explanations are not included here yet)
		if (origin.substract(r.getOrigin()).magnitude() < radius)
		{
			
			double t3 = t_hc + t_ca;
			intersection_points[0] =  r.getOrigin().add(r.getUnitTimes(t3));
			intersection_points[1] = null;
		}
		
		return intersection_points;
	}

	@Override
	public Vector closerIntersectionPoint(Ray r) throws RayTracerException
	{
		Vector[] intersections = this.calculate_intersections(r);
		if (intersections == null)
			return null;
		else if (intersections[0] == null && intersections[1] == null)
			return null;
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
		Vector N = P.substract(origin);
		if (origin.substract(r.getOrigin()).magnitude() < radius)
		{
			N = N.timesScalar(-1.0);
		}
		N.normalize();
		return N;
	}
	
	@Override
	public String toString() {
		return "s(" + origin + ", " + radius + ")";
	}

	public static Sphere parseSphere(String[] params, Set set) throws RayTracerException 
	{
		double px = Double.parseDouble(params[0]);
		double py = Double.parseDouble(params[1]);
		double pz = Double.parseDouble(params[2]);
		
		double rd = Double.parseDouble(params[3]);
		int matInx = Integer.parseInt(params[4]); 
		
		Material m = set.getMaterial(matInx);
		if (m != null){
			Material mtag = new Material(m);
			mtag.makeMoreTransparent();
			return new Sphere(mtag, new Vector(px,py,pz), rd);			
		}
		else
			throw new RayTracerException("Sphere material index error!");
	}

}
