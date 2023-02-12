package ch.epfl.cs107.play.game.superpacman.actor;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class Diamond extends AutomaticallyCollectableAreaEntity {

	private Sprite sprite;
	
	/*
	 * @param Area: the area when the diamond is present, not Null
	 * @param: Orientation: make the sprite be placed well on the area, not Null
	 * @param DiscreteCoordinates: the position of the diamond, not Null
	 * initialize the diamonds sprite
	 */
	public Diamond(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		this.sprite = new Sprite("superpacman/diamond", 1.f, 1.f, this);
	
	}
		 
	/*
	 * @param AreaInteractionVisitor: is the nestedClass in the superPacmanPlayer who handles 
	 * the interaction with the diamond, not null
	 */
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanInteractionVisitor) v).interactWith(this);
		SuperPacmanArea temporary = ((SuperPacmanArea) super.getOwnerArea());
		temporary.setDiamondsNumber(temporary.getDiamondsNumber()-1);
	}
	
	/*
	 * method that draw the diamond sprite 
	 */
	@Override
	public void draw(Canvas canvas) {
		sprite.draw(canvas);
	}
	
	/*
	 * returns the number of points added to the players score if the diamond is collected
	 */
	public int addedPoints() {
		return 10;
	}



}



