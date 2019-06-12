/*  Reads in a file containing flight destinations from various cities, 
 * and then allow the user to plan a round-trip flight route.
 */

import acm.program.*;
import acm.util.*;
import java.io.*;
import java.util.*;

public class FlightPlanner extends ConsoleProgram {

	public void run() {
		readFile();
		welcome();
		getRoute();
		dispFinalRoute();
	}
	
	/*
	 * Gets the route to be flown by the customer.
	 * Pre:  None
	 * Post: A flight plan will have been established.  Includes a startCity, all
	 * 		 connecting cities and the final destination city.
	 */
	private void getRoute() {
		String startCity = "";
		String destCity = "temp";
		
		//Check to see if user defined startCity exists in the list of cities that are available for flights.  
		//Reprompt if it's not available.
		while(true) {
			//Get startCity and set destCity to temp so that we can enter our while loop at least once
			startCity = getStartingCity();
			
			//Checks to see if city is in the hash map of cities.
			if(getDestCitiesHashMap(startCity) == null) println("That city is not available for flights.  Please rechoose your starting city.");
			else break;
		}
		
		//Add startCity to the route arrayList
		route.add(startCity);
		
		//Display all available cities that can be flown to
		dispDestCities(startCity);
				
		//Do until our startCity matches our destCity which signifies a complete round trip
		while(!startCity.equals(destCity)) {
			while(true) {
				//check if dest city can be gotten to from start city
				destCity = getDestCity();
				
				//display error msg if not and re get dest city
				if(checkValidDestCities(startCity, destCity))
					break;
			}
			
			route.add(destCity);
			
			//Display list of dest cities
			if(!startCity.equals(destCity))
				dispDestCities(destCity);
		}
	}
	
	/*
	 * Checks to see if the city the customer types exists in the arrayList that was returned from the 
	 * HashMap lookup.
	 * Pre:  startCity and destCity are passed in
	 * Post: If the dest city is found in the arrayList, return true meaning that we can fly to the city.
	 * 		 Return false if the city is not in the arrayList.  
	 */
	private boolean checkValidDestCities(String startCity, String destCity) {
		//Call the getDestCititesHashMap method passing the startCity and store all results in an arrayList.
		ArrayList<String> destCities = getDestCitiesHashMap(startCity);
		
		boolean found = true;
		
		//Return true if the destCity = startCity so that we don't execute the rest of this method.  Without this
		//the method doesn't recognize that they are equal and displays the error msg.
		if(startCity.equals(destCity)) return true;
		
		//Checks all cities that can be flown to.  If the destCity is in the arrayList, return true.
		for(int i = 0; i < destCities.size(); i++) {
			if(!destCity.equals(destCities.get(i))) {
				found = false;
			} else {
				return true;
			}
		}
		
		//destCity was not found, disp error msg.
		if(!found) println("You can't get to that city by a direct flight.");
		
		return found;
	}
	
	/*
	 * Print out the flight route of cities.
	 */
	private void dispFinalRoute() {
		println("The route you've chosen is:");
		for(int i = 0; i < route.size(); i++) {
			print(route.get(i));
			if(i != route.size() - 1)
				print(" -> ");
		}
	}
	
	/*
	 * Gets the city that the user/customer wants to fly to.
	 * Pre:  A startCity has already been selected.
	 * Post: A destination city has been inputed and stored in the route arrayList.  destCity is returned.
	 */
	private String getDestCity() {
		String destCity;
		//route.get(route.size() - 1 pulls the last entry from the route arrayList so that the correct last city entered
		//is always displayed in the question.
		destCity = readLine("Where do you want to go from " + route.get(route.size() - 1) + "? ");
		
		return destCity;
	}
	
	/*
	 * Displays all cities that you can fly to from the given start city.
	 * Pre:  City that user will be flying from is passed in.
	 * Post: Creates an arrayList of all cities that can be flown to from the start city.
	 * 		 It then iterates through that arrayList and displays the available cities.
	 */
	private void dispDestCities(String startCity) {
		//Call the getDestCititesHashMap method passing the startCity and store all results in an arrayList.
		ArrayList<String> destCities = getDestCitiesHashMap(startCity);
		
		//Display all cities that can be flown to.
		println("From " + startCity + " you can fly directly to:");
		for(int i = 0; i < destCities.size(); i++) {
			println(" " + destCities.get(i));
		}
	}
	
	/*
	 * Prompts user for starting city for the round trip flight.
	 */
	private String getStartingCity() {
		String origStartCity = readLine("Enter the starting city: ");
		
		return origStartCity;
	}
	
	/*
	 * Prints out beginning msgs for the user as well as all possible cities that can be flown to.
	 */
	private void welcome() {
		println("Welcome to Flight Planner!\nHere's a list of all the cities in our database:");
		printCities();	
		println("Let's plan a round-trip route!");
	}
	
	private void readFile() {
		//Create data structures.  Originally had this in the createDataSTructure method but
		//it would overwrite it every time method was called.  Putting it here so they are only created once.
		cities = new ArrayList<String>();
		flights = new HashMap<String, ArrayList<String>>();
		
		//Open file with flight info
		try {
			BufferedReader rd = new BufferedReader(new FileReader("U:\\study\\cs106a\\Section Handout #8\\src\\flights.txt"));
			String line = "";
			
			//Read each line in the file and pass it one by one to the createDataStructure method
			while(true) {
				line = rd.readLine();
				if(line == null) break;
				
				createDataStructure(line);				
			}
			rd.close();
		}
		catch(IOException ex) {
			throw new ErrorException(ex);
		}
	}
	
	private void createDataStructure(String line) {
		//Do the following if the line is not blank
		if(line.length() != 0) {
			//Find the index of -> separator and store the two cities in 2 diff vars
			int dashInd = line.indexOf("->");
			String startCity = line.substring(0, dashInd - 1);
			String destCity = line.substring(dashInd + 3);
			
			//Send both cities to be added to an ArrayList
			addCitiesArrLst(startCity);
			addCitiesArrLst(destCity);
			getDestCitiesHashMap(startCity).add(destCity);
		}
	}
	
	private void addCitiesArrLst(String city) {
		//If the cities ArrayList is empty, add first city
		if(cities.size() == 0) {
			cities.add(city);
			//Create HashMap of city and an empty ArrayList which will hold the destination cities once populated.
			flights.put(city, new ArrayList<String>());
		} 
		//If the cities ArrayList is not empty, check to see if the city exists.  If not add city.
		else {
			for(int i = 0; i < cities.size(); i++) {
				if(!cities.contains(city)) {
					cities.add(city);
					//Create HashMap of city and an empty ArrayList which will hold the destination cities once populated.
					flights.put(city, new ArrayList<String>());
				}
			}
		}
	}
	
	private void printCities(){
		//If there are items in the cities ArrayList, print them all out.
		if(cities.size() != 0) {
			for(int i = 0; i < cities.size(); i++) {
				println(" " + cities.get(i));
			}
		}
	}
	
	private ArrayList<String> getDestCitiesHashMap(String startCity) {
		return flights.get(startCity);
	}
	
	private void getFlightRoutes() {
		Iterator<String> flightsIt = flights.keySet().iterator();
		while(flightsIt.hasNext()) {
			println(flights.get(flightsIt.next()).toString());
		}
	}
	
	/** Private instance vars */
	private ArrayList<String> cities;
	private HashMap<String, ArrayList<String>> flights;
	private ArrayList<String> route = new ArrayList<String>();
}
