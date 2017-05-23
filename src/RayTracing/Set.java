package RayTracing;

import java.util.ArrayList;
import java.util.Random;

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

	private Camera cam;

	private double go_a_little_factor = Math.pow(10, -10);//TODO: think about the correct value

	public class PrimitiveAndIntersection {
		public Primitive primitive;
		public Vector intersection;

		public PrimitiveAndIntersection(Primitive primitive, Vector intersection) {
			this.primitive = primitive;
			this.intersection = intersection;
		}
	}

	public Set(double bg_r, double bg_g, double bg_b,
			   int shadow_rays, int rec_lvl, int sampl_lvl) {
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

	public static Set parseSet(String[] args) {
		/* "set" code */

		double bg_r = Double.parseDouble(args[0]);
		double bg_g = Double.parseDouble(args[1]);
		double bg_b = Double.parseDouble(args[2]);

		int shadow_rays = Integer.parseInt(args[3]);
		int rec_lvl = Integer.parseInt(args[4]);
		int sampl_lvl = Integer.parseInt(args[5]);

		return new Set(bg_r, bg_g, bg_b,
				shadow_rays, rec_lvl, sampl_lvl);
	}

	public void addMaterial(Material mat) {
		materials.add(mat);
	}

	public void addLight(Light lit) {
		lights.add(lit);
	}

	public void addSphere(Sphere sph) {
		spheres.add(sph);
	}

	public void addPlane(Plane pln) {
		planes.add(pln);
	}

	public void addTriangle(Triangle trng) {
		triangles.add(trng);
	}

	public Material getMaterial(int matInx) {
		for (Material m : materials) {
			if (m.getMaterialIndex() == matInx)
				return new Material(m);
		}
		return null;
	}

	public boolean hasMaterial() {
		return !materials.isEmpty();
	}

	public Color getBGColor() {
		return new Color(bgcolor);
	}

	public int getSuper_sampling_lvl() {
		return super_sampling_lvl;
	}

	public void setCamera(Camera cam) {
		this.cam = cam;
	}

	public void formAllPrimitivesList() {
		all_primitives = new ArrayList<>();
		all_primitives.addAll(spheres);
		all_primitives.addAll(planes);
		all_primitives.addAll(triangles);
	}

	@Override
	public String toString() {
		return String.format("Set(%s, shadow(%d), rec(%d), sampl(%d))", bgcolor,
				num_of_shadow_rays, max_recursion_lvl, super_sampling_lvl);
	}

	public PrimitiveAndIntersection[] get2ClosestIntersectionAndPrimitive(Ray ray) {
		Vector closest_intersection = null;
		Primitive closest_primitive = null;
		Vector second_closest_intersection = null;
		Primitive second_closest_primitive = null;
		for (Primitive primitive : all_primitives) {
			try {
				Vector curr_intsc = primitive.closerIntersectionPoint(ray);
				if (curr_intsc == null) {
					continue;
				} else {
					if (closest_intersection != null) {
						double currMinLength = closest_intersection.substract(ray.getOrigin()).magnitude();
						double thisLength = curr_intsc.substract(ray.getOrigin()).magnitude();
						if (thisLength < currMinLength) {
							second_closest_intersection = closest_intersection;
							second_closest_primitive = closest_primitive;
							closest_intersection = curr_intsc;
							closest_primitive = primitive;
						} else if (second_closest_intersection == null) {
							// second intersection
							second_closest_intersection = curr_intsc;
							second_closest_primitive = primitive;
						} else {
							double currSecondMinLength = second_closest_intersection.substract(ray.getOrigin()).magnitude();
							if (thisLength < currSecondMinLength) {
								second_closest_intersection = curr_intsc;
								second_closest_primitive = primitive;
							}
						}
					} else {
						// first intersection
						closest_intersection = curr_intsc;
						closest_primitive = primitive;
					}
				}
			} catch (RayTracerException e) {
				System.out.println("Caught exception, camera inside sphere");
				e.printStackTrace();
			}
		}
		PrimitiveAndIntersection retArr[] = new PrimitiveAndIntersection[2];
		retArr[0] = new PrimitiveAndIntersection(closest_primitive, closest_intersection);
		retArr[1] = new PrimitiveAndIntersection(second_closest_primitive, second_closest_intersection);

		return retArr;
	}

	private double notFirstHitFactor(Ray r, Vector V) {

		double retVal = 1.0;
		for (Primitive primitive : all_primitives) {
			try {
				Vector in = primitive.closerIntersectionPoint(r);
				if (in == null) {
					continue;
				} else {
					double lengthToPrimitive = in.substract(r.getOrigin()).magnitude();
					double lengthToLight = V.substract(r.getOrigin()).magnitude();
					if ((lengthToPrimitive < lengthToLight)) {
						retVal *= primitive.getMaterial().getTransparencyCoeff();
					}
				}
			} catch (RayTracerException e) {
				System.out.println("notFirstHitFactor: exception");
				e.printStackTrace();
				return retVal;
			}
		}
		return retVal;
	}

	private boolean isFirstHit(Ray r, Vector P) {

		for (Primitive primitive : all_primitives) {
			try {
				Vector in = primitive.closerIntersectionPoint(r);
				if (in == null) {
					continue;
				} else {
					double lengthToPrimitive = in.substract(r.getOrigin()).magnitude();
					double lengthToLight = P.substract(r.getOrigin()).magnitude();
					if ((lengthToPrimitive < lengthToLight)) {
						return false;
					}
				}
			} catch (RayTracerException e) {
				System.out.println("isFirstHit: exception");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/////////////////////////
	///Get Color Functions///
	/////////////////////////
	/**
	 * @param ray
	 * @param depth
	 * @return
	 * @pre - depth >= 1
	 */
	public Color getColorAtIntersectionOfRay(Ray ray, int depth) {
		//Check if got to max recursion level
		if (depth >= max_recursion_lvl)
			return bgcolor;

		//-- find closest intersection
		PrimitiveAndIntersection[] pai = get2ClosestIntersectionAndPrimitive(ray);

		Vector closest_intersection = pai[0].intersection;
		Primitive closest_primitive = pai[0].primitive;

		//-- if intersection == null return bgcolor
		if (closest_intersection == null)
			return bgcolor;

		Material m = closest_primitive.getMaterial();
		double transp = m.getTransparencyCoeff();
		Color material_reflection_color = m.getReflectionColor();

		//-- get diffusion color at intersection - including soft shadows
		//		+ doesn't cast more rays (only toward lights)
		Color diff_color = getDiffusionColorAtIntersection(closest_primitive, ray, closest_intersection);

		//-- get reflective color at intersection: (casts a ray recursively)
		//	+ cast ray from intersection in the correct direction recursively
		//	  depth = depth + 1 (if depth == max_recursion_lvl return bgcolor)
		//  + if reflection color is black then return black immediately (no recursion).
		Color reflection_color = new Color(0, 0, 0);
		if (material_reflection_color.isNotBlack()) {
			reflection_color = material_reflection_color.multiply(getReflectionColor(ray, closest_intersection, closest_primitive, depth));
		}

		//-- get transparent color at intersection: (computes color of next intersection of the same ray)
		// + cast ray through primitive and get color "behind" it
		// 	 same recursion.
		//  + inside this method if transparencyCoeff is zero then return black immediately (no recursion).
		Color transparency_color = getTransparencyColor(ray, depth, pai, transp);

		//-- get specular color at intersection point
		//      + doesn't cast more rays (only toward lights)
		Color specular_color = getSpecularColor(closest_primitive, ray, closest_intersection);

		//-- color is:
		//	transparency * "behind_color" + (1 - transparency) * (diffuse_color + specular_color) + relection_color
		Color retColor = (transparency_color.scalarMultiply(transp)).add(
						 ((diff_color.add(specular_color)).scalarMultiply(1.0 - transp)).add(
						  reflection_color));

		// return color.trim()
		retColor.trim();
		return retColor;
	}

	private Color getDiffusionColorAtIntersection(Primitive primitive, Ray inRay, Vector intersection) {
		Color color = new Color(0, 0, 0);
		Color K_d = primitive.getMaterial().getDiffuseColor();
		for (Light lit : lights) 
		{
			try 
			{
				Color I_p = lit.getColor();
				Color litEffect = new Color(0, 0, 0);
				//-- go back a little:
				Vector go_back_a_little = new Vector(intersection.substract(inRay.getVector().timesScalar(go_a_little_factor)));
				Vector N = primitive.normalAtIntersection(inRay).toUnit();

				//-- define light rectangle
				// http://web.cse.ohio-state.edu/~shen.94/681/Site/Slides_files/basic_algo.pdf
				// slide 17:

				Vector n = lit.getOrigin().substract(intersection).toUnit();
				Vector u = cam.get_up_vector().crossProduct(n).toUnit();
				Vector v = n.crossProduct(u);

				// slide 19:
				double a = lit.getRadius();
				double slot = a / num_of_shadow_rays;

				Vector C = new Vector(lit.getOrigin());

				Vector BottomLeftCorner = C.substract(
						u.timesScalar(a / 2)).substract(
						v.timesScalar(a / 2));

				Random generator = new Random();
				double numOfShadowRaysHit = 0;
				for (int ii = 0; ii < num_of_shadow_rays; ii++) {
					for (int jj = 0; jj < num_of_shadow_rays; jj++) {
						Color pLitEffect = new Color(0, 0, 0);
						Vector P = BottomLeftCorner.add(u.timesScalar(jj * slot)).add(v.timesScalar(ii * slot));
						P = P.add(u.timesScalar(slot * generator.nextDouble())).add(
								v.timesScalar(slot * generator.nextDouble()));

						Vector L = P.substract(go_back_a_little);

						double factor = N.dotProduct(L.toUnit());
						if (factor < 0) {
							factor = 0;
						}
						pLitEffect = K_d.multiply(I_p).scalarMultiply(factor);

						Ray r = new Ray(go_back_a_little, L);

						double notFirstHitFactor = notFirstHitFactor(r, P);
						if (!isFirstHit(r, P)){
							pLitEffect = pLitEffect.scalarMultiply(1 - lit.getShadowIntensity()*(1-notFirstHitFactor));
						}
//						numOfShadowRaysHit += notFirstHitFactor;
//
//						if (notFirstHitFactor != 1.0){
//							//TODO: consider removing "*notFirstHitFactor"
//							//pLitEffect = pLitEffect.scalarMultiply(1 - lit.getShadowIntensity()*notFirstHitFactor);
//							pLitEffect = pLitEffect.scalarMultiply(1 - lit.getShadowIntensity());
//						}


						litEffect = litEffect.add(pLitEffect);
					}
				}
//				double shadow_rays_factor = numOfShadowRaysHit / (num_of_shadow_rays * num_of_shadow_rays);
//				litEffect = litEffect.scalarMultiply(shadow_rays_factor);
				litEffect = litEffect.scalarMultiply(1.0 / (num_of_shadow_rays * num_of_shadow_rays));
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

	private Color getReflectionColor(Ray ray_in, Vector closest_intersection, Primitive closest_primitive, int depth) {
		//Check if got to max recursion level
		if (depth >= max_recursion_lvl)
			return bgcolor;

		try {
			//-- compute R vector (reflection)
			// 	R = V -2(V*N)N
			Vector V = ray_in.getVector();
			Vector N = closest_primitive.normalAtIntersection(ray_in);
			Vector ray_out_vector = V.substract(N.timesScalar(V.dotProduct(N) * 2));

			Vector orig_go_front_a_little = new Vector(closest_intersection.add(ray_out_vector.timesScalar(go_a_little_factor)));
			Ray ray_out = new Ray(orig_go_front_a_little, ray_out_vector);

			return getColorAtIntersectionOfRay(ray_out, depth + 1);
		} catch (RayTracerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bgcolor;
	}

	private Color getTransparencyColor(Ray ray, int depth, PrimitiveAndIntersection[] pai, double transp) {
		//Check if got to max recursion level
		if (depth >= max_recursion_lvl)
			return bgcolor;

		Color transparency_color = new Color(0, 0, 0);
		if (transp != 0) {
			transparency_color = bgcolor;

			Vector go_front_a_little = new Vector(pai[0].intersection.add(ray.getVector().timesScalar(go_a_little_factor)));
			Ray r = new Ray(go_front_a_little, ray.getVector());
			PrimitiveAndIntersection[] pai_new = get2ClosestIntersectionAndPrimitive(r);
			Vector closest_intersection = pai_new[0].intersection;

			//-- if intersection == null return bgcolor
			if (closest_intersection != null)
				transparency_color = getColorAtIntersectionOfRay(r, depth + 1);

//			if (pai[1].intersection != null) { //found 2nd intersection
//				Ray r = new Ray(pai[1].intersection, ray.getVector());
//				transparency_color = getColorAtIntersectionOfRay(r, depth + 1);
//			}
		}
		return transparency_color;
	}

	private Color getSpecularColor(Primitive closest_primitive, Ray ray, Vector closest_intersection) {
		Color retColor = new Color(0, 0, 0);
		//-- compute R vector (reflect)
		// 	R = (2L*N)N-L
		try {
			Color specColor = closest_primitive.getMaterial().getSpecularColor();
			double specIntens = closest_primitive.getMaterial().getSpecularityCoeff();
			Vector N = closest_primitive.normalAtIntersection(ray);
			for (Light lit : lights) {
				//-- go back a little:
				Vector go_back_a_little = new Vector(closest_intersection.substract(ray.getVector().timesScalar(go_a_little_factor)));

				Vector L = lit.getOrigin().substract(go_back_a_little);
				Ray r = new Ray(go_back_a_little, L);

//				double notFirstHitFactor = notFirstHitFactor(r, lit.getOrigin());
//				if (notFirstHitFactor == 0) //if in the shadow: take account for shadow intensity.
//				{
//					continue;
//				}

				if (!isFirstHit(r, lit.getOrigin())) //if in the shadow: take account for shadow intensity.
				{
					continue;
				}

				//-- compute R vector (reflect)
				// 	R = (2L*N)N-L
				double two_L_dot_N = 2 * L.dotProduct(N);
				Vector R = N.timesScalar(two_L_dot_N).substract(L).toUnit();
				Vector V = ray.getVector().timesScalar(-1).toUnit();
				double directionalIntensity = Math.pow(R.dotProduct(V), specIntens);
				double litSpecIntens = lit.getSpecularIntensity();
				Color litEffect = specColor.multiply(lit.getColor().scalarMultiply(litSpecIntens)).scalarMultiply(directionalIntensity);//.scalarMultiply(notFirstHitFactor);

				retColor = retColor.add(litEffect);
			}
		} catch (RayTracerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retColor;
	}

}