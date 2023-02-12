/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      29 nov. 2020
 * File:      SuperPacmanBehavior.java
 */

package ch.epfl.cs107.play.game.superpacman.area;

import java.util.ArrayList;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.Cell;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.Blinky;
import ch.epfl.cs107.play.game.superpacman.actor.Bonus;
import ch.epfl.cs107.play.game.superpacman.actor.Cherry;
import ch.epfl.cs107.play.game.superpacman.actor.Diamond;
import ch.epfl.cs107.play.game.superpacman.actor.Ghost;
import ch.epfl.cs107.play.game.superpacman.actor.Inky;
import ch.epfl.cs107.play.game.superpacman.actor.Pinky;
import ch.epfl.cs107.play.game.superpacman.actor.Wall;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

public class SuperPacmanBehavior extends AreaBehavior {

	private ArrayList<Ghost> ghostsArray = new ArrayList<Ghost>();
	private AreaGraph graph = new AreaGraph();
	
	public enum SuperPacmanCellType {
		NONE(0), // never used as real content 
		WALL(-16777216), //black 
		FREE_WITH_DIAMOND(-1), //white 
		FREE_WITH_BLINKY(-65536), //red 
		FREE_WITH_PINKY(-157237), //pink 
		FREE_WITH_INKY(-16724737), //cyan 
		FREE_WITH_CHERRY(-36752), //light red 
		FREE_WITH_BONUS(-16478723), //light blue 
		FREE_EMPTY(-6118750); // sort of gray
		
		

		final int type; 
		 
		
		SuperPacmanCellType(int type){
			this.type = type;
		}

		
		/*
		 * return the type of the cell in function of it coulour in the area grid
		 */
		public static SuperPacmanCellType toType(int type){
			for(SuperPacmanCellType ict : SuperPacmanCellType.values()){
				if(ict.type == type)
					return ict;
			}
			return NONE;
		}
	}
	
	/**
	 * Default SuperPacmanBehavior Constructor
	 * @param window (Window), not null
	 * @param name (String): Name of the Behavior, not null
	 */
	public SuperPacmanBehavior(Window window, String name){
		super(window, name);
		int height = getHeight();
		int width = getWidth();
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width ; x++) {
				SuperPacmanCellType color = SuperPacmanCellType.toType(getRGB(height-1-y, x));
				setCell(x,y, new SuperPacmanCell(x,y,color));
			}
		}
	}
	
	/*@param x: the coordinate x on the grid(the width),not null
	 *@param y: the coordinate y on the grid(the height), not null
	 *return if the type of the cell of coordinates(x;y) is WALL or not
	 */
	protected boolean isWall(int x, int y) {
		try {
			SuperPacmanCell newCell = (SuperPacmanCell) getCell(x, y);
			return (newCell.type ==  SuperPacmanCellType.WALL);
		}catch (Exception e) {
			return false;
		}
		
	}
	
	/*@param x: the coordinate x on the grid(the width), not Null
	 * @param y: the coordinate y on the grid (the height), not Null
	 * boolean that return if the node of coordinates (x,y) exists in the graph 
	 * the exception message is catched and returns that the node does not exist*/
	
	private boolean calculEdge(int x, int y, String edge) {
		boolean result = false;
		switch(edge) {
			case "left" : /*determine if the node has a left edge (his left neighbor is a Wall)*/
				try {
					result = (x > 0 && !isWall(x-1, y));
				}catch(Exception e) {
					result = false;
				}
				break;
			case "right" : /*determine if the node has a right edge (his right neighbor is a Wall)*/
				try {
					result = x < getHeight()-1 && !isWall(x+1, y);
				}catch(Exception e) {
					result = false;
				}
				break;
			case "up" :   /*determine if the node has an upper edge (his upper neighbor is a Wall)*/
				try {
					result = (y < getWidth()-1 && !isWall(x, y+1));
				}catch(Exception e) {
					result = false;
				}
				break;
			case "down" : /*determine if the node has a bottom edge (his bottom neighbor is a Wall)*/
				try {
					result = y > 0 && !isWall(x, y-1);
				}catch(Exception e) {
					result = false;
				}
				break;
		}
		return result;
	}
	
	
	/*@param: Area is the area when the game is played, not Null
	 *@param DiscreteCoordinates are the coordinates on the area of the actor, not Null
	 * this method make the actor be part of the grid associated to the area 
	 * @param type: is the type of the cell, not Null
	 */
	protected void registerActors(Area area) {
		int height = getHeight();
		int width = getWidth();
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(!isWall(y, x)) {
					graph.addNode(new DiscreteCoordinates(y, x), calculEdge(y, x, "left"), calculEdge(y, x, "up"), calculEdge(y, x, "right"), calculEdge(y, x, "down"));
				}
				
				switch(((SuperPacmanCell) getCell(x, y)).type) {
					case WALL:
						area.registerActor(new Wall(area, new DiscreteCoordinates(x, y), neighbourhood(x, y)));
						break;
						
					case FREE_WITH_DIAMOND:
						area.registerActor(new Diamond(area,Orientation.UP, new DiscreteCoordinates(x, y)));
						SuperPacmanArea temporary = ((SuperPacmanArea) area);
						temporary.setDiamondsNumber(temporary.getDiamondsNumber()+1);
						System.out.println("Le nombre final de diamants est : " + temporary.getDiamondsNumber());

						break;
					
					case FREE_WITH_CHERRY:
						area.registerActor(new Cherry(area,Orientation.UP, new DiscreteCoordinates(x,y)));
						break;
					
					
					case FREE_WITH_BONUS:
						area.registerActor(new Bonus(area, Orientation.UP, new DiscreteCoordinates(x,y)));
						break;
					case FREE_WITH_BLINKY:
						Blinky blinky = new Blinky(area, Orientation.RIGHT, new DiscreteCoordinates(x, y));
						area.registerActor(blinky);
						ghostsArray.add(blinky);
						break;
					case FREE_WITH_INKY:
						Inky inky = new Inky(area, Orientation.RIGHT, new DiscreteCoordinates(x, y));
						area.registerActor(inky);
						ghostsArray.add(inky);
						break;
					case FREE_WITH_PINKY:
						Pinky pinky = new Pinky(area, Orientation.RIGHT, new DiscreteCoordinates(x, y));
						area.registerActor(pinky);
						ghostsArray.add(pinky);
						break;
					case FREE_EMPTY:
						break;
				default:
					break;
						
					}
				
				
			}
		}
	
	}
	
	/*@param DiscreteCoordinates: coordinates of the node in the area, not Null;
	 * @param: Logic signal is the signal of the  node , notNull;
	 * The method call the graph class method to set the signal of the nodes
	 */
	public void setGraphSignal(DiscreteCoordinates coordinates, Logic signal) {
		if(graph.nodeExists(coordinates)) {
			graph.setSignal(coordinates, signal);
		}
	}
	
	/*
	 * @param (x,y) the coordinates of the wall, not Null
	 * the method return if the neighborhood of the wall is composed of walls
	 */
	protected boolean[][] neighbourhood(int x, int y){
		boolean[][] neighbour = new boolean[3][3];
		
		
		for(int i = -1; i <= 1; i++) {
			
			for(int j = -1; j <= 1; j++) {
				try {
					neighbour[j+1][2-i-1] = isWall(x+j, y+i);
				} catch(Exception e) {
					neighbour[j+1][2-i-1] = false;
				}
				
			}
		}
		
		return neighbour;
	}
	
	/*
	 * the method scare all the ghosts of the area
	 */
	public void scareGhosts() {
		for(int i = 0; i < ghostsArray.size(); i++) {
			ghostsArray.get(i).setIsAfraid(true);
		}
	}
	
	/*
	 * the method make the ghosts return to their normal state
	 */
	public void resetGhostState() {
		for(int i = 0; i < ghostsArray.size(); i++) {
			ghostsArray.get(i).setIsAfraid(false);
		}
	}
	
	/*
	 * method that remove the ghosts from the list 
	 */
	public void unregisterGhosts() {
		for(int i = 0; i < ghostsArray.size(); i++) {
			ghostsArray.get(i).unregisterGhost();
		}
	}
	
	/*
	 * the method make all the ghosts return to their spawn position 
	 * if they eat the player or they got eaten 
	 */
	public void resetGhostPosition() {
		for(int i = 0; i < ghostsArray.size(); i++) {
			ghostsArray.get(i).ghostRespawn();
		}
	}
	
	public class SuperPacmanCell extends Cell {

		/// Type of the cell following the enum
			 private final SuperPacmanCellType type;	
			/**
			 * Default SuperPacmanCell Constructor
			 * @param x (int): x coordinate of the cell
			 * @param y (int): y coordinate of the cell
			 * @param type (EnigmeCellType), not null
			 */
			public  SuperPacmanCell(int x, int y, SuperPacmanCellType type){
				super(x, y);
				this.type = type;
			}
			
			
			/*
			 * return thqt the entity can leave the area cells
			 */
			@Override
			protected boolean canLeave(Interactable entity) {
			return true;
			}

			
			/*
			 * indicates that the cell where the entity want to settle is not already 
			 * occupied by an entity who take all the cell space
			 */
			@Override
			protected boolean canEnter(Interactable entity) {
				return !this.hasNonTraversableContent();
				
		    }

			    
			@Override
			public boolean isCellInteractable() {
				return true;
			}

			@Override
			public boolean isViewInteractable() {
				return false;
			}

			@Override
			public void acceptInteraction(AreaInteractionVisitor v) {
			}
			


	}
	
	public AreaGraph getGraph() {
		 return graph;
	}

	
}
