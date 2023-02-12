/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      15 déc. 2020
 * File:      Banana.java
 */

package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class Banana extends AutomaticallyCollectableAreaEntity {

	private Sprite bananaSprite;
	

 
	/*
	 * @param Area: the area when the banana is present, not Null
	 * @param: Orientation: make the sprite be placed well on the area, not Null
	 * @param DiscreteCoordinates: the position of the banana, not Null
	 * initialize the banana's sprite
	 */
	public Banana(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		this.bananaSprite = new Sprite("superpacman/banana", 1.f, 1.f, this);
	}
	
	/*
	 * @param AreaInteractionVisitor: is the nestedClass in the superPacmanPlayer who handles 
	 * the interaction with the cherry, not null
	 */
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
			((SuperPacmanInteractionVisitor) v).interactWith(this);
				
	}
	
	/*
	 * method that draw the banana's sprite
	 */
	public void draw(Canvas canvas) {
		bananaSprite.draw(canvas);
	}

}
