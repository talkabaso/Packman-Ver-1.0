package Coords;

import Geom.Point3D;

/**
 * This interface represents a basic coordinate system converter, including:
 * 1. The 3D vector between two lat,lon, alt points 
 * 2. Adding a 3D vector in meters to a global point.
 * 3. convert a 3D vector from meters to polar coordinates
 * @author Boaz Ben-Moshe
 *
 */
public interface coords_converter {
	/** computes a new point which is the gps point transformed by a 3D vector (in meters)*/
	public Point3D add(Point3D gps, Point3D local_vector_in_meter);//conver the meter vector to deg and then add?
	/** computes the 3D distance (in meters) between the two gps like points */
	public double distance3d(Point3D gps0, Point3D gps1);
	/** computes the 3D vector (in meters) between two gps like points */
	public Point3D vector3D(Point3D gps0, Point3D gps1);//convert both to meter and then calaculate?
	/** computes the polar representation of the 3D vector be gps0-->gps1 
	 * Note: this method should return an azimuth (aka yaw), elevation (pitch), and distance*/
	public double[] azimuth_elevation_dist(Point3D gps0, Point3D gps1);
	/*How do you calculate elevation?
			To calculate percent slope, divide the difference between the elevations
			of two points by the distance between them
			, then multiply the quotient by 100. The difference in elevation 
			between points is called the rise. The distance
			between the points is called the run. Thus, percent slope equals 
			(rise / run) x 100
	*/
	
	/**
	 * return true iff this point is a valid lat, lon , lat coordinate: [-180,+180],[-90,+90],[-450, +inf]
	 * @param p
	 * @return
	 */
	public boolean isValid_GPS_Point(Point3D p);
	
}
