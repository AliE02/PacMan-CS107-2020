package ch.epfl.cs107.play.game.superpacman.handler;

import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.Banana;
import ch.epfl.cs107.play.game.superpacman.actor.Bonus;
import ch.epfl.cs107.play.game.superpacman.actor.Cherry;
import ch.epfl.cs107.play.game.superpacman.actor.Diamond;
import ch.epfl.cs107.play.game.superpacman.actor.Energy;
import ch.epfl.cs107.play.game.superpacman.actor.Ghost;
import ch.epfl.cs107.play.game.superpacman.actor.Key;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;

public interface SuperPacmanInteractionVisitor extends RPGInteractionVisitor {

    /// Add Interaction method with all non Abstract Interactable

    /**
     * Simulate and interaction between RPG Interactor and a Door
     * @param door (Door), not null
     */
    default void interactWith(Door door){
         //by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a Sign
     * @param sign (Sign), not null
     */
    //default void interactWith(Sign sign){
        // by default the interaction is empty
    //}
    
    default void interactWith(SuperPacmanPlayer player) {
    	
    }
    
    default void interactWith(Cherry cherry) {
    	
    }
    
    default void interactWith(Diamond diamond) {
    	
    }
    
    default void interactWith(Key key) {
    	
    }
    
    default void interactWith(Ghost ghost) {
    	
    }
    
    default void interactWith(Bonus bonus) {
    	
    }
    
    default void interactWith(Banana banana) {
    	
    }
    
    default void interactWith(Energy energy) {
    	
    }
	

}

