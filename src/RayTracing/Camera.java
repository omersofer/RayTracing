package RayTracing;

public class Camera {
	
	private Vector position;
	private Vector look_at_point;
	private Vector up_vector;
	
	private double screen_distance;
	private double screen_width;
	
	public Camera(Vector position, Vector look_at, Vector up_vector,
			double scrn_width, double scrn_distance)
	{
		this.position = position;
		this.look_at_point = look_at;
		this.up_vector = up_vector;
		this.screen_distance = scrn_distance;
		this.screen_width = scrn_width;
	}
	
	public static Camera parseCamera(String[] params) 
	{
		double px, py, pz, lx, ly, lz, ux, uy, uz, sc_d, sc_w;
		px = Double.parseDouble(params[0]);
		py = Double.parseDouble(params[1]);
		pz = Double.parseDouble(params[2]);
		lx = Double.parseDouble(params[3]);
		ly = Double.parseDouble(params[4]);
		lz = Double.parseDouble(params[5]);
		ux = Double.parseDouble(params[6]);
		uy = Double.parseDouble(params[7]);
		uz = Double.parseDouble(params[8]);
		sc_d = Double.parseDouble(params[9]);
		sc_w = Double.parseDouble(params[10]);
		
		Vector p, l, u;
		p = new Vector(px, py, pz);
		l = new Vector(lx, ly, lz);
		u = new Vector(ux, uy ,uz);
		return new Camera(p,l,u, sc_d, sc_w);
	}
	
	@Override
	public String toString() {
		return "Cam(pos=" + position + "lookAt=" + look_at_point +
				"upvect=" + up_vector + ")";
	}
}
