package RayTracing;

import RayTracing.RayTracer.RayTracerException;

public abstract class Primitive {
	
	/**
	 * This classed is extended by sphere, triangle, plane...
	 */
	
	private Material material;
	
	public Primitive(Material material)
	{
		if(material == null)
			this.material = null;
		else
			this.material = new Material(material);
	}
	
	public abstract Vector closerIntersectionPoint(Ray r) throws RayTracerException;
	
	public abstract Vector normalAtIntersection(Ray r) throws RayTracerException;
	
	public Material getMaterial()
	{
		return new Material(material);
	}
}