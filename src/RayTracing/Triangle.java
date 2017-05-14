package RayTracing;

import RayTracing.RayTracer.RayTracerException;

public class Triangle extends Primitive {

	private Vector T1;
	private Vector T2;
	private Vector T3;
	private Plane pln;
	
	public Triangle(Material material, Vector T1, Vector T2, Vector T3) 
	{
		super(material);
		
		this.T1 = new Vector(T1);
		this.T2 = new Vector(T2);
		this.T3 = new Vector(T3);
		
		// for some point p=(x,y,z) on a plane, the plane is
		// represented by: p*N + d = 0
		
		// calc norm
		Vector side_a = T2.substract(T1);
		Vector side_b = T3.substract(T1);
		Vector norm = side_a.crossProduct(side_b).toUnit();
		
		// select p on plane as T1
		double d = T1.dotProduct(norm);
		
		this.pln = new Plane(material, norm, d);
	}
	
	/**
	 * Compute it parametrically
	 */
	@Override
	public Vector closerIntersectionPoint(Ray r) throws RayTracerException 
	{
		Vector p = pln.closerIntersectionPoint(r);
		if (p == null)
			return null;
		Vector v1p = p.substract(T1);
		
		Vector v12 = T2.substract(T1).toUnit();
		double l_a = v1p.dotProduct(v12);
		double alpha = l_a / T2.substract(T1).magnitude();
		
		Vector v13 = T3.substract(T1).toUnit();
		double l_b = v1p.dotProduct(v13);
		double beta = l_b / T3.substract(T1).magnitude();
		
		if (0 <= alpha && alpha <= 1 &&
				0 <= beta && beta <= 1 &&
				alpha + beta <= 1)
		{
			return p;
		}
		return null;
	}

	@Override
	public Vector normalAtIntersection(Ray r) throws RayTracerException {
		if (closerIntersectionPoint(r) == null) {
			return null;
		}
		return pln.normalAtIntersection(r);
	}

	@Override
	public String toString() {
		return "Tr(" + T1 + ", " + T2 + ", " + T3 +")";
	}

	public static Triangle parseTriangle(String[] params, Set set) throws RayTracerException 
	{
		double px1 = Double.parseDouble(params[0+0]);
		double py1 = Double.parseDouble(params[1+0]);
		double pz1 = Double.parseDouble(params[2+0]);
		
		double px2 = Double.parseDouble(params[0+3]);
		double py2 = Double.parseDouble(params[1+3]);
		double pz2 = Double.parseDouble(params[2+3]);
		
		double px3 = Double.parseDouble(params[0+6]);
		double py3 = Double.parseDouble(params[1+6]);
		double pz3 = Double.parseDouble(params[2+6]);
		
		int matInx = Integer.parseInt(params[9]); 
		
		Material m = set.getMaterial(matInx);
		if (m != null)
			return new Triangle(m, new Vector(px1,py1,pz1),
					new Vector(px2, py2, pz2),
					new Vector(px3, py3, pz3));
		else
			throw new RayTracerException("Triangle material index error!");
	}
}
