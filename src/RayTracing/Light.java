package RayTracing;

import RayTracing.RayTracer.RayTracerException;

public class Light {

	private Vector origin;
	private Color color;
	private double specular_intensity;
	private double shadow_intensity;
	private double radius;
	
	public Light(Vector origin,
			Color color,
			double spec_ints,
			double shadow_ints,
			double radius)
	{
		this.origin = origin;
		this.color = new Color(color);
		
		this.specular_intensity = spec_ints;
		this.shadow_intensity = shadow_ints;
		this.radius = radius;
	}

	public static Light parseLight(String[] params, Set set) {
		double px = Double.parseDouble(params[0]);
		double py = Double.parseDouble(params[1]);
		double pz = Double.parseDouble(params[2]);
		
		double color_r = Double.parseDouble(params[0+3]);
		double color_g = Double.parseDouble(params[1+3]);
		double color_b = Double.parseDouble(params[2+3]);
		
		double spec_intensity = Double.parseDouble(params[6]);
		double shadow_intensity = Double.parseDouble(params[7]);
		double radius = Double.parseDouble(params[8]);
	
		return new Light(new Vector(px, py, pz),
				new Color(color_r, color_g, color_b),
				spec_intensity, shadow_intensity, radius);
	}

	public Vector getOrigin() 
	{
		return new Vector(origin);
	}

	public Color getColor() {
		return new Color(color);
	}
	
	public double getShadowIntensity()
	{
		return shadow_intensity;
	}

	public double getRadius() {
		return radius;
	}
}
