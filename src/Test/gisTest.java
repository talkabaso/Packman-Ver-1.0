package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import GIS.metaDataPack;
import GIS.packman;
import Geom.Point3D;

class gisTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

//	@Test
//	void test() {
//		fail("Not yet implemented");
//	}
	
	@Test
	public void getPostionAndDataTest() {
		
		//check only the packman because in the fruit its the same
		metaDataPack meta=new metaDataPack("1", 8, 6);
		Point3D position =new Point3D(32.2,33.3,20);
		packman p=new packman(meta, position);
		
		//position Test
		Point3D expected=position;
		double xexpected=position.x();
		double yexpected=position.y();
		double zexpected=position.z();
		
		Point3D current =p.getPosition();
		double xcurrent=position.x();
		double ycurrent=position.y();
		double zcurrent=position.z();
		
		Assert.assertEquals(xexpected, xcurrent,0);
		Assert.assertEquals(yexpected, ycurrent,0);
		Assert.assertEquals(zexpected, zcurrent,0);
		
		//metaData Test
		
		String id=meta.getId();		
		double speed=meta.getSpeed();
		double radius =meta.getRadius();
		
		String idCurrent=p.getId();
		double speedCurrent=p.getSpeed();
		double radiusCurrent=p.getRadius();
		
		if (!id.equals(idCurrent)) {
			
			Assert.fail();
		}
		Assert.assertEquals(speed, speedCurrent,0);
		Assert.assertEquals(radius, radiusCurrent,0);
	}

}
