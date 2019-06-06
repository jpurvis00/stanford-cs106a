/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

import acm.util.*;
import java.util.*;
import java.io.*;
import java.util.HashMap;

public class NameSurferDataBase implements NameSurferConstants {
	
	private HashMap<String, NameSurferEntry> namesDB = new HashMap<String, NameSurferEntry>();
	
/* Constructor: NameSurferDataBase(filename) */
/**
 * Creates a new NameSurferDataBase and initializes it using the
 * data in the specified file.  The constructor throws an error
 * exception if the requested file does not exist or if an error
 * occurs as the file is being read.
 */
	public NameSurferDataBase(String filename) {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(filename));
			String line = "";
			
			while(true) {
				line = rd.readLine();
				if(line == null) break;
				
				//Create a new NameSurferEntry object for the line being read
				NameSurferEntry nameEntry = new NameSurferEntry(line);
				//Store the name and object in a hashmap.
				namesDB.put(nameEntry.getName(), nameEntry);
			}
			rd.close();
		}
		catch(IOException ex) {
			throw new ErrorException(ex);
		}
	}
	
/* Method: findEntry(name) */
/**
 * Returns the NameSurferEntry associated with this name, if one
 * exists.  If the name does not appear in the database, this
 * method returns null.
 */
	public NameSurferEntry findEntry(String name) {
		//Searches the hashmap for the name and returns the hashmap key/value
		if(namesDB.containsKey(name)) {
			return namesDB.get(name);
		} else return null;
	}
}

