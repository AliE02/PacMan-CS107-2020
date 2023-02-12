/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      16 déc. 2020
 * File:      IntelligentGhost.java
 */

package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

public abstract class IntelligentGhost extends Ghost {
	
	protected DiscreteCoordinates target;
	protected Sprite[][] ghostSprite = RPGSprite.extractSprites("superpacman/ghost.inky", 2, 1, 1, this, 16, 16, new Orientation[] {Orientation.UP, Orientation.RIGHT , Orientation.DOWN, Orientation.LEFT});
	protected Animation[] animations = Animation.createAnimations(4, ghostSprite);
	protected Animation currentGhostAnimation;

	/*
	 * Calls the superclass Constructor, i.e(Ghost)
	 */
	public IntelligentGhost(Area area, Orientation orientation, DiscreteCoordinates position,
			DiscreteCoordinates refuge, int speed, String ghostType) {
		super(area, orientation, position, refuge, speed, ghostType);
	}
	
	/*
	 * call the superclass method 
	 * update the animations of the ghost 
	 * determine the conditions of displacement that depends of the player's presence or not in the ghost's field of view 
	 * and the ghost's state (afraid or not)
	 * make the ghost choose an orientation whenever he comes to a new cell
	 */
	@Override 
	public void update(float deltaTime) {
		if(!((SuperPacmanArea) getOwnerArea()).getPause()) {
			super.update(deltaTime);
			currentGhostAnimation = animations[super.getOrientation().ordinal()];
			currentGhostAnimation.update(deltaTime);
		}
		
		
		if(!isAfraid) {
			if(playerInFOV()) {
				setPlayerTarget();
			}else {
				setRandomTarget();
			}
		}else {
			if(playerInFOV()) {
				setAfraidTarget();
			}else {
				setRandomTarget();
			
			}
	}
		
		if(!isDisplacementOccurs()) {
			orientate(getNextOrientation());
		}
		
	}
	
	/*
	 * Method that draw the intelligent ghost animation in its normal state, but draw the common afraid animation if the ghost get afraid
	 */
	@Override
	public void draw(Canvas canvas) {
		if(super.getIsAfraid()) {
			super.draw(canvas);
		}else{
			currentGhostAnimation.draw(canvas);
		}
	}
	
	/*
	 * method that return the required orientations to accede to a target(if it exists)
	 * or that return a random orientation
	 */
	@Override
	public Orientation getNextOrientation() {
		if(((SuperPacmanArea) getOwnerArea()).ori(getCurrentMainCellCoordinates(), target) != null){
			return ((SuperPacmanArea)getOwnerArea()).ori(getCurrentMainCellCoordinates(), target).poll();
		}
		return Orientation.fromInt(RandomGenerator.getInstance().nextInt(4));
	}
	
	@Override
	public void interactWith(Interactable other) {
		super.interactWith(other);
		
	}
	
	
	public SuperPacmanPlayer getMemory() {
		return memory;
	}
	
	/*
	 * Will be explained in the subclasses
	 */
	protected abstract void setRandomTarget();
	
	/*
	 * Will be explained in the subclasses
	 */
	protected abstract void setAfraidTarget();
	
	/*
	 * when the ghost is not scared he follows the player whenever he goes
	 */
	protected void setPlayerTarget() {
		this.target = memory.getCurrentCells().get(0);
	}

}
