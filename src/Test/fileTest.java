package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import File_format.readCsv;
import GIS.fruit;
import GIS.game;
import GIS.packman;

class fileTest {

	String pathName="C:\\Users\\Talka\\Documents\\GitHub\\Packman-Ver-1.0\\testFile.csv"; //the location of the file
	ArrayList<fruit> fruits=new ArrayList<fruit>();
	ArrayList<packman> packmans=new ArrayList<packman>();

	

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
	public void readFromCsvTest() throws IOException {

		readCsv csv =new readCsv(pathName);
		ArrayList<String> csvLines=csv.readCsvGame();// each line is element(string)

		int expecteds =15;
		int actuals=csvLines.size();
		Assert.assertEquals(expecteds, actuals);

		//check the content of the file 

		String s="";
		for(int i=0;i<csvLines.size();i++) {

			s=csvLines.get(i);
			if (i>=0 && i<=2 && s.charAt(0)!='P') { //the 3 first element should be packmans

				Assert.fail();
				
			}
			
			if (i>2 && s.charAt(0)!='F') { // all the rest should be fruits

				Assert.fail();

			}
		}

	}
	
	// create game object then stores all the data from the csv file in fruits and packmans arrays
	//i expect the size of fruits and packmans array to be like the number of fruits and packmans in 
	//the csv file		
	@Test
	public void gameCreation() throws IOException {
		
		game g=new game(fruits,packmans);
		game.createGameCollection(pathName); 
		
		int expectedsFruit=12;
		int expectedsPack=3;
		
		Assert.assertEquals(expectedsFruit, fruits.size());
		Assert.assertEquals(expectedsPack, packmans.size());
		
		//check if the speed of each packman is 1 like the file
		int expectedSpeed=1;
		for(int i=0;i<packmans.size();i++) {

			if (packmans.get(i).getSpeed()!=expectedSpeed) {
				
				Assert.fail();
			}
		}
		
	}
	

}

