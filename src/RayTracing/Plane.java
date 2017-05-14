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
		//Plane equation is: P*N-d = 0.
		//Ray equation is : P(t) = O + t*V
		//To find intersection substitute: t = (d-N*O)/(V*N)
		double t = (d - normal.dotProduct(r.getOrigin())) / (r.getVector().dotProduct(normal));
		if (t < 0)
		{
			return null;
		}
		else
		{
			Vector P = r.getOrigin().add(r.getUnitTimes(t));
			return P;
		}
	}

	@Override
	public Vector normalAtIntersection(Ray r) throws RayTracerException {
		Vector intersection = closerIntersectionPoint(r);
		if (intersection == null)
		{
			return null;
		}
		else
		{
			//TODO: this is still not understood - check which normal should be returned in each case.
			Vector a_little_this_way = intersection.add(normal);
			Vector a_little_that_way = intersection.add(normal.timesScalar(-1));
			double this_way_dist = a_little_this_way.substract(r.getOrigin()).magnitude();
			double that_way_dist = a_little_that_way.substract(r.getOrigin()).magnitude();
			if (this_way_dist < that_way_dist)
			{
				return new Vector(normal);
			}
			else
			{
				return normal.timesScalar(-1);

			}
		}
	}
	
	@Override
	public String toString() {
		return "Pln(" + normal + ", " + d +")";
	}

	public static Plane parsePlane(String[] params, Set set) throws RayTracerException 
	{
		double nx = Double.parseDouble(params[0]);
		double ny = Double.parseDouble(params[1]);
		double nz = Double.parseDouble(params[2]);
		
		double offs = Double.parseDouble(params[3]);
		int matInx = Integer.parseInt(params[4]); 
		
		Material m = set.getMaterial(matInx);
		if (m != null)
			return new Plane(m, new Vector(nx,ny,nz), offs);
		else
			throw new RayTracerException("Plane material index error!");
	}

}
