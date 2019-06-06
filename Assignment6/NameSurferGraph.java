/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes
 * or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {

	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraph() {
		addComponentListener(this);
		displayEntries = new ArrayList<NameSurferEntry>();
	}
	
	
	/**
	* Clears the list of name surfer entries stored inside this class.
	*/
	public void clear() {
		displayEntries.clear();
	}
	
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	* Note that this method does not actually draw the graph, but
	* simply stores the entry; the graph is drawn by calling update.
	*/
	public void addEntry(NameSurferEntry entry) {
		displayEntries.add(entry);
	}
	
	
	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	*/
	public void update() {
		removeAll();
		createGrid();
		drawLabels();
		if(displayEntries.size() >= 0) {
			for(int i = 0; i < displayEntries.size(); i++) {
				drawGraph(displayEntries.get(i), i);
			}
		}
	}
	
	/**
	 * Draws the grid structure to the canvas.  
	 */
	private void createGrid() {
		//draw all vertical lines
		for(int i = 0; i < NDECADES; i++) {			
			vertLines = new GLine(i * (getWidth()/NDECADES), 1, i * (getWidth()/NDECADES), getHeight());
			add(vertLines);
		}
		
		//draw top horizontal lines
		horzLines = new GLine(1, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE);
		add(horzLines);
		
		//draw bottom horizontal lines
		horzLines = new GLine(1, getHeight() - GRAPH_MARGIN_SIZE, getWidth(), getHeight() - GRAPH_MARGIN_SIZE);
		add(horzLines);
	}
	
	/**
	 * Draw year labels on grid
	 */
	private void drawLabels() {
		for(int i = 0; i < NDECADES; i++) {
			//increment start year by 10 each x through the loop.  convert int to string so we can use it as a glabel
			String year = Integer.toString(START_DECADE + (i * 10));
			decade = new GLabel(year, i * (getWidth()/NDECADES) + 3, getHeight());
			add(decade);
		}
	}
	
	/**
	 * Plots the lines on the graph and the names that the lines represent
	 */
	private void drawGraph(NameSurferEntry entry, int eNumb) {
		//draw lines
		for(int i = 0; i < NDECADES - 1; i++) {
			//name will be displayed at each year 
			String name = entry.getName();
			//finalDispName will be used for the last year. This is needed as our loop stops at NDECADES - 1.  
			String finalDispName = name;
			int rank1 = entry.getRank(i);
			int rank2 = entry.getRank(i + 1);
			
			//Create one string by appending the rank to the name if rank1 is not 0
			if(rank1 != 0) name += " " + Integer.toString(rank1);
			//If rank1 is 0, append an * signifying that there is no rank for that name for that year
			else name += " *";
			
			//Create one string by appending the rank to finalDispName if rank2 is not 0
			if(i + 1 == NDECADES - 1 && rank2 != 0) finalDispName += " " + Integer.toString(rank2); 
			//If rank2 is 0, append an * signifying that there is no rank for that name for that year
			else if(i + 1 == NDECADES - 1) finalDispName += " *";
						
			//If both rank1 and rank2 have values > 0, plot the line from one year to the next
			if(rank1 > 0 || rank2 > 0) {
				//If rank1 has a 0 value, start the line at the bottom of the grid 
				if(rank1 == 0) {
					graphLine = new GLine(i * (getWidth()/NDECADES), getHeight() - GRAPH_MARGIN_SIZE, (i + 1) * (getWidth()/NDECADES), GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE*2) * rank2/MAX_RANK);
					nameLabel = new GLabel(name, i * (getWidth()/NDECADES) + 3, getHeight() - GRAPH_MARGIN_SIZE);
					lastNameLabel = checkForLastDisplayName(finalDispName, i, rank2);
				} 
				//If rank2 has a 0 value, the line must end up at the bottom of the grid
				else if (rank2 == 0){
					graphLine = new GLine(i * (getWidth()/NDECADES), GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE*2) * rank1/MAX_RANK, (i + 1) * (getWidth()/NDECADES), getHeight() - GRAPH_MARGIN_SIZE);
					nameLabel = new GLabel(name, i * (getWidth()/NDECADES) + 3, getHeight() - GRAPH_MARGIN_SIZE);
					lastNameLabel = checkForLastDisplayName(finalDispName, i, rank2);
				} 
				//Both rank1 and rank2 have values, plot the line starting at rank 1 and ending at rank2.
				else {
					graphLine = new GLine(i * (getWidth()/NDECADES), GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE*2) * rank1/MAX_RANK, (i + 1) * (getWidth()/NDECADES), GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE*2) * rank2/MAX_RANK);
					nameLabel = new GLabel(name, i * (getWidth()/NDECADES) + 3, GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE*2) * rank1/MAX_RANK);
					lastNameLabel = checkForLastDisplayName(finalDispName, i, rank2);
				}	
			}
			//name did not rank for time period.  output no line and put name at bottom of grid
			else if (rank1 == 0 && rank2 == 0) {
				//Set graphline to 0's, without this we got a null point exc if this if statement was executed first bc
				//I was adding the graphline object below wout first creating the obj
				graphLine = new GLine(0, 0, 0, 0);
				nameLabel = new GLabel(name, i * (getWidth()/NDECADES) + 3, getHeight() - GRAPH_MARGIN_SIZE);
				lastNameLabel = checkForLastDisplayName(finalDispName, i, rank2);
			}
			
			//Run each name through a cycle of 4 colors.  Default is black.
			if(eNumb % 4 == 1) {graphLine.setColor(Color.RED); nameLabel.setColor(Color.RED); lastNameLabel.setColor(Color.RED);}
			else if (eNumb % 4 == 2) {graphLine.setColor(Color.BLUE); nameLabel.setColor(Color.BLUE); lastNameLabel.setColor(Color.BLUE);}
			else if (eNumb % 4 == 3) {graphLine.setColor(Color.MAGENTA); nameLabel.setColor(Color.MAGENTA); lastNameLabel.setColor(Color.MAGENTA);}
			
			//Add all graphic objects to canvas
			add(graphLine);
			add(nameLabel);
			//If we are on the last iteration of the loop, add the last name label
			if(i + 1 == NDECADES - 1) add(lastNameLabel);
		}
	}
	
	/*
	 * Checks to see if we are on the last iteration of the loop for the current name we are on in the draw graph function.  
	 * It then creates the last glabel for the last decade.  This is needed because we need to access the last rank2. 
	 */
	private GLabel checkForLastDisplayName(String fName, int numb, int rank2) {
		if(numb == 9 && rank2 != 0) 
			lastNameLabel = new GLabel(fName, (numb + 1) * (getWidth()/NDECADES) + 3, GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE*2) * rank2/MAX_RANK);
		else	
			lastNameLabel = new GLabel(fName, (numb + 1) * (getWidth()/NDECADES) + 3, getHeight() - GRAPH_MARGIN_SIZE);
			
		return lastNameLabel;
	}
	
	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
	
	/* Private instance vars */
	private ArrayList<NameSurferEntry> displayEntries;
	
	/** Private obj vars **/
	private GLine vertLines;
	private GLine horzLines;
	private GLabel decade;
	private GLabel nameLabel;
	private GLine graphLine;
	private GLabel lastNameLabel;
}
