package RayTracing;

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
	
	public abstract boolean instersects(Vector v);
	
	public abstract Vector intersectionPoint(Vector v);
}