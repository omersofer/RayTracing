package RayTracing;

public class Light {

	private Vector origin;
	private double[] color;
	private double specular_intensity;
	private double shadow_intensity;
	private double radius;
	
	public Light(Vector origin,
			double r, double g, double b,
			double spec_ints,
			double shadow_ints,
			double radius)
	{
		this.origin = origin;
		this.color = new double[3];
		color[0] = r;
		color[1] = g;
		color[2] = b;
		
		this.specular_intensity = spec_ints;
		this.shadow_intensity = shadow_ints;
		this.radius = radius;
	}
}
