package RayTracing;

import java.util.ArrayList;

public class Set {
	
	private double[] bgcolor;
	private int num_of_shadow_rays;
	private int max_recursion_lvl;
	private int super_sampling_lvl;
	
	private ArrayList<Material> materials;
	private ArrayList<Light> lights;
	private ArrayList<Sphere> spheres;
	private ArrayList<Plane> planes;
	private ArrayList<Triangle> triangles;
	
	public Set(double bg_r, double bg_g, double bg_b,
			int shadow_rays, int rec_lvl, int sampl_lvl)
	{
		bgcolor = new double[3];
		bgcolor[0] = bg_r;
		bgcolor[1] = bg_g;
		bgcolor[2] = bg_b;
		
		num_of_shadow_rays = shadow_rays;
		max_recursion_lvl = rec_lvl;
		super_sampling_lvl = sampl_lvl;
		
		materials = new ArrayList<>();
		lights = new ArrayList<>();
		spheres = new ArrayList<>();
		planes = new ArrayList<>();
		triangles = new ArrayList<>();
	}
	
	public static Set parseSet(String[] args)
	{
		/* "set" code */
		
		double bg_r = Double.parseDouble(args[0]);
		double bg_g = Double.parseDouble(args[1]);
		double bg_b = Double.parseDouble(args[2]);
		
		int shadow_rays = Integer.parseInt(args[3]);
		int rec_lvl = Integer.parseInt(args[4]);
		int sampl_lvl = Integer.parseInt(args[5]);
		
		return new Set( bg_r,  bg_g,  bg_b,
				 shadow_rays,  rec_lvl,  sampl_lvl);
	}
	
	public void addMaterial(Material mat)
	{
		materials.add(mat);
	}
	
	public void addLight(Light lit)
	{
		lights.add(lit);
	}
	
	public void addSphere(Sphere sph)
	{
		spheres.add(sph);
	}
	
	public void addPlane(Plane pln)
	{
		planes.add(pln);
	}
	
	public void addTriangle(Triangle trng)
	{
		triangles.add(trng);
	}
	
	public Material getMaterial(int matInx) 
	{
		for (Material m : materials)
		{
			if (m.getMaterialIndex() == matInx)
				return new Material(m);
		}
		return null;
	}
	
	public boolean hasMaterial()
	{
		return !materials.isEmpty();
	}
	
	@Override
	public String toString() {
		return String.format("Set(rgb(%.2f,%.2f,%.2f), shadow(%d), rec(%d), sampl(%d))", bgcolor[0],  bgcolor[1], bgcolor[2],
				num_of_shadow_rays,  max_recursion_lvl,  super_sampling_lvl);
	}

	
	
}
