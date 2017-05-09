package RayTracing;

import RayTracing.RayTracer.RayTracerException;

public abstract class Primitive {
	
	/**
	 * This classed is supposed to be
	 * extended by sphere, triangle, plane...
	 */
	
	private Material material;
	
	public Primitive(Material material)
	{
		this.material = material;
	}
	
	public abstract Vector closerIntersectionPoint(Ray r) throws RayTracerException;
	
	public abstract Vector normalAtIntersection(Ray r) throws RayTracerException;
	
	public Material getMaterial()
	{
		return new Material(material);
	}
}