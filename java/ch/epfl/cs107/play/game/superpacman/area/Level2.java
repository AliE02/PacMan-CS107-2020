/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      29 nov. 2020
 * File:      Level2.java
 */

package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Level2 extends SuperPacmanArea {
	
	public final static DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 29);
	
	/*
	 * method that returns the levels title
	 */
	@Override
	public String getTitle() {
		return "superpacman/Level2";
	}
	
	/*
	 * method that create the area, instauring  the door; the keys and  the gates
	 */
	@Override
	protected void createArea() {
		super.createArea();
		
		Door door = new Door("superpacman/Level3",new DiscreteCoordinates(15, 29), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(14,0), new DiscreteCoordinates (15,0) );
		registerActor(door);
		
		Key key1 = new Key(this, Orientation.RIGHT, new DiscreteCoordinates(3,16));
		Key key2 = new Key(this, Orientation.RIGHT, new DiscreteCoordinates(26,16));
		Key key3 = new Key(this, Orientation.RIGHT, new DiscreteCoordinates(2,8));
		Key key4 = new Key(this, Orientation.RIGHT, new DiscreteCoordinates(27,8));
		
		
		registerActor(key1);
		registerActor(key2);
		registerActor(key3);
		registerActor(key4);
		
		Gate[] key1Gates = {new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8, 14), key1),
							new Gate(this, Orientation.DOWN, new DiscreteCoordinates(5, 12), key1),
							new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8, 10), key1),
							new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8, 8), key1)
							};
		
		Gate[] key2Gates = {new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21, 14), key2),
							new Gate(this, Orientation.DOWN, new DiscreteCoordinates(24, 12), key2),
							new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21, 10), key2),
							new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21, 8), key2),
							};
		
		Gate[] key3_4Gates = {new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(10, 2),key3, key4),
							  new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(19, 2),key3, key4),
							  new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(12, 8),key3, key4),
							  new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(17, 8),key3, key4),
							  };
		
		Gate[] diamondGates = {new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(14, 3), this),
							   new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(15, 3), this),
							   };
		
		for(int i = 0; i < 4; i++) {
			registerActor(key1Gates[i]);
			registerActor(key2Gates[i]);
			registerActor(key3_4Gates[i]);
		}
		for(int i = 0; i < 2; i++) {
			registerActor(diamondGates[i]);
		}
		
	}
	
	/*
	 * return the spawn position of the player in the area level 2;
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
