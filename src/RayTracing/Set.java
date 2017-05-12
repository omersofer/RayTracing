package RayTracing;

import java.security.AllPermission;
import java.util.ArrayList;

import RayTracing.RayTracer.RayTracerException;

public class Set {
	
	private Color bgcolor;
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
		bgcolor = new Color(bg_r, bg_g, bg_b);
		
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
	

	public Color getColorAtIntersectionOfRay(Ray ray) 
	{
		Vector closest_intersection = null;
		Primitive closest_primitive = null;
		
		ArrayList<Primitive> all_primitives = new ArrayList<>();
		all_primitives.addAll(spheres);
		all_primitives.addAll(planes);
		all_primitives.addAll(triangles);
		// FIXME: doesn't account for hitting lights!
		for (Primitive primitive : all_primitives)
		{
			try 
			{
				Vector curr_intsc = primitive.closerIntersectionPoint(ray);
				if (curr_intsc == null)
				{
					continue;					
				}
				else
				{
					if (closest_intersection != null)
					{
						//System.out.println("for ray " + ray + " intersection was not null!");
						double currMinLength = closest_intersection.substract(ray.getOrigin()).magnitude();
						double thisLength = curr_intsc.substract(ray.getOrigin()).magnitude();
						if (thisLength < currMinLength)
						{
							closest_primitive = primitive;
							closest_intersection = curr_intsc;
						}
					}
					else
					{
						// first intersection
						closest_intersection = curr_intsc;
						closest_primitive = primitive;
					}
				}
			}
			catch (RayTracerException e) 
			{
				System.out.println("Caught exception, camera inside sphere");
				e.printStackTrace();
			}
		}

		Color colorAtIntersection = this.bgcolor;
		if (closest_intersection != null)
		{
			colorAtIntersection = getColorAtIntersection(closest_primitive, closest_intersection);
		}
		
		return colorAtIntersection;
	}
	
	public Color getBGColor()
	{
		return new Color(bgcolor);
	}
	
	private Color getColorAtIntersection(Primitive closest_primitive, Vector closest_intersection) 
	{
		// FIXME
		return closest_primitive.getMaterial().getDiffuseColor();
	}

	@Override
	public String toString() {
		return String.format("Set(%s, shadow(%d), rec(%d), sampl(%d))", bgcolor,
				num_of_shadow_rays,  max_recursion_lvl,  super_sampling_lvl);
	}


	
	
}
