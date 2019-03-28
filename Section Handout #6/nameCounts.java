/* Write a program that asks the user for a list of names (one per line) until 
 * the user enters a blank line (i.e., just hits return when asked for a name).  
 * At that point the program should print out how many times each name in the list 
 * was entered.  You may find that using a HashMapto  keep  track  of  the information 
 * entered  by  user  may  greatly simplify this problem.  
 */

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class nameCounts {
	public static void main(String[] args) {
		promptName();
		printHashMap();
	}
	
	/* Prompts the user for names.  We break out of this loop if the user hits enter
	 * and does not supply a name.  It then passes that name to the storeName method 
	 * to put it in a hashMap.
	 * Pre:  No pre condition
	 * Post: Names have been entered and sent to the storeName method
	 */
	private static void promptName() {
		while(true) {
			System.out.print("Enter name: ");
			Scanner input = new Scanner(System.in);
			String name = input.nextLine();
			if(name.equals("")) break;
			storeName(name);
		}
	}
	
	/* Stores a name and value in a HashMap.  If the name does not exist, the value stored
	 * will be a 1.  If the name does exists, get the value and increment it by 1.
	 * Pre:  1 name at a time is passed into this method
	 * Post: Name and value have been put into or updated in the HashMap
	 */
	private static void storeName(String name) {
		//Name is already in the HashMap, increment the value by 1
		if(namesMap.containsKey(name))
			namesMap.put(name, namesMap.get(name) + 1);
		//Name is not in the HashMap, add it and set the value to 1
		else
			namesMap.put(name, 1);
		
	}
	
	/* Prints all values from the HashMap.
	 * Pre:  HashMap exists and contains values
	 * Post: HashMap has been printed 
	 */
	private static void printHashMap() {
		//This is an iterator used to go through all values in a HashMap.  name is a variable that hold
		//the key each time we go through the for loop.  We then get that key which stores the value associated 
		//with is in numTimes.  We then print out the name and value associated with it.
		for(String name:namesMap.keySet()) {
			Integer numTimes = namesMap.get(name);
			System.out.println("Entry [" + name + "] has count " + numTimes);
		}
	}
	
	/* Private instance variables */
	private static Map<String, Integer> namesMap = new HashMap<String, Integer>();
}
