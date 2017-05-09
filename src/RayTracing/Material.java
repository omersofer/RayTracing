package RayTracing;

public class Material 
{
	public final static int R = 0;
	public final static int G = 1;
	public final static int B = 2;
	
	private double[] diffuseColor;
	private double[] specularColor;
	private double specularityCoeff;
	private double[] reflectionColor;
	private double transparencyCoeff;
	
	
	public Material(double diff_r, double diff_g, double diff_b,
			double spec_r, double spec_g, double spec_b,
			double spec_coeff,
			double refl_r, double refl_g, double refl_b,
			double trans_coeff)
	{
		diffuseColor = new double[3];
		diffuseColor[R] = diff_r;
		diffuseColor[G] = diff_g;
		diffuseColor[B] = diff_b;
		
		specularColor = new double[3];
		specularColor[R] = spec_r;
	    specularColor[G] = spec_g;
		specularColor[B] = spec_b;
		
		specularityCoeff = spec_coeff;
		
		reflectionColor[R] = refl_r;
		reflectionColor[G] = refl_g;
		reflectionColor[B] = refl_b;
		
		transparencyCoeff = trans_coeff;
	}
	
	public Material(Material m)
	{
		this(m.diffuseColor[R],
				m.diffuseColor[G],
				m.diffuseColor[B],
				m.specularColor[R],
				m.specularColor[G],
				m.specularColor[B],
				m.specularityCoeff,
				m.reflectionColor[R],
				m.reflectionColor[G],
				m.reflectionColor[B],
				m.transparencyCoeff);
	}
	
	public double[] getDiffuseColor() {
		double[] copy = new double[3];
		System.arraycopy(diffuseColor, 0, copy, 0, diffuseColor.length);
		return copy;
	}
	public double[] getSpecularColor() {
		return specularColor;
	}
	public double getSpecularityCoeff() {
		return specularityCoeff;
	}
	public double[] getReflectionColor() {
		return reflectionColor;
	}
	public double getTransparencyCoeff() {
		return transparencyCoeff;
	}
	
	
}
