package Map;

import java.util.ArrayList;
import Geom.Point3D;

/**
 * this class contains an ArrayList of Points3D of the packman way
 * @author Tal and Aric
 *
 */

public class path {

	ArrayList<Point3D> path; // collection of Point3D
	
	// Constructor //
	public path() {
		
		path=new ArrayList<Point3D>();
	}
	
	public void add(Point3D p) {
		
		path.add(p);
	}
	
	// Getters and Setters //
	
	public int getSize() {
		
		return path.size();
	}
	
	public ArrayList<Point3D> getPath(){
		
		return this.path;
	}
	
	public Point3D getPathI(int i) {
		
		return path.get(i);
	}
	
	public String toString() {
		
		int i=0;
		String s="";
		while(i<path.size()) {
			s+=" "+i+")";
			s+=path.get(i).x()+", "+path.get(i).y()+", "+path.get(i).z()+", "+path.get(i).getTimeStamp()+"\n";
			i++;
		}
		return s;
	}
}