package Coords;

import Geom.Point3D;

public class MyCoords implements coords_converter {

	final double  earthRadius=6371000;


	/**
	 * This function calculate the new gps point transformed by a 3D vector (in meters)
	 * @param gps is point3D represent lat,lon,alt 
	 * @param local_vector_in_meter represent the meter  
	 */
	@Override
	public Point3D add(Point3D gps, Point3D local_vector_in_meter) {
		double lonNorm = Math.cos(Math.toRadians(gps.x()));
		double diffRadX = local_vector_in_meter.x()/earthRadius; // dif for lat
		double latDiff = Math.toDegrees(diffRadX);

		double diffRadY = local_vector_in_meter.y()/(earthRadius*lonNorm); // dif for long
		double longDiff = Math.toDegrees(diffRadY);

		return new Point3D(gps.x() + latDiff,gps.y() + longDiff,gps.z()+local_vector_in_meter.z());
	}

	/**
	 * This function caclculate the distance between 2 locations
	 * @param gps0 is point represent lat,lon,alt
	 * @param gps1 is point represent lat,lon,alt
	 */
	@Override
	public double distance3d(Point3D gps0, Point3D gps1) {

		double maxDist=100000;

		double diffLat=gps1.x()-gps0.x();
		double diffXRad=(diffLat*Math.PI)/180;	
		double toMeterDiffXRad=Math.sin(diffXRad)*6371000;

		double diffLon=gps1.y()-gps0.y();
		double diffyRad=(diffLon*Math.PI)/180;
		double toMeterDiffyRad= Math.sin(diffyRad)*Math.cos(gps0.x()*Math.PI/180)*earthRadius;

		double x =toMeterDiffXRad*toMeterDiffXRad;
		double y=toMeterDiffyRad*toMeterDiffyRad;

		double dist=Math.sqrt(x+y);
		if (dist>=maxDist) {

			throw new RuntimeException("Error: the distance is over than: "+maxDist+" km");
		}
		return dist;
	}
	
	@Override
	public Point3D vector3D(Point3D gps0, Point3D gps1) {

		double diffLat=gps1.x()-gps0.x();
		double diffXRad=(diffLat*Math.PI)/180;	
		double toMeterDiffXRad=Math.sin(diffXRad)*earthRadius;


		double diffLon=gps1.y()-gps0.y();
		double diffyRad=(diffLon*Math.PI)/180;
		double toMeterDiffyRad= Math.sin(diffyRad)*Math.cos(gps0.x()*Math.PI/180)*earthRadius;

		double diffZ=gps1.z()-gps0.z();

		Point3D vector=new Point3D(toMeterDiffXRad,toMeterDiffyRad,diffZ);
		return vector;
	}

	@Override
	public double[] azimuth_elevation_dist(Point3D gps0, Point3D gps1) {

		double	azimuth_elevation_dist [] =new double[3];
		azimuth_elevation_dist[0]=azimuth(gps0, gps1); // Azimuth
		azimuth_elevation_dist[1]=elevation(gps0 ,gps1); // Pitch (elevation)
		azimuth_elevation_dist[2]=distance3d(gps0, gps1); // Distance

		return azimuth_elevation_dist;
	}

	@Override
	public boolean isValid_GPS_Point(Point3D p) {
		return ((p.x()>=-90 && p.x()<=90) && (p.y()>=-180 && p.y()<=180) && p.z()>=-450);
	}

	/**
	 * This function calculate the elevation between 2 points
	 * @param p0 is point3D represent lat,lon,alt  
	 * @param p1 is point3D represent lat,lon,alt 
	 * @return the elevation
	 */
	public double elevation(Point3D p0,Point3D p1) {

		double dist=distance3d(p0,p1);
		double elevation= Math.toDegrees(Math.asin((p1.z()-p0.z())/dist));

		return elevation;
	}

	/**
	 * This function calculate the azimuth between 2 points
	 * @param p0 is point3D represent lat,lon,alt  
	 * @param p1 is point3D represent lat,lon,alt 
	 * @return the azimuth
	 */
	public double azimuth(Point3D gps0, Point3D gps1) {
		double latRadGps0 = Math.toRadians(gps0.x());
		double lonRadGps0 = Math.toRadians(gps0.y());

		double latRadGps1 = Math.toRadians(gps1.x());
		double lonRadGps1 = Math.toRadians(gps1.y());

		double y = Math.sin(lonRadGps1 - lonRadGps0) * Math.cos(latRadGps1);

		double x = Math.cos(latRadGps0) * Math.sin(latRadGps1)
				- Math.sin(latRadGps0) * Math.cos(latRadGps1) * Math.cos(lonRadGps1 - lonRadGps0);

		double azimuth = Math.atan2(y, x);
		double azimuthInDegree = Math.toDegrees(azimuth);
		if(azimuthInDegree<0) {
			azimuthInDegree=azimuthInDegree+360;
		}
		return azimuthInDegree;
	}
}
