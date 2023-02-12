package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class Key extends CollectableAreaEntity implements Logic{
	private Sprite sprite;
	private Logic signal;
	
	/*
	 * @param Area: the area when the ghost is present, not Null
	 * @param: Orientation: make the sprite  well placed,  on the area, not Null
	 * @param DiscreteCoordinates position: the position of the key, not Null
	 * initialize the key's sprite
	 * signal is the signal of the key: he change when the key is picked
	 */ 
	public Key(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		this.sprite = new Sprite("superpacman/key", 1.f, 1.f, this);
		signal = Logic.FALSE;
	}
	
	/*
	 * @param AreaInteractionVisitor: is the nestedClass in the superPacmanPlayer 
	 * who handles the interaction with the key, not null
	 */
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanInteractionVisitor) v).interactWith(this);
	}
	 
	/*
	 * method that draw the key's sprite
	 */
	public void draw(Canvas canvas) {
		sprite.draw(canvas);
	}
	
	/*
	 * method that set the key's signal to true 
	 * then make the  key leave the area
	 */
	@Override
	public void isCollected() {
		signal = Logic.TRUE;
		getOwnerArea().unregisterActor(this);
	}
	
	/*
	 * method that return if the signal is On( it's depends on the logic of the key)
	 */
	@Override
	public boolean isOn() {
		if(signal == Logic.TRUE) return true;
		return false;
	}
	
	@Override
	public boolean isOff() {
		return false;
	}
	
	/*
	 * method that get the intensity of key's signal
	 */
	@Override
	public float getIntensity() {
		return 0;
	}
	
	

}


