package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Positionable;
import ch.epfl.cs107.play.window.Canvas;

public class Bonus extends AutomaticallyCollectableAreaEntity {
	
	public static float BONUS_TIMER;
	Positionable parent; 
	private Sprite[] sprites;
	private Animation animation;
	
	/*
	 * @param Area: the area when the bonus is present, not Null
	 * @param: Orientation: make the animation be placed well on the area, not Null
	 * @param DiscreteCoordinates: the position of the bonus, not Null
	 * initialize the bonus sprites and animations
	 */
	public Bonus(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		sprites = RPGSprite.extractSprites("superpacman/coin", 4, 1, 1, parent, position.toVector(), 16, 16);
		animation = new Animation(2, sprites, true);
	}
	 
	/*
	 * call the superclass method
	 *updtate the animation of the bonus
	 */
	@Override
	public void update(float deltaTime) {
		if(!((SuperPacmanArea) getOwnerArea()).getPause()) {
			super.update(deltaTime);
			animation.update(deltaTime);
	 	}
		
	}
	
	/*
	 * method that draw the animation of the bonus
	 */
	@Override
	public void draw(Canvas canvas) {
		animation.draw(canvas);
		
	}
	
	/*
	 * @param AreaInteractionVisitor: is the nestedClass in the superPacmanPlayer who 
	 * handles the interaction with the bonus, not null
	 */
	public void acceptInteraction(AreaInteractionVisitor Pacman) {
		((SuperPacmanInteractionVisitor) Pacman).interactWith(this);
		
		this.isCollected();
		
	}
		
}
	



