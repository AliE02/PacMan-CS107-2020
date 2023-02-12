/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      29 nov. 2020
 * File:      SuperPacman.java
 */

package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.Level0;
import ch.epfl.cs107.play.game.superpacman.area.Level1;
import ch.epfl.cs107.play.game.superpacman.area.Level2;
import ch.epfl.cs107.play.game.superpacman.area.Level3;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class SuperPacman extends RPG {
	
	public final static float CAMERA_SCALE_FACTOR = 10.f;
	public final static float STEP = 0.05f;
	
	private SuperPacmanPlayer player;
	private final String[] areas = {"superpacman/Level0", "superpacman/Level1", "superpacman/Level2", "superpacman/Level3"};
	private final DiscreteCoordinates[] startingPositions = {Level0.PLAYER_SPAWN_POSITION,
															 Level1.PLAYER_SPAWN_POSITION,
															 Level2.PLAYER_SPAWN_POSITION,
															 Level3.PLAYER_SPAWN_POSITION};

	private int areaIndex;
	
	private void createAreas(){

		addArea(new Level0());
		addArea(new Level1());
		addArea(new Level2());
		addArea(new Level3());

	}

	@Override
	public String getTitle() {
		return "Super Pac-Man";
	}
	
	@Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if(super.begin(window, fileSystem)) {
        	window.getTransform();
        	createAreas();
			areaIndex = 0;
			Area area = setCurrentArea(areas[areaIndex], true);
			player = new SuperPacmanPlayer(area, Orientation.RIGHT, startingPositions[areaIndex]);
			initPlayer(player);
			return true;
		}
		return false;
    }
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if(player.getIsInvulnerable()) scareCurrentAreaGhosts();
		else resetCurrentAreaGhostsState();
		
	}
	
	
	public void scareCurrentAreaGhosts() {
		((SuperPacmanArea) getCurrentArea()).scareBehaviorGhosts();
		
	}
	
	public void resetCurrentAreaGhostsState() {
		((SuperPacmanArea) getCurrentArea()).resetBehaviorGhostsState();
	}
	

}
