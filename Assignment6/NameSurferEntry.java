/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

import acm.util.*;
import java.util.*;

public class NameSurferEntry implements NameSurferConstants {

/* Constructor: NameSurferEntry(line) */
/**
 * Creates a new NameSurferEntry from a data line as it appears
 * in the data file.  Each line begins with the name, which is
 * followed by integers giving the rank of that name for each
 * decade.
 */
	public NameSurferEntry(String line) {
		//find index of first space
		int nameInd = line.indexOf(" ");
		//Assign the substring starting at pos 0 to the first space to Name
		Name = line.substring(0, nameInd);
		
		//Create a new string tokenizer with all values after the name value using substring
		StringTokenizer str1 = new StringTokenizer(line.substring(nameInd + 1));
		
		//While there are still values/tokens left in the substring, turn them into ints and
		//store them in an array
		int i = 0;
		while(str1.hasMoreTokens()) {
			rankings[i] = Integer.parseInt(str1.nextToken());
			i++;
		}
	}

/* Method: getName() */
/**
 * Returns the name associated with this entry.
 */
	public String getName() {
		return Name;
	}

/* Method: getRank(decade) */
/**
 * Returns the rank associated with an entry for a particular
 * decade.  The decade value is an integer indicating how many
 * decades have passed since the first year in the database,
 * which is given by the constant START_DECADE.  If a name does
 * not appear in a decade, the rank value is 0.
 */
	public int getRank(int decade) {
		return rankings[decade];
	}

/* Method: toString() */
/**
 * Returns a string that makes it easy to see the value of a
 * NameSurferEntry.
 */
	public String toString() {
		String objStr = "";
		objStr = Name + " [";
		
		for(int i = 0; i < rankings.length; i++) {
			objStr += getRank(i);
			
			//adds a space after every value except the last one
			if(i != rankings.length - 1) objStr += " ";
		}
		
		objStr += "]";
		
		return objStr;
	}
	
	/** Private instance vars */
	private String Name;
	private int[] rankings = new int[NDECADES];
}

