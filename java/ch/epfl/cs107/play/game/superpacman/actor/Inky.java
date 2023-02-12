/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      11 déc. 2020
 * File:      Inky.java
 */

package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

public class Inky extends IntelligentGhost {

	private final static int MAX_DISTANCE_WHEN_SCARED = 5;
	private final static int MAX_DISTANCE_WHEN_NOT_SCARED = 10;
	private final static int SPEED = 6;
	
	/*
	 * call the superClass method 
	 * initialize the sprites of Inky and its animations
	 */
	public Inky(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position, position, SPEED, "inky");
		Sprite[][] inkySprite = RPGSprite.extractSprites("superpacman/ghost.inky", 2, 1, 1, this, 16, 16, new Orientation[] {Orientation.UP, Orientation.RIGHT , Orientation.DOWN, Orientation.LEFT});
		this.ghostSprite = inkySprite;
		this.animations = Animation.createAnimations(4, inkySprite);
		this.currentGhostAnimation = animations[super.getOrientation().ordinal()];
		
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}
	
	/* set a random target for Inky when he doesn't have the player in his field of view.
	 * Inky still near to his refuge position
	 */
	@Override
	protected void setRandomTarget() {
		int x =  RandomGenerator.getInstance().nextInt(Math.abs(MAX_DISTANCE_WHEN_NOT_SCARED));
		int y =  RandomGenerator.getInstance().nextInt(Math.abs(refuge.y -  MAX_DISTANCE_WHEN_NOT_SCARED));
		this.target = new DiscreteCoordinates(x, y);
	}
	
	/*
	 * when Inky is scared he try randomly to return as nearly as possible to his refuge position 
	 */
	@Override
	protected void setAfraidTarget() {
		
		int x = RandomGenerator.getInstance().nextInt(getOwnerArea().getHeight());
		int y = RandomGenerator.getInstance().nextInt(getOwnerArea().getWidth());
		
		if((x >= refuge.x - MAX_DISTANCE_WHEN_SCARED) && (x <= refuge.x + MAX_DISTANCE_WHEN_SCARED) && (y >= refuge.y - MAX_DISTANCE_WHEN_SCARED) && (y <= refuge.y + MAX_DISTANCE_WHEN_SCARED)){
			this.target = new DiscreteCoordinates(x, y);
		}
		
	}
	

}
