/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      29 nov. 2020
 * File:      Level1.java
 */

package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Level1 extends SuperPacmanArea{
	
	public final static DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 6);
	
	//method that returns the levels title
	@Override
	public String getTitle() {
		return "superpacman/Level1";
	}
	
	/*
	 * method that create the area, instauring the door, the key and the gate 
	 */
	@Override
	protected void createArea() {
		super.createArea();
		Door door = new Door("superpacman/Level2",new DiscreteCoordinates(15, 29), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(14,0), new DiscreteCoordinates (15,0) );
		registerActor(door);
		
		Gate gate1 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(14, 3), this);
		Gate gate2 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(15, 3), this);
		registerActor(gate1);
		registerActor(gate2);
		
	}
	
	/*
	 * return the spawn position of the player in the area level 1;
	 */ 
	@Override
	public DiscreteCoordinates getSpawnPosition() {
		return PLAYER_SPAWN_POSITION;
	}

	//return if the signal is On in function of the keys value 
	@Override
	public boolean isOn() {
		return (super.getSignal() == TRUE);
	}


	@Override
	public boolean isOff() {
		return false;
	}


	@Override
	public float getIntensity() {
		return 0;
	}
	
	
}
