/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program
					implements FacePamphletConstants {

	/** Private instance vars */
	private String nameFieldText;
	private String statusFieldText;
	private String pictureFieldText;
	private String friendFieldText;

	/** private object vars */
	private JTextField nameField;
	private JTextField statusField;
	private JTextField pictureField;
	private JTextField friendField;
	private FacePamphletProfile newProfile;
	private FacePamphletDatabase profileDB;
	private FacePamphletCanvas canvas;

	/**
	 * This method has the responsibility for initializing the 
	 * initialization that needs to be performed.
	 */
	public void init() {
		this.profileDB = new FacePamphletDatabase();
		this.canvas = new FacePamphletCanvas();
		add(canvas);

		//Create text field on top of canvas
			nameField = new JTextField(TEXT_FIELD_SIZE);
			add(new JLabel("Name"), NORTH);
			add(nameField, NORTH);

			//Create buttons on the top of the canvas
			add(new JButton("Add"), NORTH);
			add(new JButton("Delete"), NORTH);
			add(new JButton("Lookup"), NORTH);

			//Create buttons on the left side of the canvas
			statusField = new JTextField(TEXT_FIELD_SIZE);
			add(statusField, WEST);
			add(new JButton("Change Status"), WEST);

			add(new JLabel(EMPTY_LABEL_TEXT), WEST);

			pictureField = new JTextField(TEXT_FIELD_SIZE);
			add(pictureField, WEST);
			add(new JButton("Change Picture"), WEST);

			add(new JLabel(EMPTY_LABEL_TEXT), WEST);

			friendField = new JTextField(TEXT_FIELD_SIZE);
			add(friendField, WEST);
			add(new JButton("Add Friend"), WEST);

			addActionListeners();
			statusField.addActionListener(this);
			pictureField.addActionListener(this);
			friendField.addActionListener(this);
    }
    
  
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent e) {
		//Get action performed on button/text field
    	String cmd = e.getActionCommand();
    	
    	nameFieldText = nameField.getText();
    	statusFieldText = statusField.getText();
    	pictureFieldText = pictureField.getText();
    	friendFieldText = friendField.getText();
    	
    	/*Adds a person to the social network*/
    	if(cmd.equals("Add")) {
    		if(!nameFieldText.equals("")) {
    			/*If name does not already exist, create new profile, store it and display the profile.*/
    			if(!this.profileDB.containsProfile(nameFieldText)) {
					newProfile = new FacePamphletProfile(nameFieldText);
					this.profileDB.addProfile(newProfile);
					this.canvas.displayProfile(this.profileDB.getProfile(nameFieldText));
					this.canvas.showMessage("New profile created");
				}
    			/*Profile already exists*/
    			else {
					this.canvas.showMessage("A profile with the name " + nameFieldText + " already exists");
				}
    		}
    	}
    	/*Deletes a profile from the social network*/
    	else if(cmd.equals("Delete")) {
    		if(!nameFieldText.equals("")){
    			/*If profile exists, delete profile, delete profile from all friends lists, clear canvas*/
				if(this.profileDB.containsProfile(nameFieldText)){
					this.profileDB.deleteProfile(nameFieldText);

					deleteFromAllProfiles(nameFieldText);
					this.canvas.removeAll();

					this.canvas.showMessage("Profile of " + nameFieldText + " deleted");
				} else { /*Profile does not exist*/
					this.canvas.showMessage("A profile with the name " + nameFieldText + " does not exist");
				}
    		}

    	}
    	/*Lookup profile*/
    	else if(cmd.equals("Lookup")) {
    		if(!nameFieldText.equals("")) {
    			/*If profile exists, display profile*/
				if(this.profileDB.containsProfile(nameFieldText)) {
					this.canvas.displayProfile(this.profileDB.getProfile(nameFieldText));
					this.canvas.showMessage("Displaying " + nameFieldText);
				} 
				/*Clear canvas and state that profile does not exist*/
				else {
					this.canvas.removeAll();
					this.canvas.showMessage("A profile with the name " + nameFieldText + " does not exist");
				}
			}
    	}
    	/*Change profiles status*/
    	else if(cmd.equals("Change Status")) {
    		/*If both status and name fields are not blank*/
    		if(!statusFieldText.equals("") && !nameFieldText.equals("")) {
    			/*If the profile exists in the db, change status, display profile*/
    			if(this.profileDB.containsProfile(nameFieldText)){
    				this.profileDB.getProfile(nameFieldText).setStatus(statusFieldText);
					this.canvas.displayProfile(this.profileDB.getProfile(nameFieldText));
					this.canvas.showMessage("Status updated to " + statusFieldText);
				} 
    			/*Profile does not exist*/
    			else {
					this.canvas.showMessage("Please select a profile to change status");
				}
    		} else {
				this.canvas.showMessage("Please select a profile to change status");
			}
    	}
    	/*Change picture for profile*/
    	else if(cmd.equals("Change Picture")) {
    		if(!pictureFieldText.equals("") && !nameFieldText.equals("")) {
				/*If the profile exists in the db, create new image or error if it doesn't exist, clear
				* canvas, display new updated profile*/
				if(this.profileDB.containsProfile(nameFieldText)){
					GImage image = null;
					try{
						image = new GImage(pictureFieldText);
						this.profileDB.getProfile(nameFieldText).setImage(image);
						this.canvas.removeAll();
						this.canvas.displayProfile(this.profileDB.getProfile(nameFieldText));
						this.canvas.showMessage("Picture updated");
					} catch(ErrorException ex) {
						this.canvas.showMessage("Unable to open image file: " + pictureFieldText);
					}
				} else {
					this.canvas.showMessage("Please select a profile to change status");
				}
			} else {
				this.canvas.showMessage("Please select a profile to change status");
			}
    	}
    	/*Add a friend to a profile*/
    	else if(cmd.equals("Add Friend")) {
    		if(!friendFieldText.equals("") && !nameFieldText.equals("")) {
    			/*If profile exists*/
				if(this.profileDB.containsProfile(nameFieldText)){
					/*Check to see if the added friend has a profile*/
					if(doesFriendExist(friendFieldText)) {
						/*If the adding of the friend return true(false will b returned if the friend already
						* exists in the hashSet), add the friend, clear canvas, display new updated profile.*/
						if(this.profileDB.getProfile(nameFieldText).addFriend(friendFieldText)){
							this.profileDB.getProfile(friendFieldText).addFriend(nameFieldText);
							this.canvas.removeAll();
							this.canvas.displayProfile(this.profileDB.getProfile(nameFieldText));
							this.canvas.showMessage(friendFieldText + " added as a friend");
						} 
						/*Friend was already found in the friend list*/
						else {
							this.canvas.showMessage(nameFieldText + " already has " + friendFieldText + " as a friend");
						}
					/*Added friend does not have a profile*/
					} else {
						this.canvas.showMessage(friendFieldText + " does not exist");
					}
				}
    		} else {
				this.canvas.showMessage("Please select a profile to add friend");
			}
    	}
	}

	/*Checks to see if the friend exists in the persons friend list.*/
	private boolean doesFriendExist(String friendName){
    	return this.profileDB.containsProfile(friendName);
	}

	/*If a profile is deleted, this deletes that profile from all profiles
	* friend lists.*/
	private void deleteFromAllProfiles(String name){
    	for(FacePamphletProfile profile : this.profileDB.getAllProfiles()){
			profile.removeFriend(name);
		}
	}
}
