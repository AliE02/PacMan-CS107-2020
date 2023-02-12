/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      11 déc. 2020
 * File:      Pinky.java
 */

package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

public class Pinky extends IntelligentGhost {

	private final static int MIN_AFRAID_DISTANCE = 5;
	private final static int MAX_RANDOM_ATTEMPT = 200;
	private final static int SPEED = 6;
	private int randomAttemptCounter;
	
	 
	/*
	 * call the superClass method 
	 * initialize the sprites of Inky and its animations
	 * initialize the number of random attempts to 0;
	 */
	public Pinky(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position, position, SPEED, "pinky");
		Sprite[][] pinkySprite = RPGSprite.extractSprites("superpacman/ghost.pinky", 2, 1, 1, this, 16, 16, new Orientation[] {Orientation.UP, Orientation.RIGHT , Orientation.DOWN, Orientation.LEFT});
		this.ghostSprite = pinkySprite;
		this.animations = Animation.createAnimations(4, pinkySprite);
		this.currentGhostAnimation = animations[super.getOrientation().ordinal()];
		randomAttemptCounter = 0;
	}
	 
	/*
	 * call the superclass method
	 * reset the number of random Attempt whenever the Pinky make a move 
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		randomAttemptCounter = 0;
		
	}
	
	/* 
	 * set a random target for Pinky when he doesn't have the player in his field of view.
	 */
	@Override
	protected void setRandomTarget() {
		int x = RandomGenerator.getInstance().nextInt(getOwnerArea().getHeight());
		int y = RandomGenerator.getInstance().nextInt(getOwnerArea().getWidth());
		this.target = new DiscreteCoordinates(x, y);
	}
	
	/*
	 * when the ghost is scared he try to find randomly a way to move  away from the player (at least the minimum afraid distance)
	 * whenever he try a random attempt, the counter increase by 1
	 * he can't do more than 200 attempts, once this maximum number is reached, the ghost chose an absolute random move
	 */
	@Override
	protected void setAfraidTarget() {
		int x;
		int y;
		
		do {
			x = RandomGenerator.getInstance().nextInt(getOwnerArea().getHeight());
			y = RandomGenerator.getInstance().nextInt(getOwnerArea().getWidth());
			randomAttemptCounter += 1;
		}while(Math.abs( memory.getCurrentCells().get(0).x - x) > MIN_AFRAID_DISTANCE || Math.abs(memory.getCurrentCells().get(0).y - y) > MIN_AFRAID_DISTANCE && randomAttemptCounter < MAX_RANDOM_ATTEMPT);
		
		this.target = new DiscreteCoordinates(x, y);
		
	}
	
}
