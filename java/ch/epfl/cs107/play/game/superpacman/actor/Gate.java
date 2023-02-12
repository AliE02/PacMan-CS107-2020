/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      4 déc. 2020
 * File:      Gate.java
 */

package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class Gate extends AreaEntity {
	private Logic signal1;
	private Logic signal2;
	private Logic signal3 = Logic.FALSE;
	private boolean twoKeys;
	private Sprite horizontalGate;
	private Sprite verticalGate;
	
	/*
	 * @param: Area is the area of the gate
	 * @param: DiscreteCoordinates position  is the coordinates of the gate in the grid
	 * @Logic signal is the signal given to the gate which 
	 * determine if the gate is opened or closed
	 * vertical/horizontal gate is the sprites linked to a gate in a vertical/horizontal position
	 */
	public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal1) {
		super(area, orientation, position);
		this.signal1 = signal1;
		horizontalGate = new Sprite("superpacman/gate", 1.f, 1.f, this, new RegionOfInterest(0, 64, 64, 64));
				//new Sprite("superpacman/gate", 1.f, 1.f, this);
		verticalGate = new Sprite("superpacman/gate", 1.f, 1.f, this, new RegionOfInterest(0,0,64,64));
		((SuperPacmanArea)this.getOwnerArea()).setBehaviorNodeSignal(this.getCurrentMainCellCoordinates(), signal1);
		
	}
	
	/*
	 * @param: Area is the area of the gate
	 * @param: DiscreteCoordinates position  is the coordinates of the gate in the grid
	 * @param Logic signal1 is one of the signals given to the gate which 
	 * @param Logic signal2 is the second signal given to the gate 
	 * Logic signal 3(the combination of the two first signals)
	 * determine if the gate is opened or closed
	 * vertical/horizontal gate is the sprites linked respectively to  a gate in a vertical/horizontal position
	 */
	public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal1, Logic signal2) {
		super(area, orientation, position);
		this.signal1 = signal1;
		this.signal2 = signal2;
		this.twoKeys = AND(signal1, signal2);
		if(twoKeys) {
			signal3 = Logic.TRUE;
		}
		horizontalGate = new Sprite("superpacman/gate", 1.f, 1.f, this, new RegionOfInterest(0, 64, 64, 64));
				//new Sprite("superpacman/gate", 1.f, 1.f, this);
		verticalGate = new Sprite("superpacman/gate", 1.f, 1.f, this, new RegionOfInterest(0,0,64,64));
		((SuperPacmanArea)this.getOwnerArea()).setBehaviorNodeSignal(this.getCurrentMainCellCoordinates(), signal3);
		
		
	}
	
	/*
	 * return the coordinates of the gate's position  
	 */
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}
	
	/*
	 * call the superclass update
	 * update the signal(s) linked to the gates
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if(signal2 != null) {
			twoKeys = AND(signal1, signal2);
		}
		if(twoKeys) {
			signal3 = Logic.TRUE;
		}
		
	}

	@Override
	public boolean takeCellSpace() {
		if(signal2 != null) return !twoKeys;
		return (!signal1.isOn());
	}

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanInteractionVisitor) v).interactWith(this);
		
		
	}
	
	/*
	 * draw the gates if the signals required to make them open are off
	 * if the gate is in a veritcal position, this method draw the vertical gate
	 * if the gate is in an horizontal position, the method draw the horizontal gate 
	 */
	@Override
	public void draw(Canvas canvas) {
		if(signal2 != null) {
			if(!twoKeys) {
				if(getOrientation() == Orientation.LEFT || getOrientation() == Orientation.RIGHT) {
					horizontalGate.draw(canvas);
				}
				else {
					verticalGate.draw(canvas);
				}
			}
		}
		else {
			if(!signal1.isOn()) {
				if(getOrientation() == Orientation.LEFT || getOrientation() == Orientation.RIGHT) {
					horizontalGate.draw(canvas);
				}
				else {
					verticalGate.draw(canvas);
				}
			}
		}
		
	}
	
	/*
	 * determine if both the two signals required are on
	 */
	public boolean AND(Logic signal1, Logic signal2) {
		
		return signal1.isOn() && signal2.isOn();
	}

}
