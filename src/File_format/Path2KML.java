package File_format;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import Coords.MyCoords;
import GIS.fruit;
import GIS.game;
import GIS.packman;
import GIS.solution;
import Geom.Point3D;
import algorithm.algo;

/**
 * this class create Kml from ArrayLists of fruits and packmans
 * @author Tal and Aric
 *
 */
public class Path2KML {

	private static long startTimer;
	private static int delayPath=1; // each first path of packman receive little delay in timeStamp
	private final BufferedWriter writer;
	String timeInString; // the time of Saving the KML

	public Path2KML(String output) throws IOException {

		writer = new BufferedWriter(new FileWriter(output));
		startTimer=System.currentTimeMillis();
		timeInString = timestampToDate(startTimer);
	}

	/**
	 * 
	 * @param str to know if its Packman / Fruit / Packman's path
	 * @param id number of Packman fruit in csv file
	 * @param speed the speed of Packman
	 * @param radius the radius of Packman
	 * @param time the time that fruit is eaten and path created
	 * @param weight the weight of fruit
	 * @param latitude position in X-axis
	 * @param longitude position in X-axis
	 * @param alt
	 * @throws IOException
	 */
	public void writeIcon(String str,String id,double speed,double radius,
			String time,double weight,double latitude,
			double longitude,double alt) throws IOException {

		if(str=="P") { // This is Packman

			writer.write("<Placemark>\n" + 
					"<name><![CDATA["+"Packman" +id+"]]></name>\n" + "<TimeSpan><begin>"+timeInString+
					"</begin>"+"<end>"+"</end></TimeSpan>"+
					"<description><![CDATA[Speed: <b>"+speed+"</b><br/>Radius: <b>"
					+radius+"</b><br/>Date: <b>"
					+time+"</b>]]></description><styleUrl>"+"pack"+"</styleUrl>\n" + 
					"<Point>\n" + 
					"<coordinates>"+latitude+","+longitude+","+alt+"</coordinates></Point>\n" + 
					"</Placemark>\n" + 
					"");
		}

		else if(str=="F") { // This is Fruit

			// for creating date format needed for KML
			time = time.replace(" ", "T");  
			time+="Z";

			writer.write("<Placemark>\n" + "<TimeSpan><begin>"+timeInString+"</begin>"+"<end>"+time+"</end></TimeSpan>"+
					"<name><![CDATA["+"Fruit "+id+"]]></name>\n" +
					"<description><![CDATA[Weight: <b>"+weight+"</b><br/>]]></description><styleUrl>"
					+"fruit"+"</styleUrl>\n" + 
					"<Point>\n" + 
					"<coordinates>"+latitude+","+longitude+","+alt+"</coordinates></Point>\n" + 
					"</Placemark>\n" + 
					"");
		}
		else { // This is packman's path

			// for creating date format needed for KML
			time = time.replace(" ", "T");
			time+="Z";

			writer.write("<Placemark>\n" + 
					"<name><![CDATA["+"Path" +id+"]]></name>\n" +"<TimeSpan><begin>"+time+""
					+ "</begin>"+"<end>"+"</end></TimeSpan>"+
					"<description><![CDATA[Speed: <b>"+speed+"</b><br/>Radius: <b>"
					+radius+"</b><br/>Date: <b>" 
					+time+"</b>]]></description><styleUrl>"+"pack"+"</styleUrl>\n" + 
					"<Point>\n" + 
					"<coordinates>"+latitude+","+longitude+","+alt+"</coordinates></Point>\n" + 
					"</Placemark>\n" + 
					"");
		}
	}

	/**
	 * add the start of document with 2 pictures of Packman and Fruit
	 * @throws IOException
	 */
	private void writeStart() throws IOException { 

		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
		writer.write("<Document>\n");
		writer.write("<Style id=\"fruit\"><IconStyle><Icon><href>"
				+ "https://imagizer.imageshack.com/img922/2829/excmjT.png</href></Icon></IconStyle></Style>\r\n" + 
				"\r\n" + 
				"<Style id=\"pack\"><IconStyle><Icon><href>"
				+ "https://imagizer.imageshack.com/img922/8062/gAGiT9.png</href></Icon></IconStyle></Style>\r\n");
	}

	/**
	 * the end of document file
	 * @throws IOException
	 */
	private void writeEnd() throws IOException {
		writer.write("</Document>\n</kml>\n");
	}

	/**
	 * this function converts from long (TimeStamp) to the time in String
	 * @param timeStamp 
	 * @return String of date
	 */
	public static String timestampToDate(long timeStamp) { // change from timeStamp to String (Date)

		String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date (timeStamp));
		return date;
	}

	/**
	 * This function creating KML file
	 * @param ff fruit ArrayList
	 * @param pp packman ArrayList
	 * @param s Solution that contains all the paths of the packmans
	 * @param save the name we choose for the file
	 * @param pEaten ArrayList of packmans with list of eatenFruits for all packmans
	 * @throws IOException
	 */
	public static void run(ArrayList<fruit> ff,ArrayList<packman> pp,solution s,String save,ArrayList<packman> pEaten)
			throws IOException {

		MyCoords m = new MyCoords();

		if (pp.size()==0 && ff.size()==0) { // Exception for non elements

			throw new RuntimeException("the game is empty please enter elements or open file");
		}

		Path2KML k =new Path2KML(save);
		k.writeStart();

		for(int i=0; i<pp.size(); i++) {

			String id = pp.get(i).getId();
			double speed = pp.get(i).getSpeed();
			double radius=pp.get(i).getRadius();
			double lat = pp.get(i).getPosition().x();
			double lon = pp.get(i).getPosition().y();
			double alt = pp.get(i).getPosition().z();
			// Start point for calculate the distance between next Fruit in eatenFruit list
			Point3D start = pp.get(i).getPosition();
			// for set correct timeStamp for all path points
			long currentTime = startTimer; 

			for(int j=0; j<pEaten.get(i).getEatenFruits().size();j++) { // loop of all path of packmans

				fruit f = pEaten.get(i).getEatenFruits().get(j);
				 // calculate the distance then minus radius from the sum
				double dist = m.distance3d(start, f.getPosition())-pEaten.get(i).getRadius();
				// divide the distance in speed of Packman for know how many steps took to eat
				long timeStamp = (long) (dist/pEaten.get(i).getSpeed()); 
				// multiply in 1000 for convert it to Seconds
				currentTime+=timeStamp*1000; 
				//set to the fruit point the timeStamp is eaten
				pEaten.get(i).getEatenFruits().get(j).getPosition().setTimeStamp(currentTime); 

				//Data on fruits
				id = pEaten.get(i).getEatenFruits().get(j).getId();
				double weight = pEaten.get(i).getEatenFruits().get(j).getWeight();
				double fruitLat = pEaten.get(i).getEatenFruits().get(j).getX();
				double fruitLon = pEaten.get(i).getEatenFruits().get(j).getY();
				double fruitAlt = pEaten.get(i).getEatenFruits().get(j).getZ();
				String time = timestampToDate(pEaten.get(i).getEatenFruits().get(j).getPosition().getTimeStamp());

				k.writeIcon("F",id,0,0,time,weight,fruitLon,fruitLat,fruitAlt);

				start = f.getPosition(); // update the position of start Point to next fruit
			}

			k.writeIcon("P",id,speed,radius,"0",0,lon,lat,alt);
		}

		for(int i=0; i<s.getPathCollection().size();i++) { // Loop on paths

			int t=delayPath;

			for(int j=1; j<s.getPathCollection().get(i).getPath().size();j++) {
				// setTimeStamp for showing
				s.getPathCollection().get(i).getPath().get(j).setTimeStamp(getStartTimer()+(t++)*1000); 
				
				double lat=s.getPathCollection().get(i).getPathI(j).x();
				double lon=s.getPathCollection().get(i).getPathI(j).y();
				double alt=s.getPathCollection().get(i).getPathI(j).z();

				String time = timestampToDate(s.getPathCollection().get(i).getPathI(j).getTimeStamp());
				k.writeIcon("N",""+pp.get(i).getId(),0,0,time,0,lon,lat,alt);
			}
			delayPath++;
		}
		//Close the writing to file
		k.writeEnd();
		k.writer.flush();
	}

	// Getters and Setters //

	public static long getStartTimer() {
		return startTimer;
	}

	public static void setStartTimer(long startTimer) {
		Path2KML.startTimer = startTimer;
	}


	public static void main(String[] args) throws IOException { // optional to create kml file from this class

		ArrayList<packman> pp=new ArrayList<packman>();
		ArrayList<fruit> ff=new ArrayList<fruit>();

		String name = "enter the file name here"+".kml"; // the name of file we create
		Path2KML k =new Path2KML(name);
		game g=new game(ff,pp);
		String pathFile = "C:\\Users\\Tal\\Desktop\\tal.csv"; // path to csv file
		game.createGameCollection(pathFile); 
		ArrayList<fruit> copyFruit=game.copyFruit(); // copy because the fruits delete in calcAll function
		ArrayList<packman> copyPack=game.copyPack(); // copy because the fruits delete in calcAll function

		solution s = algo.calcAll(ff, pp); // for create the paths for all packmans

		Path2KML.run(copyFruit,copyPack,s,name,pp);
	}
}
