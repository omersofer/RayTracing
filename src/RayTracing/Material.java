package RayTracing;

public class Material 
{
	private static int static_matIndex = 1;
	
	public final static int R = 0;
	public final static int G = 1;
	public final static int B = 2;
	
	private int matInx;
	private Color diffuseColor;
	private Color specularColor;
	private double specularityCoeff;
	private Color reflectionColor;
	private double transparencyCoeff;
	
	
	public Material(int matIndex, double diff_r, double diff_g, double diff_b,
			double spec_r, double spec_g, double spec_b,
			double spec_coeff,
			double refl_r, double refl_g, double refl_b,
			double trans_coeff)
	{
		matInx = matIndex;
		diffuseColor = new Color(diff_r, diff_g, diff_b);
		specularColor = new Color(spec_r, spec_g, spec_b);
		specularityCoeff = spec_coeff;
		reflectionColor = new Color(refl_r, refl_g,refl_b);
		transparencyCoeff = trans_coeff;
	}
	
	public Material(Material m)
	{
		this(m.matInx,
				m.diffuseColor.getR(),
				m.diffuseColor.getG(),
				m.diffuseColor.getB(),
				m.specularColor.getR(),
				m.specularColor.getG(),
				m.specularColor.getB(),
				m.specularityCoeff,
				m.reflectionColor.getR(),
				m.reflectionColor.getG(),
				m.reflectionColor.getB(),
				m.transparencyCoeff);
	}
	
	public Color getDiffuseColor() {
		return new Color(diffuseColor);
	}
	
	public Color getSpecularColor() {
		return new Color(specularColor);
	}
	
	public double getSpecularityCoeff() {
		return specularityCoeff;
	}
	
	public Color getReflectionColor() {
		return new Color(reflectionColor);
	}
	
	public double getTransparencyCoeff() {
		return transparencyCoeff;
	}
	
	public int getMaterialIndex()
	{
		return matInx;
	}

	public static Material parseMaterial(String[] params) 
	{
		double diff_r = Double.parseDouble(params[0]);
		double diff_g = Double.parseDouble(params[1]);
		double diff_b = Double.parseDouble(params[2]);
		
		double spec_r = Double.parseDouble(params[3]);
		double spec_g = Double.parseDouble(params[4]);
		double spec_b = Double.parseDouble(params[5]);
		
		double refl_r = Double.parseDouble(params[6]);
		double refl_g = Double.parseDouble(params[7]);
		double refl_b = Double.parseDouble(params[8]);
		
		double spec_coeff = Double.parseDouble(params[9]);
		double trans_coeff = Double.parseDouble(params[10]);
		
		return new Material(static_matIndex++, diff_r, diff_g, diff_b,
				spec_r, spec_g, spec_b,
				spec_coeff,
				refl_r, refl_g, refl_b,
				trans_coeff);
	}
	
}
