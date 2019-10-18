package File_format;

import java.io.IOException;
import java.util.ArrayList;
import GIS.fruit;
import GIS.packman;
import Map.converts;

/**
 * This class create String that will contains ArrayLists (Fruits and Packmans) data
 * @author Aric and Tal 
 */

public class writeCsv {
	
	/**
	 * This function receive 2 ArrayLists  
	 * @param ff ArrayList of fruits
	 * @param pp ArrayList of packmans
	 * @return String that contains all csv Data 
	 * @throws IOException
	 */
	public static String Write(ArrayList<fruit> ff,ArrayList<packman> pp,double h,double w) throws IOException {
		
	   converts c = new converts();

	   ArrayList<packman> pCoords=converts.pixels2CoordsPack(pp,h,w);
       ArrayList<fruit> fCoords=converts.pixels2CoordsFruit(ff,h,w);
		
		String str = "Type,id,Lat,Lon,Alt,Speed/Weight,Radius,"+pCoords.size()+","+fCoords.size()+"\n";
		for(int i=0;i<pCoords.size();i++) {
			
			String id=pCoords.get(i).getId();
			double packX=pCoords.get(i).getX();
			double packY=pCoords.get(i).getY();
			double packZ=pCoords.get(i).getZ();
			double speed=pCoords.get(i).getSpeed();
			double radius=pCoords.get(i).getRadius();
			
			str+="P,"+id+","+packX+","+packY+","+packZ+","+speed+","+radius+","+"\n";
		}

		for(int i=0;i<fCoords.size()-1;i++) {
			
			String id=fCoords.get(i).getId();
			double fruitX=fCoords.get(i).getX();
			double fruitY=fCoords.get(i).getY();
			double fruitZ=fCoords.get(i).getZ();
			double weight=fCoords.get(i).getWeight();

			str+="F,"+id+","+fruitX+","+fruitY+","+fruitZ+","+weight+","+"\n";
		}
		
		//For the last element in the Csv file
		str+="F,"+fCoords.get(fCoords.size()-1).getId()+","+fCoords.get(fCoords.size()-1).getX()+","+
		fCoords.get(fCoords.size()-1).getY()+","+
				fCoords.get(fCoords.size()-1).getZ()+","+fCoords.get(fCoords.size()-1).getWeight();		
		
		return str;
	}
}