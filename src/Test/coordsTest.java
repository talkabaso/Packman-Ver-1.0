package Test;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Coords.MyCoords;
import Geom.Point3D;

class coordsTest {

	private Point3D p0;
	private Point3D p1;
	
	private Point3D p4;
	private Point3D p5;
	
	private MyCoords m ;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		
		 p0=new Point3D(32.103315,35.209039,670);
	      p1=new Point3D(32.106352,35.205225,650);
	
	     p4=new Point3D(32.10490203,35.21044203,673);
		 p5=new Point3D(32.10190203,35.21044203,680);
	     
		 m = new MyCoords();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

//	@Test
//	void test() {
//		fail("Not yet implemented");
//	}
	
	@Test
	public void distanceTest() {
		
		//first check
		double expcted=493.0523;
		
		double current=m.distance3d(p0, p1);
		
		Assert.assertEquals(expcted, current,0.01);
	
		//second check
		
		double expcted2=333.5848;
		
		double current2=m.distance3d(p4, p5);
		
		Assert.assertEquals(expcted2, current2,0.01);


		//third check should throw exception
		//here we check the distance between two far points the function distance should throw exception 
		boolean tooFar=false;
		Point3D farPoint=new Point3D(37.2765467,32.55678732,190);
		
		try {
			
			m.distance3d(p0, farPoint);
			
		}catch(Exception e){
			
			tooFar=true;
		}
		
		Assert.assertTrue(tooFar);
		
	}
	@Test
	public void isValidTest() {
		
		Point3D p3=new Point3D(100,20,900);//invalid
		Point3D p4=new Point3D(80,200,876);//invalid
		
		//the expected result of the function isValid of each point
		boolean expected1=true;
		boolean expected2=false;
		boolean expected3=false;
	
		//check if the points are valid
		boolean current1=m.isValid_GPS_Point(p0);
		boolean current2=m.isValid_GPS_Point(p3);
		boolean current3=m.isValid_GPS_Point(p4);
		
		Assert.assertTrue(expected1==current1 && expected2==current2 && expected3==current3 );
		
	}

}
