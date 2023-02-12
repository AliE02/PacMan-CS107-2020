package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;

import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public abstract class AutomaticallyCollectableAreaEntity extends CollectableAreaEntity {
	


	/*
	 *call the superclass constructor.
	 */
	public AutomaticallyCollectableAreaEntity(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		
	} 
	
	/*
	 * method that make the entity leave the area (to simulate the fact that it's collected)
	 */
	protected void isCollected(){
		getOwnerArea().unregisterActor(this);
	}
	
	
	/*
	 * return the current coordinates of the entitiy
	 */
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	
	@Override
	public boolean takeCellSpace() {
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

	@Override
	public abstract void acceptInteraction(AreaInteractionVisitor v);

	@Override
	public abstract void draw(Canvas canvas);

}


