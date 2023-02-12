/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      29 nov. 2020
 * File:      Level0.java
 */

package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;


public class Level0 extends SuperPacmanArea{
	
	public final static DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(10, 1);
	
	//method that returns the levels title
	@Override
	public String getTitle() {
		return "superpacman/Level0";
	}
	

	/*
	 * method that create the area, instauring the door, the key and the gate 
	 */
	@Override
	protected void createArea() {
		System.out.println("createArea de Level0 fonctionne? : oui");
		super.createArea();
		Door door = new Door("superpacman/Level1",Level1.PLAYER_SPAWN_POSITION, Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(5,9), new DiscreteCoordinates (6,9) );
		registerActor(door);
		Key key = new Key(this, Orientation.RIGHT, new DiscreteCoordinates(3,4));
		registerActor(key);
		
		Gate gate1 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(5, 8), key);
		Gate gate2 = new Gate(this, Orientation.LEFT, new DiscreteCoordinates(6, 8), key);
		registerActor(gate1);
		registerActor(gate2);
		
		
		
	}
	
	/*
	 * return the spawn position of the player in the area level 0;
	 */
	@Override
	public DiscreteCoordinates getSpawnPosition() {
		return PLAYER_SPAWN_POSITION;
	}


	@Override
	public boolean isOn() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isOff() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public float getIntensity() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
}
