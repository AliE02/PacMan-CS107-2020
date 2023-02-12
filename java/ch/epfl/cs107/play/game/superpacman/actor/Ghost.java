/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      8 déc. 2020
 * File:      Ghost.java
 */

package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public abstract class Ghost extends MovableAreaEntity implements Interactor{
	protected final static int GHOST_SCORE = 500;
	protected int RADIUS = 5;
	protected boolean isAfraid;
	protected SuperPacmanPlayer memory;
	protected DiscreteCoordinates refuge;
	private Sprite[] afraidSprite;
	private Animation afraidAnimation;
	private GhostHandler handler;
	private int speed;
	private String ghostType;
	
	
	/*
	 * @param: Area the area when the ghost is present, not Null
	 * @param: Orientation: make the sprites and the eyes well placed,  on the area, not Null
	 * @param: DiscreteCoordinates position: the position of the ghost, not Null
	 * @param: DiscreteCoordinates refuge: the spawn position and the position where it  can go if it gets scared.
	 * @param: int speed: it's the speed of the ghost;
	 * @param: String ghostType hepls differentiate between the different ghosts(inky, pinky, blinky, boss)
	 * initialize the animation and the sprites of the ghosts when they got afraid
	 */
	public Ghost(Area area, Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates refuge, int speed, String ghostType) {
		super(area, orientation, position);
		this.refuge = refuge;
		this.ghostType = ghostType;
		isAfraid = false;
		afraidSprite = RPGSprite.extractSprites("superpacman/ghost.afraid", 1,1, 1, this, 16, 16);
		afraidAnimation = new Animation(4, afraidSprite);
		handler = new GhostHandler();
		this.speed = speed;
		
	}
	
	/*
	 * Return the current coordinates of the ghost
	 */
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() { 
		return false;
	}
	
	@Override
	public boolean isCellInteractable(){
		return true;
	}
	
	@Override
	public boolean isViewInteractable() {
		return false;
	}
	
	/*
	 * @param AreaInteractionVisitor: is the nestedClass in the superPacmanPlayer who 
	 * handles the interaction with the Ghost, not null
	 */
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanInteractionVisitor) v).interactWith(this);

	}
	
	@Override
	public void draw(Canvas canvas) {
		afraidAnimation.draw(canvas);
		

	}
	
	public abstract Orientation getNextOrientation();
	
	@Override
	public void update(float deltaTime) {
		/*
		 * Makes the ghost move if it arrives in a a new cell
		 */
		if(!isDisplacementOccurs()) {
			move(speed);
		}
		
		super.update(deltaTime);
		afraidAnimation.update(deltaTime);
	}
	 
	public boolean stepOn() {
		return true;
	}
	
	/*
	 * Returns the ghost type which is a string that helps differentiate between all types of ghosts
	 */
	public String getGhostType() {
		return ghostType;
	}
	
	/*
	 * @param: DiscreteCoordinates (x,y) respectively the width and the height of the position the neighbors of the ghost( including him), not Null.
	 * @param: int RADIUS, is the ray of vision of the ghost, >0
	 * the method return the field of view of the ghost (the cells that it can see)
	 */
	private List<DiscreteCoordinates> fieldOfView() {
		int x = getCurrentMainCellCoordinates().x;
		int y = getCurrentMainCellCoordinates().y;
		int length = RADIUS;
		
		ArrayList<DiscreteCoordinates> FOV = new ArrayList<DiscreteCoordinates>();
		if(x < 5) length = x;
		for(int i = x-length; (i <= x+RADIUS && i < getOwnerArea().getWidth()); i++) {
			for(int j = y-Math.max(length, RADIUS); (j <= y+length && j < getOwnerArea().getHeight()); j++) {
				FOV.add(new DiscreteCoordinates(i, j));
			}
		}
		return FOV;
	}
	
	/*
	 * Make the ghost go back to his main refuge position
	 */
	public void resetPosition() {
		super.setCurrentPosition(refuge.toVector());
		resetMotion();
	}
	
	/*
	 * Method that makes the ghost afraid
	 */
	public void setIsAfraid(boolean isAfraid) {
		this.isAfraid = isAfraid;
	}

	/*
	 * Returns all the coordinates of the ghost's field of view cells
	 * taken from the method fieldOfView()
	 */
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return fieldOfView();
	}
	
	/*
	 * boolean that determine that the player is in the field of view of the ghost or not
	 */
	public boolean playerInFOV() {
		for(int i = 0; i < getFieldOfViewCells().size(); i++) {
			if(memory == null) {
				return false;
			}
			else if(memory.getCurrentCells().get(0).equals(getFieldOfViewCells().get(i))) {
				interactWith(memory);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean wantsCellInteraction() {
		return false;
	}
	
	@Override
	public boolean wantsViewInteraction() {
		return true;
	}
	
	
	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}
	
	/*
	 * method that make the ghost forget the player and his position
	 */
	public void resetMemory() {
		this.memory = null;
	}
	
	/*
	 * method that make the ghosts back to their spawn position 
	 * after they eat the player or they got eaten by the player
	 */
	public void ghostRespawn() {
		getOwnerArea().leaveAreaCells(this, getEnteredCells());
		
		abortCurrentMove();
		setCurrentPosition(refuge.toVector());
		resetMotion();
		
		getOwnerArea().enterAreaCells(this, getCurrentCells());
	}
	
	public boolean getIsAfraid() {
		return isAfraid;
	}
	
	/*
	 * make the ghosts leave the area;
	 */
	public void unregisterGhost() {
		getOwnerArea().leaveAreaCells(this, getEnteredCells());
		abortCurrentMove();
		resetMotion();
		getOwnerArea().unregisterActor(this);
	}
	
	public class GhostHandler implements SuperPacmanInteractionVisitor{
		
		/*
		 * @param SuperpacmanPlayer player: is the player 
		 * who undergo the view interaction, not null
		 */
		public void interactWith(SuperPacmanPlayer player) {
			memory = player;
		} 
	}
	

}
