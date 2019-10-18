package GIS;

import java.util.ArrayList;
import Map.path;

/**
 * this class contains all the packmans solution - the ways of all packmans to fruits
 * @author Tal and Aric
 */

public class solution {
	
	private ArrayList<path> pathCollection;
	
	// Constructor //
	public solution() {
		
		pathCollection=new ArrayList<path>();
	}
	
	public void add(path p) {
		
		pathCollection.add(p);
	}
	
	// Getters and Setters //
	
	public int getSize() {
		
		return pathCollection.size();
	}

	public ArrayList<path> getPathCollection(){
		
		return pathCollection;
	}

	@Override
	public String toString() {
		return "solution [pathCollection=" + pathCollection + "]";
	}
}