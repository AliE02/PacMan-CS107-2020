/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      4 déc. 2020
 * File:      SuperPacmanPlayerStatusGUI.java
 */

package ch.epfl.cs107.play.game.superpacman.actor;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class SuperPacmanPlayerStatusGUI implements Graphics {
	
	private SuperPacmanPlayer player;
	private TextGraphics displayedScore;
	private ImageGraphics[] displayedLife;
	private ImageGraphics youLose;
	private ImageGraphics youWin;
	
	/*
	 * Constructor initiates the player and the displayed life to an array of 5 ImagesGraphics
	 * which represent the hp of the player
	 */
	public SuperPacmanPlayerStatusGUI(SuperPacmanPlayer player) {
		this.player = player;
		displayedLife = new ImageGraphics[5];
	}
	
	
	/*
	 * The method "draw" sets the displayedScore, the displayedLife using the score and the hp of the player
	 * for the displayed life, we draw yellow pacmans for the hp that the player has and gray ones for the hps that 
	 * the player has lost
	 * If the player is loosingi.e(player.getIsLoosing()), we draw youLose and we pause the game
	 * If the player is winning i.e(player.getIsWinning()) we draw youWin and we pause the game
	 */
	@Override
	public void draw(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width/2, height/2));
		
		displayedScore = new TextGraphics("Score : " + Integer.toString(player.getScore()), 0.5f, Color.BLACK, Color.YELLOW, 0.1f, false, false, anchor.add(new Vector(5.5f, height-1.2f)));
		for(int i = 0; i < 5; i++) {
			int state = 64;
			if(i < player.getHp()) state = 0;
			else state = 64;
			
			displayedLife[i] = new ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplay"), 0.8f, 0.8f, new RegionOfInterest(state, 0, 64, 64), anchor.add(new Vector(i, height - 1.375f)), 1, 1);
			
		}
		
		youWin = new ImageGraphics(ResourcePath.getSprite("superpacman/youwin"), 10.f, 10.f, null, anchor.add(new Vector(2, 2)), 1, 1);
		youLose = new ImageGraphics(ResourcePath.getSprite("superpacman/YouLose"), 14.f, 14.f, null, anchor.add(new Vector(0, 0)), 1, 1);
		
		displayedScore.draw(canvas);
		for(int i = 0; i < 5; i++) {
			displayedLife[i].draw(canvas);
		}
		
		if(player.getIsLoosing()) {
			youLose.draw(canvas);
		}
		
		
		if(player.getIsWinning()) {
			youWin.draw(canvas);
		}
		
		
		
	}

}
