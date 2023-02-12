/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      29 nov. 2020
 * File:      SuperPacmanArea.java
 */

package ch.epfl.cs107.play.game.superpacman.area;

import java.util.Queue;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.actor.Boss;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public abstract class SuperPacmanArea extends Area implements Logic{
	
	protected Logic signal = Logic.FALSE;
	private int DiamondsNumber = 0;
	private SuperPacmanBehavior behavior;
	private boolean bossBoolean;
	private Boss boss;
	private boolean pause;

    /**
     * Create the area by adding it all actors
     * called by begin method
     * Note it set the Behavior as needed !
     */
    protected void createArea() {
    	behavior.registerActors(this);
    }
    

    @Override
    public final float getCameraScaleFactor() {
        return 15.f;
    }
    
    
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
        	behavior = new SuperPacmanBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            pause = false;
            return true;
        }
        return false;
    }
    
    @Override
    public String getTitle() {
    	return null;
    }
  
    /*
     * call the superclass update method 
     * set the signal of the area to true if no diamonds remain
     * settle the tools to make pause or resume the game
     */
    @Override
    public void update(float deltaTime) {
    	super.update(deltaTime);
		if(noDiamonds()) {
			setSignal(Logic.TRUE);
		}
		
		if(bossBoolean) {
			registerBoss();
		}
		
		Keyboard keyb = this.getKeyboard();
		
		if(keyb.get(Keyboard.SPACE).isDown()) {
			pause = true;
		}
		if(keyb.get(Keyboard.ENTER).isDown()) {
			pause = false;
		}
		
    }
    
    
    
    // method who set the signal to a desired value
	public Logic getSignal() {
		return signal;
	}

	public void setSignal(Logic signal) {
		this.signal = signal;
	}

	public int getDiamondsNumber() {
		return DiamondsNumber;
	}

	public void setDiamondsNumber(int diamondsNumber) {
		DiamondsNumber = diamondsNumber;
	}
	
	
	/*
	 * boolean that indicates that no diamonds are remaining in the area
	 */
	public boolean noDiamonds() {
		return DiamondsNumber <= 0;
	}
	
	/*
	 * method who change the ghosts behavior from normal to scared
	 */
	public void scareBehaviorGhosts() {
		behavior.scareGhosts();
	}
	
	/*
	 * method which makes the ghosts back to their normal state
	 */
	public void resetBehaviorGhostsState() {
		behavior.resetGhostState();
	}
	
	/*
	 * method which makes the ghosts back to their main position 
	 */
	public void resetBehaviorGhostsPosition() {
		behavior.resetGhostPosition();
	}
	/*
	 * method that make all the main ghosts(from the behavior) leave the area
	 */
	public void unregisterBehaviorGhosts() {
		behavior.unregisterGhosts();
	}
	
	public abstract DiscreteCoordinates getSpawnPosition();
	
	/*
	 * @param:DiscreteCoordinates from: determines the position when the movable area entity is located
	 * @param: DiscreteCoordinates to: determines the destination where the entity wants to go
	 * this method give a queue of the necessary orientations to take the shortest path
	 */
	public Queue<Orientation> ori(DiscreteCoordinates from, DiscreteCoordinates to){
		Queue<Orientation> shortestPath = this.behavior.getGraph().shortestPath(from, to);
		return shortestPath;

	}
	
	/*change the nodes signal (in function of if the gates are opened or not)*/
	public void setBehaviorNodeSignal(DiscreteCoordinates coordinates, Logic signal) {
		behavior.setGraphSignal(coordinates, signal);
	}
	
	/*
	 * register the Boss ghost in the area
	 */
	public void registerBoss() {
		boss = new Boss(this, Orientation.RIGHT, new DiscreteCoordinates(20, 20));
		this.registerActor(boss);
	}
	
	/*
	 * Determine if it's the moment to register the boss in the area
	 */
	public void setBossBoolean(Boolean bool) {
		bossBoolean = bool;
	}
	
	/*
	 * Scare the boss
	 */
	public void scareBoss() {
		boss.setIsAfraid(true);
	}
	
	public boolean getPause() {
		return pause;
	}
	
	
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	
    
}
