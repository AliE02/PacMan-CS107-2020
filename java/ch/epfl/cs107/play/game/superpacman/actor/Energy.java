/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      15 déc. 2020
 * File:      Energy.java
 */

package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class Energy extends AutomaticallyCollectableAreaEntity {
	
	private Sprite energySprite;
	
	/*
	 * @param Area: the area when the energy is present, not Null
	 * @param: Orientation: make the sprite be placed well on the area, not Null
	 * @param DiscreteCoordinates: the position of the energy, not Null
	 * initialize the energy's sprite (a heart)
	 */
	public Energy(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		this.energySprite = new Sprite("superpacman/energy", 1.f, 1.f, this);
	}
	
	
	/*
	 * @param AreaInteractionVisitor: is the nestedClass in the superPacmanPlayer who handles the interaction 
	 * with the energy, not null
	 */
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
			((SuperPacmanInteractionVisitor) v).interactWith(this);
				
	}
	
	/* 
	 * method that draw the energy's sprite
	 */
	public void draw(Canvas canvas) {
		energySprite.draw(canvas);
	}
	
	
	public int addedHp() {
		return 1;
	}

}
