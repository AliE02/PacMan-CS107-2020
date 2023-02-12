package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class Cherry extends AutomaticallyCollectableAreaEntity {
	
	private Sprite sprite;
	
	/*
	 * @param Area: the area when the cherry is present, not Null
	 * @param: Orientation: make the sprite be placed well on the area, not Null
	 * @param DiscreteCoordinates: the position of the cherry, not Null
	 * initialize the cherrie's sprite
	 */
	public Cherry(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		this.sprite = new Sprite("superpacman/cherry", 1.f, 1.f, this);
	}
		
	/* 
	 * @param AreaInteractionVisitor: is the nestedClass in the superPacmanPlayer who handles
	 *  the interaction with the cherry, not null
	 */
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
			((SuperPacmanInteractionVisitor) v).interactWith(this);
				
	}
	
	/*
	 * method that draw the cherry sprite
	 */
	public void draw(Canvas canvas) {
		sprite.draw(canvas);
	}
	
	/*
	 * returns the number of points added to the players score if the cherry is collected
	 */
	public int addedPoints() {
		return 200;
	}

}


