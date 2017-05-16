package RayTracing;

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
	private ArrayList<Primitive> all_primitives;
	
	public class PrimitiveAndIntersection
	{
		public Primitive primitive;
		public Vector intersection;

		public PrimitiveAndIntersection(Primitive primitive, Vector intersection) 
		{
			this.primitive = primitive;
			this.intersection = intersection;
		}
	}
	
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
	

	/**
	 * @pre - depth >= 1
	 * @param ray
	 * @param depth
	 * @return
	 */
	public Color getColorAtIntersectionOfRay(Ray ray, int depth)
	{
		//-- find closest intersection
		// TODO: can change "getClosestIntersectionAndPrimitive" to return both closest and
		// second closest to the convenience of transparency calculation.
		// => "get2ClosestIntersectionAndPrimitive" returns an array of 2 of PrimitiveAndIntersection.
		PrimitiveAndIntersection pai = 
				getClosestIntersectionAndPrimitive(ray);
		
		Vector closest_intersection = pai.intersection; 
		Primitive closest_primitive = pai.primitive;
		
		//-- if intersection == null return bgcolor
		if (closest_intersection == null || depth == max_recursion_lvl)
			return bgcolor;
			
		Material m = closest_primitive.getMaterial();
		double transp = m.getTransparencyCoeff();

		//-- get diffusion color at intersection - including soft shadows
		//		+ doesn't cast more rays (only toward lights)
		Color diff_color = getDiffusionColorAtIntersection(closest_primitive, ray, closest_intersection);
		
		//-- get reflective color at intersection: (casts a ray recursively)
		//	+ cast ray from intersection in the correct direction recursively
		//	  depth = depth + 1 (if depth == max_recursion_lvl return bgcolor)
		//  + inside this method if reflection color is black then return black immediately (no recursion).
		Color reflection_color = getReflectionColor(ray, closest_intersection, closest_primitive, depth); //give it "inRay": inside this function it will cast an "outRay"
		
		//-- get transparent color at intersection: (computes color of next intersection of the same ray)
		// + cast ray through primitive and get color "behind" it
		// 	 same recursion.
		//  + inside this method if transparencyCoeff is zero then return black immediately (no recursion).
		Color transparency_color = getTransparencyColor(ray, depth); //searches for the 2nd intersection...
		
		//-- get specular color at intersection point
		//      + doesn't cast more rays (only toward lights)
		Color specular_color = getSpecularColor(closest_primitive, ray, closest_intersection);
		
		//-- color is:
		//	transparency * "behind_color" + (1 - transparency) * (diffuse_color + specular_color) + relection_color
		Color retColor = transparency_color.scalarMultipy(transp).add(
				diff_color.add(specular_color).scalarMultipy(1 - transp)).add(reflection_color);
				
		// return color.trim()
		retColor.trim();
		return retColor;
	}
	 
	private Color getSpecularColor(Primitive closest_primitive, Ray ray, Vector closest_intersection) 
	{
		Color retColor = new Color(0,0,0);
		//-- compute R vector (reflect)
		// 	R = (2L*N)N-L
		try {
			Color specColor = closest_primitive.getMaterial().getSpecularColor();
			double specIntens = closest_primitive.getMaterial().getSpecularityCoeff(); 
			Vector N = closest_primitive.normalAtIntersection(ray);
			for (Light lit : lights)
			{
				//-- go back a little:
				Vector go_back_a_little = new Vector(closest_intersection.substract(ray.getVector().timesScalar(Math.pow(10, -10))));//TODO: think about the correct timesScalar(?)

				Vector L = lit.getOrigin().substract(go_back_a_little);
				Ray r = new Ray(go_back_a_little, L);
				
				if (!firstHit(r, lit)) //if in the shadow: take account for shadow intensity.
				{
					continue;
				}
				
				//-- compute R vector (reflect)
				// 	R = (2L*N)N-L
				double two_L_dot_N = 2*L.dotProduct(N);
				Vector R = N.timesScalar(two_L_dot_N).substract(L).toUnit();
				Vector V = ray.getVector().timesScalar(-1).toUnit();
				double directionalIntensity = Math.pow(R.dotProduct(V), specIntens);
				Color litEffect = specColor.multiply(lit.getColor()).scalarMultipy(directionalIntensity);
				
				retColor = retColor.add(litEffect);
			}
		} catch (RayTracerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retColor;
	}

	private Color getTransparencyColor(Ray ray, int depth) {
		// TODO Auto-generated method stub
		return new Color(0,0,0);
	}

	private Color getReflectionColor(Ray ray, Vector closest_intersection, Primitive closest_primitive, int depth) {
		// TODO Auto-generated method stub
		return new Color(0,0,0);
	}

	public PrimitiveAndIntersection getClosestIntersectionAndPrimitive(Ray ray) 
	{
		// TODO: now doesn't account for hitting lights!
		Vector closest_intersection = null;
		Primitive closest_primitive = null;
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
						double currMinLength = closest_intersection.substract(ray.getOrigin()).magnitude();
						double thisLength = curr_intsc.substract(ray.getOrigin()).magnitude();
						if (thisLength < currMinLength)
						{
							closest_intersection = curr_intsc;
							closest_primitive = primitive;
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
		return new PrimitiveAndIntersection(closest_primitive, closest_intersection);
	}
	
	public Color getBGColor()
	{
		return new Color(bgcolor);
	}
	
	private Color getDiffusionColorAtIntersection(Primitive primitive, Ray inRay, Vector intersection) 
	{
		Color color = new Color(0,0,0);
		Color K_d = primitive.getMaterial().getDiffuseColor();
		for (Light lit : lights)
		{
			try
			{
				Color litEffect = new Color(0,0,0);
				//-- go back a little:
				Vector go_back_a_little = new Vector(intersection.substract(inRay.getVector().timesScalar(Math.pow(10, -10))));//TODO: think about the correct timesScalar(?)

				Vector L = lit.getOrigin().substract(go_back_a_little);
				Vector N = primitive.normalAtIntersection(inRay).toUnit();
				
				//TODO: soft shadows : need more rays to be cast towards the light.
				
				Ray r = new Ray(go_back_a_little, L);
				
				Color I_p = lit.getColor();
				double factor = N.dotProduct(L.toUnit());
				if (factor < 0)
					factor = 0;
				
				litEffect = K_d.multiply(I_p).scalarMultipy(factor);
				if (!firstHit(r, lit)) //if in the shadow: take account for shadow intensity.
				{
					litEffect = litEffect.scalarMultipy(1 - lit.getShadowIntensity());
				}
				color = color.add(litEffect);
			}
			catch (RayTracerException e)
			{
				e.printStackTrace();
				continue;
			}
		}
		return color;
	}

	private boolean firstHit(Ray r, Light lit) 
	{
		for (Primitive primitive : all_primitives)
		{
			try 
			{
				Vector in = primitive.closerIntersectionPoint(r);
				if (in == null)
				{
					continue;
				}
				else
				{
					double lengthToPrimitive = in.substract(r.getOrigin()).magnitude();
					double lengthToLight = lit.getOrigin().substract(r.getOrigin()).magnitude();
					if (lengthToPrimitive < lengthToLight)
					{
						return false;
					}					
				}
			} 
			catch (RayTracerException e) 
			{
				System.out.println("firstHit: exception");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("Set(%s, shadow(%d), rec(%d), sampl(%d))", bgcolor,
				num_of_shadow_rays,  max_recursion_lvl,  super_sampling_lvl);
	}

	public void formAllPrimitivesList() {
		all_primitives = new ArrayList<>();
		all_primitives.addAll(spheres);
		all_primitives.addAll(planes);
		all_primitives.addAll(triangles);
	}


	
	
}
