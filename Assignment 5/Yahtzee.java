/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;
import java.util.HashMap;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		//Only allow the # of players to be 4 or less
		while(true) {
			nPlayers = dialog.readInt("Enter number of players 1-4");
			if(nPlayers <= MAX_PLAYERS)
				break;
		}
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		totScores = new int[nPlayers];
		playGame();
	}

	/* Starts the game */
	private void playGame() {
		//Create multi-dimensional array to store categories/scores in for all players
		createMultiArray();
		
		//Stores the score for a round of die depending on what category it is assigned to
		int score = 0;
		
		//This for loop executes 13 times.  Once for each available scoring category
		for(int i = 0; i < N_SCORING_CATEGORIES; i++) {
			//This for loop executes however many times there are players.  Each player get's 13 rounds.
			for(int j = 1; j <= nPlayers; j++) {
				display.printMessage(playerNames[j - 1] + "'s turn!  Click the \"Roll Dice\" button to roll the dice.");
				
				//Initialize roll dice button
				display.waitForPlayerToClickRoll(j);
				
				//Roll/re-roll the die
				dieSelectionAndRoll();
			
				while(true) {
					//Check to see if category is empty, print err msg if not
					if(isCategoryEmpty(j)) {
						//Figure out the score achieved according to category
						score = checkScoring();
						
						//Assign turn to category if empty
						assignTurnCategory(j, score);
						break;
					}
					else
						display.printMessage("You already picked that category.  Please choose another category.");
				}
				
				//Displays the score on the scoresheet for the correct category
				display.updateScorecard(category, j, score);
				
				//Update upper score, lower score, upper bonus and total
				updateTotalScore(i, j);
			}
		}
		//Print msg with who won the game
		displayWinner();
	}
	
	/* Displays the name of the winner with their points total
	 * Pre:  All rounds of the game have been finished
	 * Post: The winner with points total has been displayed
	 */
	private void displayWinner() {
		//Initialize highScore to the first score in the totScores array
		int highScore = totScores[0];
		//Used to keep track of the winning name and score
		int index = 0;
		
		//Check all scores to find the high score
		for(int i = 0; i < nPlayers - 1; i++) {
			if(totScores[i + 1] > highScore) {
				highScore = totScores[i + 1];
				index = i + 1;
			}
		}
		display.printMessage("Congratulations, " + playerNames[index] + ", you're the winner with a total score	of " + totScores[index] + "!");
	}
	
	/* Calculates the score and updates the scorecard with real time scores after a players turn.
	 * Pre:  A player has finished a round of rolls and assigned that roll to a category
	 * Post: Scorecard has been updated with new scores
	 */
	private void updateTotalScore(int round, int player) {
		//update upper score
		int upperScore = 0;
		for(int i = 0; i < 6; i++) {
			if(scores[i][player - 1] != -1)
				upperScore += scores[i][player - 1];
		}
		display.updateScorecard(7, player, upperScore);
		
		//update upper bonus
		int upperBonus = 0;
		if(upperScore >= 63)
			upperBonus = 35;
		display.updateScorecard(8, player, upperBonus);
		
		//update lower score
		int lowerScore = 0;
		for(int i = 8; i < 15; i++) {
			if(scores[i][player - 1] != -1)
				lowerScore += scores[i][player - 1];
		}
		display.updateScorecard(16, player, lowerScore);
		
		//update total score
		int totalScore = 0;
		totalScore = upperScore + lowerScore + upperBonus;
		totScores[player - 1] = totalScore;
		display.updateScorecard(17, player, totalScore);
	}
	
	/* Validates scoring based on category the round has been assigned to.
	 * Pre:  User has selected a category for their round to be assigned to
	 * Post: Current state of the die are evaluated for that category and a score is assigned
	 * 		 and then returned.
	 */
	private int checkScoring() {
		//Used to sum a series of die
		int sum = 0;
		int count = 0;
		
		//Create a hashmap of die and the # of times they are rolled
		createHashMap();

		//Keeps track of how many times a # has been rolled in one round
		Integer cardCounts = 0;
				
		//Switch statement to run through the category selected
		switch(category)
		{
			//Ones: Sum all die who's values = 1
			case 1:  
				cardCounts = fullHouse.get(1);  //Gets how many times a 1 has been rolled
				sum = cardCounts;
				break;
			
			//Twos: Sum all die who's values = 2
			case 2: 
				cardCounts = fullHouse.get(2);  //Gets how many times a 2 has been rolled
				sum = cardCounts * 2;
				break;
			
			//Threes: Sum all die who's values = 3	
			case 3: 
				cardCounts = fullHouse.get(3);  //Gets how many times a 3 has been rolled
				sum = cardCounts * 3;
				break;
				
			//Fours: Sum all die who's values = 4
			case 4: 
				cardCounts = fullHouse.get(4);  //Gets how many times a 4 has been rolled
				sum = cardCounts * 4;
				break;
				
			//Fives: Sum all die who's values = 5
			case 5: 
				cardCounts = fullHouse.get(5);  //Gets how many times a 5 has been rolled
				sum = cardCounts * 5;
				break;
				
			//Sixes: Sum all die who's values = 6
			case 6: 
				cardCounts = fullHouse.get(6);  //Gets how many times a 6 has been rolled
				sum = cardCounts * 6;
				break;
				
			//Three of a kind: Check to see if any 3 die are the same.  Scoring for this category
			//is the sum of all die if there is 3 of a kind.  If not, score is 0.
			case 9: 
				//This for loop goes through all the keys in the fullHouse hashMap and assigns them to the 
				//card variable.  It then gets that key which returns the value(how many times the # has been rolled)
				//and stores that # in cardCounts.  We can then use that to test for 3 of a kind.
				for(Integer card:fullHouse.keySet()) {
					cardCounts = fullHouse.get(card);
					if(cardCounts >= 3)
						count++;
					sum += card * cardCounts;
				}
				if(count < 1)
					sum = 0;
				break;
			
			//Four of a kind: Check to see if any 4 die are the same.  Scoring for this category
			//is the sum of all die if there is 4 of a kind.  If not, score is 0.
			case 10:
				//This for loop goes through all the keys in the fullHouse hashMap and assigns them to the 
				//card variable.  It then gets that key which returns the value(how many times the # has been rolled)
				//and stores that # in cardCounts.  We can then use that to test for 4 of a kind.
				for(Integer card:fullHouse.keySet()) {
					cardCounts = fullHouse.get(card);
					if(cardCounts >= 4)
						count++;
					sum += card * cardCounts;
				}
				if(count < 1)
					sum = 0;
				break;

			//Full house: Consists of 2 of a kind and 3 of a kind.  Sort the array and then compare adjacent values.
			//If adj values are equal, inc count.  There are 3 pairs in a full house(ex. (2 2) (3 (3) 3)).
			case 11:
				//This for loop goes through all the keys in the fullHouse hashMap and assigns them to the 
				//card variable.  It then gets that key which returns the value(how many times the # has been rolled)
				//and stores that # in cardCounts.  We can then use that to test for a full house.
				for(Integer card:fullHouse.keySet()) {
					cardCounts = fullHouse.get(card);
					if(cardCounts == 2 || cardCounts == 3)
						count++;
				}

				if(count == 2)
					sum = 25;
				break;
				
			//Small straight: consists of 4 consecutive values.  Sort the array and then check to see if the 
			//next # in the array minus 1 equals the first #.  This means that the next # is one greater(ex. 1, 2 -> 2-1=1).
			//When comparing four #'s like this, we will get "true" 3 times.  This means that we have a straight of 4 #'s.
			case 12: 
				//This for loop goes through all the keys in the fullHouse hashMap and assigns them to the 
				//card variable.  It then gets that key which returns the value(how many times the # has been rolled)
				//and stores that # in cardCounts.  We can then use that to test for a small straight.
				for(Integer card:fullHouse.keySet()) {
					cardCounts = fullHouse.get(card);
					if(cardCounts == 1 || cardCounts == 2)
						count++;
				}

				if(count >= 4)
					sum = 30;
				break;

			//Large straight: consists of 5 consecutive values.  Sort the array and then check to see if the 
			//next # in the array minus 1 equals the first #.  This means that the next # is one greater(ex. 1, 2 -> 2-1=1).
			//When comparing four #'s like this, we will get "true" 4 times.  This means that we have a straight of 5 #'s.
			case 13:
				//This for loop goes through all the keys in the fullHouse hashMap and assigns them to the 
				//card variable.  It then gets that key which returns the value(how many times the # has been rolled)
				//and stores that # in cardCounts.  We can then use that to test for large straight.
				for(Integer card:fullHouse.keySet()) {
					cardCounts = fullHouse.get(card);
					if(cardCounts == 1)
						count++;
				}

				if(count >= 5)
					sum = 40;
				break;
				
			//Yahtzee!  All die must be the same #.  Check consecutive #'s to see if they are equal.  We will get "true"
			//4 times if all 5 die are equal.
			case 14: 
				//This for loop goes through all the keys in the fullHouse hashMap and assigns them to the 
				//card variable.  It then gets that key which returns the value(how many times the # has been rolled)
				//and stores that # in cardCounts.  We can then use that to test for a yahtzee.
				for(Integer card:fullHouse.keySet()) {
					cardCounts = fullHouse.get(card);
					if(cardCounts == 5)
						count++;
				}

				if(count == 1)
					sum = 50;
				break;
				
			//Chance: Any roll can be assigned to this category.  The scoring for this is just the sum of all die.
			case 15: 
				for(int i = 0; i < N_DICE; i++) {
					sum += dice[i];
				}
				break;
				
			default:
				sum = 0;
		}
		
		return sum;
	}
	
	/* Creates a hashmap of die values as the key and updates the value according to how many dice
	 * show that same value. ex -> 1:2, 2:0, 3:3
	 */
	private void createHashMap(){
		fullHouse = new HashMap<Integer, Integer>();
		
		/* Initialize hashmap with 0's */
		for(int i = 1; i < 7; i++) {
			fullHouse.put(i, 0);
		}
		
		/* Cycle through all dice.  Add 1 to the key every time we see that value. */
		for(int i = 0; i < N_DICE; i++) {
			if(fullHouse.containsKey(dice[i]))
				fullHouse.put(dice[i], fullHouse.get(dice[i]) + 1);
		}
	}
	
	/* Assigns the score to the correct place in the multi-dimensional array.
	 * Pre:  MD array exists.  Current player and score are passed to method
	 * Post: Score has been assigned to the correct category and to the correct player
	 */
	private void assignTurnCategory(int player, int score) {
		//The array starts at 0.  category and player both start at 1.  We subtract 1 so that we get a 
		//starting array positions of 0.
		scores[category - 1][player - 1] = score;
	}
	
	/* Creates a multi-dimensional array.  The array has N_CATEGORIES(all options that we can assign
	 * a round too) rows and nPlayers(# of players) columns.
	 * Pre:  The # of players has been entered
	 * Post: A multi-dimensional array has been created
	 */
	private void createMultiArray() {
		//Create multi-dimensional array for scores/categories
		scores = new int[N_CATEGORIES][nPlayers];
				
		//Initialize array to all -1 since that will not exists as a valid score
		for(int i = 0; i < N_CATEGORIES; i++) {
			for(int j = 0; j < nPlayers; j++) {
				scores[i][j] = -1;
			}
		}
	}
	
	/* Checks to see if a players round has been assigned to a category.
	 * Pre:  The player is passed to this method
	 * Post: True is returned if nothing has been assigned to category and false if 
	 * 		 it already contains a score
	 */
	private boolean isCategoryEmpty(int player) {
		//Player must assign roll to a category
		category = display.waitForPlayerToSelectCategory();
			
		//Checks to see if the player has assigned a value to a category or not.  All categories
		//are initialized with a -1 since we can never achieve that value w the die.  If the 
		//value is -1, we return true so that the player can assign a roll to the category.
		if(scores[category - 1][player - 1] == -1) return true;
		
		//Return false if that category has a value assigned to it.
		return false;
	}
	
	/* Runs through the steps to complete a round for a single player.  Rolls the dice, displays the 
	 * values for the die, prints a directional msg to the user and gets the die that the user selects to 
	 * re-roll.
	 * Pre: Players round has started
	 * Post: Players round of 3 turns has ended
	 */
	private void dieSelectionAndRoll() {
		//Used to tell the rollDice function whether or not to initialize all dice or check and see which
		//dice were selected by the player for a re-roll.  If turn = 1, initialize all 5 die.  Any other value, 
		//check for selected die and only re-roll those die.
		int turn = 1;
		
		//Player has 3 turns
		for(int i = 0; i < MAX_TURNS; i++) {
			turn += i;
			
			//Click the button to roll the dice and keep track of all values in an array
			rollDice(turn);
			
			//Display the values of the die on the screen
			display.displayDice(dice);
						
			//Display instructional msg
			msgPlayer(turn);
			
			//Allows the player to select die to re-roll.
			if(i < 2)
				display.waitForPlayerToSelectDice();
		}
	}

	/* Displays directions for the players next turn depending on what turn they are on.
	 * Pre:  turn is passed to the method
	 * Post: A directional msg is printed for the user
	 */
	private void msgPlayer(int turn) {
		if(turn == 1 || turn == 2)
			display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\"");
		else
			display.printMessage("Select a category for this roll");
	}
	
	/* Checks to see if this is the players first turn or not.  If so, creates a new array for the
	 * 5 dice #'s to be stored in and randomly assigns all 5 die a # between 1 and 6.  If it's not 
	 * the players first turn, we check the die to see what the player has selected and only 
	 * re-roll those die using the random # generator.  
	 * Pre:  The players turn is passed to this method
	 * Post: Returns an array of 5 die when turn = 1 and re-rolls only the selected die otherwise
	 */
	private void rollDice(int turn) {
		//Players first turn of the round
		if(turn == 1) {
			//Create an array of size N_DICE(5)
			dice = new int[N_DICE];
			
			//Call the random # generator for each die and assign it to the array.
			for(int i = 0; i < N_DICE; i++) {
				dice[i] = rgen.nextInt(1, 6);
			}
		//Do this for all other turns in the players round
		} else {
			//Check to see if each die has been selected.  If it has, re-roll the die and 
			//store the new value.
			for(int i = 0; i < N_DICE; i++) {
				if(display.isDieSelected(i)) {
					dice[i] = rgen.nextInt(1, 6);
				}
			}
		}
	}
		
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	private int[] dice;
	private YahtzeeMagicStub YMagicStub;
	private int[][] scores;
	private int category;
	private int[] totScores;
	HashMap<Integer, Integer> fullHouse;
}
