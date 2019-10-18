package File_format;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class reads from csv file and save each line in arrayList
 * @author Aric and Tal 
 */

public class readCsv {

	private final BufferedReader reader;

	// Constructor - receive csv File path //
	public readCsv(String input) throws IOException {
		reader = new BufferedReader(new FileReader(input));
	}
	
	/**
	 * this class scan the csv File that received in constructor and read all lines and insert
	 * data to ArrayList of Strings
	 * @return ArrayList that contains all csv elements
	 * @throws IOException
	 */
	public ArrayList<String> readCsvGame() throws IOException {

		ArrayList<String> strings=new ArrayList<String>();
		try { 

			reader.readLine(); // skip 1st line
			String str = reader.readLine();

			while (str != null) {

				strings.add(str);
				str = reader.readLine();
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		} 
		finally {
			if (reader != null) {
				try {
					reader.close();
				}
				catch(Exception e) {

					throw new RuntimeException(e);
				}
			} 
		}
		
		return strings; // return the arrayList of all the lines in the csv file as strings
	}
}