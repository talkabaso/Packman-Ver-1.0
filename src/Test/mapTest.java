package Test;

import java.io.IOException;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Coords.MyCoords;
import Geom.Point3D;
import Map.converts;
import Map.map;
import Map.pix;

class mapTest {
	
	pix p1;
	pix p2;
	MyCoords m;
	converts c;
	Point3D circle;
	double distCaravanCirc;
	double angleCircCaravan;
	Point3D caravan; //coordinate caravan
	pix caravanPix; //pix caravan
	map ourMap;
	double height;
	double width;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		
		caravanPix=new pix(923,406);
		p2=new pix(732,544); // middle of the circle
		 m=new MyCoords();
		c=new converts();
		ourMap=new map();
		height = ourMap.getHeight();
		width = ourMap.getWidth();
		circle=new Point3D(32.102477,  35.207464);
		caravan=new Point3D(32.103342, 35.208818);
		distCaravanCirc=m.distance3d(caravan, circle);//284.1
		angleCircCaravan=m.azimuth(circle,caravan);//195.69	
	}

	@AfterEach
	void tearDown() throws Exception {
	}

//	@Test
//	void test() {
//		fail("Not yet implemented");
//	}
	
	@Test
	public void distBet2Pix() throws IOException {
		
		double expected=distCaravanCirc;
		double distance=converts.distanceBet2Pixels(caravanPix, p2,height,width);
		System.out.println("the distance is "+distance);
		
		Assert.assertEquals(expected, distance,3);
		
	}

	@Test 
	public void angleBet2Pix() {
		
		
		double expected=angleCircCaravan;
		double angle=converts.angleBet2Pixels(p2, caravanPix,height,width);// result=195.79 the real is 195.79
		
		System.out.println(angle);
		Assert.assertEquals(expected, angle,1);
	}

	//this function coverts pixel to coords and then calculate the distance between the original coords to
	//the coords that we converted
	@Test
	public void pixel2Coords(){
		
		
		double expected=0;
		Point3D p1Coords=converts.pixel2Coords(caravanPix.x(), caravanPix.y(),height,width);
		
		double actualDist=m.distance3d(p1Coords, caravan);
		
		Assert.assertEquals(expected, actualDist,3);//check caravan
		
		Point3D p2Coords=converts.pixel2Coords(p2.x(), p2.y(),height,width);		
		
		double actualDist2=m.distance3d(p2Coords, circle);
				
		Assert.assertEquals(expected, actualDist2,5.5);// cheack p2
				
	}
	
	//this function coverts coords to pixel and then calculate the distance2D between the original pixels to
		//the pixels that we converted
	
	@Test
	public void coords2Pixel() {
		
		double expected=0;
		
		Point3D caravanPixel=new Point3D(caravanPix.x(),caravanPix.y()); //original
		Point3D p2pix=new Point3D(p2.x(),p2.y());// original

		
		Point3D p1Pixel=converts.coords2Pixel(caravan.x(),  caravan.y(),height,width); //new
		Point3D p2Pixel =converts.coords2Pixel(circle.x(),  circle.y(),height,width); //new
						
		double currentDist1=p1Pixel.distance2D(caravanPixel);
		
		Assert.assertEquals(expected, currentDist1,8);// check caravan
		
		double currentDist2=p2Pixel.distance2D(p2pix);
		
		Assert.assertEquals(expected, currentDist2,8);// check p2 
		
	}
	
}
