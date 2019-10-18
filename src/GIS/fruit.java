package GIS;

import Geom.Point3D;

/**
 * this class represent Fruit object each of fruit has : id,position and weight associated with him 
 * @author Tal and Aric
 */

public class fruit {

	private Point3D position;
	private metaDataFruit data;
	
	// Constructor //
	public fruit(metaDataFruit data,Point3D position) {
		
		this.data=data;
		this.position=position;
	}
	
	// Getters and Setters //
	
	public metaDataFruit getData() {
		
		return data;
	}
	
	public Point3D getPosition() {
		
		return position;
	}

	public String getId() {
		return data.getId();
	}

	public void setId(String id) {
		data.setId(id);
	}

	public double getX() {
		return position.get_x();
	}

	public void setX(double x) {
		position.set_x(x);
	}

	public double getY() {
		return position.get_y();
	}

	public void setY(double y) {
		position.set_y(y);
	}

	public double getZ() {
		return position.get_z();
	}

	public void setZ(double z) {
		position.set_z(z);
	}

	public double getWeight() {
		return data.getWeight();
	}

	public void setWeight(double weight) {
		data.setWeight(weight);
	}

	@Override
	public String toString() {
		return "fruit [position=" + position + ", data=" + data + "]";
	}
}