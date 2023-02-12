/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      15 déc. 2020
 * File:      Boss.java
 */

package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

public class Boss extends IntelligentGhost {

	private final static int MIN_AFRAID_DISTANCE = 5;
	private final static int MAX_RANDOM_ATTEMPT = 200;
	private final static int SPEED = 4;
	
	private int randomAttemptCounter;
	  
	/*
	 * call the superclass constructor
	 * initialize the boss's sprites and animations
	 * change the radius of the field of view to 15(the other ghosts have a smaller field of view)
	 */
	public Boss(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position, position,SPEED, "boss");
		Sprite[][] bossSprite = RPGSprite.extractSprites("superpacman/ghost.boss", 2, 1, 1, this, 16, 16, new Orientation[] {Orientation.UP, Orientation.RIGHT , Orientation.DOWN, Orientation.LEFT});
		this.ghostSprite = bossSprite;
		this.animations = Animation.createAnimations(4, bossSprite);
		this.currentGhostAnimation = animations[super.getOrientation().ordinal()];
		randomAttemptCounter = 0;
		RADIUS = 15;
	}
 
	/*
	 * call the superclass method
	 * reset the number of random Attempt whenever the Boss make a move 
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}
	
	/* 
	 * set a random target for the Boss when he doesn't have the player in his field of view.
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
		
		int x = RandomGenerator.getInstance().nextInt(getOwnerArea().getHeight());
		int y = RandomGenerator.getInstance().nextInt(getOwnerArea().getWidth());
		while(Math.abs( super.memory.getCurrentCells().get(0).x - x) > MIN_AFRAID_DISTANCE || Math.abs(super.memory.getCurrentCells().get(0).y - y) > MIN_AFRAID_DISTANCE && randomAttemptCounter < MAX_RANDOM_ATTEMPT) {
			x = RandomGenerator.getInstance().nextInt(getOwnerArea().getHeight());
			y = RandomGenerator.getInstance().nextInt(getOwnerArea().getWidth());
			randomAttemptCounter += 1;
			
		}
		this.target = new DiscreteCoordinates(x, y);
	}
	
	
}
