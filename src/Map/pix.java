package Map;

/**
 * this class represent object of pixel Point (x,y)
 * @author Tal and Aric
 */

public class pix {

	private double x;
	private double y;
	
	// Constructor //
	public pix(double x,double y) {
		
		this.x=x;
		this.y=y;
	}
	
	// Getters and Setters //
	
	public double x() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public double y() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "pix [x=" + x + ", y=" + y + "]";
	}
}