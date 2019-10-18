package algorithm;

import java.util.ArrayList;
import Coords.MyCoords;
import GIS.fruit;
import GIS.game;
import GIS.metaDataFruit;
import GIS.metaDataPack;
import GIS.packman;
import GIS.solution;
import Geom.Point3D;

public abstract class algo {

	/**
	 * this function add this fruit to the eatenFruit array of the packman.
	 * Progress to the fruit each second by move in radius 
	 * add the weight of the fruit to his score and the time it takes to his total time and to
	 * the total time of the game
	 * @param f fruit
	 * @param p packman
	 */
	public static void packEatFruit(fruit f,packman p) {
		
		MyCoords m =new MyCoords();
		System.out.println("packman " +p.getId() +" eats fruit "+ f.getId());
		p.getEatenFruits().add(f);
		// CHECK THE TIME IT WILL TAKE TO EAT TO FRUIT AFTER CHANGING THE POSTION
		// OF FRUIT BY RADIUS=newFruitPosition
		p.setPoints(p.getPoints()+f.getWeight());//update the points of the packman
		Point3D tempPointFruit=changeCoordsAcordnigToRadius(f.getPosition(), p.getPosition(), p.getRadius()); 
		metaDataFruit data=new metaDataFruit(f.getId(),f.getWeight());
		fruit newFruitPosition =new fruit(data,tempPointFruit);

		double timeToEat=getTime(p,newFruitPosition);

		game.setTotalTime(game.getTotalTime()+timeToEat);

		p.setTime(p.getTime()+getTime(p,newFruitPosition));// add to packman's time agg the time to eat this fruit
		while(m.distance3d(f.getPosition(), p.getPosition())>p.getRadius()) {// send the original location  fruit
			proceedToFruit(p,f);
		}
		System.out.println("total time: "+game.getTotalTime());
		game.setScore(game.getScore()+f.getWeight());

	}

	/**
	 * Progress to the fruit by move in radius, set the packman 
	 * position according to his current position after each step
	 *  and add this position to his path
	 * @param p packman
	 * @param f fruit
	 */
	public static void proceedToFruit(packman p,fruit f) {

		Point3D temp=changeCoordsAcordnigToRadius(p.getPosition(),f.getPosition(),p.getRadius());
		p.setPosition(temp);
		p.addToTrack(temp); // add to track the current position afeter procceding to fruit
	}

	/**
	 * This function calculate the time it will take to eat the fruit Given the speed and radius
	 * @param p packman
	 * @param f  fruit that closer in raduis to packman
	 * @return time
	 */
	private static double getTime(packman p,fruit f) {

		MyCoords m= new MyCoords(); 
		Point3D packmanPoint=p.getPosition();
		Point3D friutPoint=f.getPosition();

		double speedPack=p.getSpeed();
		double dist=m.distance3d(packmanPoint,friutPoint);
		if (dist<p.getRadius()) {

			return 0;
		}
		return dist/speedPack;
	}

	/**
	 * This function get fruits and packmans and calculate the paths of each packman to the
	 * fruit he should eat, Given data such as radius speed and time
	 * @param fruits ArrayList of fruits objects
	 * @param packsArrayList of packmans objects
	 * @return solution that contain all the paths in the game
	 */
	public static solution calcAll(ArrayList<fruit> fruits,ArrayList<packman> packs) {

		solution answer=new solution();
		
		int size=fruits.size();

		int k=0;
		while(k<size) { //pass all the fruits

		
			double min=Double.MAX_VALUE;
			int index=-1;
			 int packSize=packs.size();
			 int minToEat=0;
			for(int i=0;i<packSize;i++) { // pass all the packmans and check which packman should eat
				
				packman current = packs.get(i);
				int toEat=eatFruit(fruits,current); // return the index of fruit the packman should eat
				
				Point3D PackmanPosition=current.getPosition();
				Point3D fruitPosition=fruits.get(toEat).getPosition();
				double radius=current.getRadius();
				double speed=current.getSpeed();
				String id=current.getId();
				Point3D tempPointPackman=changeCoordsAcordnigToRadius(PackmanPosition,fruitPosition ,radius); 	
				metaDataPack data= new metaDataPack(id,speed ,radius);

				packman newPackPosition =new packman(data,tempPointPackman);// the current packman after radius change
	
				double timeToEat=getTime(newPackPosition, fruits.get(toEat));
				if (timeToEat<min) {
					
					min=timeToEat;
					index=i;// the packman that will eat fastest
					minToEat=toEat;
				}
				
			}
			System.out.println("packman: "+packs.get(index).getId()+" win");
			packman current = packs.get(index); // the packman that should eat the fruit stores in "toEat"
			fruit temp=new fruit(fruits.get(minToEat).getData(), fruits.get(minToEat).getPosition());
			fruits.remove(minToEat);
			packEatFruit(temp,current); //make the packman eat this fruit
			
			k++;
		}

		// add the paths of the packmans to the solution
		for(int j=0;j<packs.size();j++) {

			answer.getPathCollection().add(packs.get(j).getPath());
		}
		
		System.out.println(answer);

		return answer;
	}

	/**
	 * This function get one packman and collection of fruits and return the fruit
	 * who should be eaten the packman.
	 * @param f
	 * @param p
	 * @return
	 */
	public static int eatFruit(ArrayList<fruit> f,packman p) {

		double min=Double.MAX_VALUE;
		int current=-1;
		for(int i=0;i<f.size();i++) {

			Point3D PackmanPosition=p.getPosition();
			Point3D fruitPosition=f.get(i).getPosition();
			double radius=p.getRadius();
			double speed=p.getSpeed();
			String id=p.getId();
			
			Point3D tempPointPackman=changeCoordsAcordnigToRadius(PackmanPosition,fruitPosition,radius); 	
			metaDataPack data= new metaDataPack(id,speed,radius);

			packman newPackPosition =new packman(data,tempPointPackman);

			fruit temp=f.get(i);
			if (getTime(newPackPosition,temp)<min) {

				min=getTime(newPackPosition,temp);
				current=i;
			}

		}
		return current; // the index of the fruit the packman should eat

	}


/**
 * This function return the postion of p1 by moving in radius to p2
 * @param p1 start postion 
 * @param p2 end position
 * @param radius radius in meter to progress 
 * @return
 */
	public static Point3D changeCoordsAcordnigToRadius(Point3D p1,Point3D p2,double radius) {

		MyCoords m=new MyCoords();

		double distance=m.distance3d(p1,p2);
		if (m.distance3d(p1, p2)!=0) {

			double ratio=radius/distance;
			double lat = p1.x() + ((p2.x() - p1.x()) * ratio);
			double lon= p1.y() + ((p2.y() - p1.y()) * ratio);

			return new Point3D(lat,lon,p1.z());

		}
		return p1; // or p2 they equals
	}

}
	


