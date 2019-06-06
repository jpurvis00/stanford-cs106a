/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;
import acm.graphics.*;

public class NameSurfer extends Program implements NameSurferConstants {

/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the top of the window.
 */
	public void init() {
	    //Create text field on bottom of canvas
		nameField = new JTextField(15);
		add(new JLabel("Name"), SOUTH);
		add(nameField, SOUTH);
		
		//Create Graph and Clear buttons on the bottom of the canvas
		add(new JButton("Graph"), SOUTH);
		add(new JButton("Clear"), SOUTH);
		
		//Create new NameSurferGraph object and add it to display
		graph = new NameSurferGraph();
		add(graph);
		
		//Create listeners for the text field and buttons
		nameField.addActionListener(this);
		addActionListeners();
		
		namesDB = new NameSurferDataBase(NAMES_DATA_FILE);
	}

/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
	public void actionPerformed(ActionEvent e) {
		//Get action performed on button/text field
		String cmd = e.getActionCommand();
		
		//Graph button has been clicked
		if(cmd.equals("Graph")) {
			//Get input from text field
			String nameFieldText = nameField.getText();
			
			nameFieldText = titleCase(nameFieldText);
			
			NameSurferEntry rank = namesDB.findEntry(nameFieldText);
			//If name appears in the db, graph it
			if(rank != null) {
				graph.addEntry(rank);
				graph.update();
			}
		} 
		//Clear button has been clicked
		else if (cmd.equals("Clear")) {
			graph.clear();
			graph.update();
		}
		
		//Text has been entered in the text field and the enter button has been hit instead of clicked with the mouse
		if(e.getSource() == nameField) {
			String nameFieldText = nameField.getText();
			
			nameFieldText = titleCase(nameFieldText);
			
			NameSurferEntry rank = namesDB.findEntry(nameFieldText);
			//If name appears in the db, graph it
			if(rank != null) {
				graph.addEntry(rank);
				graph.update();
			}
		}
	}
	
	public String titleCase(String str) {
		//Check to see if first char is lowercase, if so, convert to uppercase and put the name back together.
		//All names in the names-data.txt file are upper case.  We can't expect the user to know that. 
		char ch = str.charAt(0);
		if(Character.isLowerCase(ch)) ch = Character.toUpperCase(ch);
		str = ch + str.substring(1);
		
		return str;
	}
	
	/* Private instant vars */
	private JTextField nameField;
	
	/** Private obj vars **/
	private NameSurferEntry NSEntry;
	private NameSurferGraph graph;
	private NameSurferDataBase namesDB;
	
}
