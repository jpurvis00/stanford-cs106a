/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */


import acm.graphics.*;
import java.awt.*;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class FacePamphletCanvas extends GCanvas 
					implements FacePamphletConstants {

	private GLabel appMsg;

	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the display
	 */
	public FacePamphletCanvas() {
	}

	
	/** 
	 * This method displays a message string near the bottom of the 
	 * canvas.  Every time this method is called, the previously 
	 * displayed message (if any) is replaced by the new message text 
	 * passed in.
	 */
	public void showMessage(String msg) {
		/*If a msg already exists, remove it so we are not writing over the top of it*/
		if(appMsg != null)
			remove(appMsg);

		appMsg = new GLabel(msg);
		appMsg.setFont(MESSAGE_FONT);
		add(appMsg, (getWidth() / 2) - (appMsg.getWidth() / 2), getHeight() - BOTTOM_MESSAGE_MARGIN);
	}
	
	
	/** 
	 * This method displays the given profile on the canvas.  The 
	 * canvas is first cleared of all existing items (including 
	 * messages displayed near the bottom of the screen) and then the 
	 * given profile is displayed.  The profile display includes the 
	 * name of the user from the profile, the corresponding image 
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		removeAll();

		displayName(profile);
		displayImage(profile);
		displayStatus(profile);
		displayFriends(profile);
	}

	/*Displays profiles name*/
	private void displayName(FacePamphletProfile profile){
		/*Display profile name*/
		GLabel label = new GLabel(profile.getName());
		label.setFont(PROFILE_NAME_FONT);
		label.setColor(Color.BLUE);
		add(label, LEFT_MARGIN, TOP_MARGIN * 2);
	}

	/*Displays profiles image*/
	private void displayImage(FacePamphletProfile profile){
		GImage image = profile.getImage();
		
		/*If no image has been set yet, we will display an empty rectangle with the words
		* "No Image" in the rect.*/
		if(image == null){
			GRect rect = new GRect(IMAGE_WIDTH, IMAGE_HEIGHT);
			rect.setLocation(LEFT_MARGIN, IMAGE_MARGIN + (TOP_MARGIN * 2));
			add(rect);

			GLabel msg = new GLabel("No Image");
			msg.setFont(PROFILE_IMAGE_FONT);
			msg.setLocation(LEFT_MARGIN + ( IMAGE_WIDTH / 2 ) - ( msg.getWidth() / 2 ) , IMAGE_MARGIN + (TOP_MARGIN * 2) + ( IMAGE_HEIGHT / 2 ) );
			add(msg);
		} 
		/*Display image*/
		else {
			double x = IMAGE_WIDTH / image.getWidth();
			double y = IMAGE_HEIGHT / image.getHeight();
			image.scale(x, y);
			image.setLocation(LEFT_MARGIN, IMAGE_MARGIN + (TOP_MARGIN * 2));
			add(image);
		}
	}

	/*Displays profiles status*/
	private void displayStatus(FacePamphletProfile profile){
		GLabel label;

		if(profile.getStatus().equals("")){
			label = new GLabel("No current status");
		} else {
			label = new GLabel(profile.getName() + " is " + profile.getStatus());
		}

		label.setFont(PROFILE_STATUS_FONT);
		add(label, LEFT_MARGIN, STATUS_MARGIN + IMAGE_MARGIN + IMAGE_HEIGHT + (TOP_MARGIN * 2));
	}

	/*Displays a list of all the profiles friends*/
	private void displayFriends(FacePamphletProfile profile){
		/*Display Friends: header*/
		GLabel friends = new GLabel("Friends: ");
		friends.setLocation(getWidth() / 2, IMAGE_MARGIN + (TOP_MARGIN * 2));
		friends.setFont(PROFILE_FRIEND_LABEL_FONT);
		add(friends);

		Iterator itr = profile.getFriends();
		double y = IMAGE_MARGIN + (TOP_MARGIN * 2) + friends.getHeight();

		/*Display all friends under header*/
		while(itr.hasNext()){
			String friend = "" + itr.next();
			GLabel friendLbl = new GLabel(friend);
			friendLbl.setFont(PROFILE_FRIEND_FONT);
			add(friendLbl, getWidth() / 2, y);
			y += friendLbl.getHeight();
		}
	}
}
