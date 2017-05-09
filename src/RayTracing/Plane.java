package RayTracing;

import RayTracing.RayTracer.RayTracerException;

public class Plane extends Primitive {

	protected Vector normal;
	protected double d; 		//offset along the normal
	
	public Plane(Material material, Vector Normal, double d) {
		super(material);
		this.normal = Normal.toUnit();
		this.d = d;
	}

	@Override
	public Vector closerIntersectionPoint(Ray r) throws RayTracerException 
	{
		double t = - (r.getOrigin().dotProduct(normal) + d) 
				/ (r.getVector().dotProduct(normal));
		Vector P = r.getOrigin().add(r.getUnitTimes(t));
		return P;
	}

	@Override
	public Vector normalAtIntersection(Ray r) throws RayTracerException {
		// FIXME: 	in here we might want to return (-normal) sometimes
		// 			depending on the location of r.origin
		return new Vector(normal);
	}
	
	@Override
	public String toString() {
		return "Pln(" + normal + ", " + d +")";
	}

}
