/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      11 déc. 2020
 * File:      Blinky.java
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

public class Blinky extends Ghost {

	private Sprite[][] blinkySprite = RPGSprite.extractSprites("superpacman/ghost.blinky", 2, 1, 1, this, 16, 16, new Orientation[] {Orientation.UP, Orientation.RIGHT , Orientation.DOWN, Orientation.LEFT});
	private Animation[] blinkyanimations = Animation.createAnimations(4, blinkySprite);
	private Animation currentBlinkyAnimation = blinkyanimations[super.getOrientation().ordinal()];
	private final static int SPEED = 6;
	
	/*
	 * call the superclass constructor
	 */
	public Blinky(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position, position, SPEED, "blinky");
	}
	
	/*
	 * update the position of Blinky and it's orientation if the game is not paused
	 * else the game will be paused and the moves of blinky too
	 */
	@Override 
	public void update(float deltaTime) {
		if(!((SuperPacmanArea) getOwnerArea()).getPause()) {
			currentBlinkyAnimation = blinkyanimations[super.getOrientation().ordinal()];
			currentBlinkyAnimation.update(deltaTime);
			
			super.update(deltaTime);
		}
		
		if(!isDisplacementOccurs()) {
			
			orientate(getNextOrientation());
			
		}
		
		currentBlinkyAnimation.update(deltaTime);
		
	}
	
	/*
	 * draw the blinky animation if it is in its normal state
	 * if blinky is afraid; the afraid sprite will be drawed
	 */
	@Override
	public void draw(Canvas canvas) {
		if(super.getIsAfraid()) {
			super.draw(canvas);
		}else{
			currentBlinkyAnimation.draw(canvas);
		}
		
	}

	@Override
	public void interactWith(Interactable other) {
		super.interactWith(other);
		
	}
	
	@Override
	public void ghostRespawn() {
		super.ghostRespawn();
	}
	
	@Override
	public void resetMemory() {
		super.resetMemory();
	}

	/*
	 * this method choose a random orientation through a random integer generator
	 */
	@Override
	public Orientation getNextOrientation() {
		int randomInt = RandomGenerator.getInstance().nextInt(4);
		return Orientation.fromInt(randomInt);
	}

}
