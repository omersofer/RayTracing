package RayTracing;

public class Material 
{
	public final static int R = 0;
	public final static int G = 1;
	public final static int B = 2;
	
	private byte[] diffuseColor;
	private byte[] specularColor;
	private double specularityCoeff;
	private byte[] reflectionColor;
	private double transparencyCoeff;
	
	
	public Material(byte diff_r, byte diff_g, byte diff_b,
			byte spec_r, byte spec_g, byte spec_b,
			double spec_coeff,
			byte refl_r, byte refl_g, byte refl_b,
			double trans_coeff)
	{
		diffuseColor = new byte[3];
		diffuseColor[R] = diff_r;
		diffuseColor[G] = diff_g;
		diffuseColor[B] = diff_b;
		
		specularColor = new byte[3];
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
	
	public byte[] getDiffuseColor() {
		byte[] copy = new byte[3];
		System.arraycopy(diffuseColor, 0, copy, 0, diffuseColor.length);
		return copy;
	}
	public byte[] getSpecularColor() {
		return specularColor;
	}
	public double getSpecularityCoeff() {
		return specularityCoeff;
	}
	public byte[] getReflectionColor() {
		return reflectionColor;
	}
	public double getTransparencyCoeff() {
		return transparencyCoeff;
	}
	
	
}
