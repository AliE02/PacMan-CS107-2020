/*
 * Author:    Ali Essonni / Mohamed Othmane Idrissi Oudghiri
 * Date:      29 nov. 2020
 * File:      SuperPacmanPlayer.java
 */

package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class SuperPacmanPlayer extends Player {
	
	
	
	private Sprite[][] sprites = RPGSprite.extractSprites("superpacman/pacman", 4, 1, 1, this, 64, 64, new Orientation[] {Orientation.DOWN, Orientation.LEFT , Orientation.UP, Orientation.RIGHT});
	private SuperPacmanPlayerHandler handler;
	private boolean invulnerable;
	private boolean isWinning;
	private boolean isLoosing;
	private static float bonusTimer;
	private static float bananaTimer;
	private Orientation desiredOrientation = Orientation.RIGHT;
	private Animation[] animations = Animation.createAnimations(SPEED/2, sprites);
	private Animation currentAnimation;
	private int bossNumber = 1;
	private int collectedDiamonds = 0;
    private final static int SPEED = 4;
    private int currentSpeed = SPEED;
    private int score;
    private int hp;
    private SuperPacmanPlayerStatusGUI pacmanStatus;
    private List<DiscreteCoordinates> spawnPosition;
    
    /* @param Area: the area when the player is present, not Null
	 * @param: Orientation: make the sprites well placed on the area, not Null
	 * @param DiscreteCoordinates position: the position of the player, not Null
	 * int hp is the number of lives the player have in the game (initialized to 3)
	 * int score is the number of points the player have in the game 
	 * SuperPacmanStatus is the graphic image of his hp's and his score
	 * handler is an instance of the nested class SuperpacmanlayerHandler who handles the interactions
	 */
	public SuperPacmanPlayer(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		invulnerable = false;
		bonusTimer = 0.f;
		desiredOrientation = orientation;
		handler = new SuperPacmanPlayerHandler();
		hp = 3;
		score = 0;
		isWinning = false;
		System.out.println("Constructeur SuperPacmanPlayer fonctionne? : oui");
		pacmanStatus = new SuperPacmanPlayerStatusGUI(this);

		resetMotion();
	}
	
	
	 @Override
	 public void update(float deltaTime) {
		
		desiredOrientation = desiredOrientation();
		List<DiscreteCoordinates> comingCellCoordinates;
		
		/*
		 * if the user don't press any direction button, the player will continue in his way until he reach a wall
		 * if the user press a direction button, the player choose a direction and he will follow it in the next update 
		 * (his animation also will change the direction)
		 */
		if(!((SuperPacmanArea) getOwnerArea()).getPause()) {
			if(desiredOrientation == null) {
				comingCellCoordinates = Collections.singletonList(getCurrentMainCellCoordinates().jump(new Vector(0.f, 0.f)));
				currentAnimation = animations[super.getOrientation().ordinal()];
			}else {
				comingCellCoordinates = Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector()));
				currentAnimation = animations[super.getOrientation().ordinal()];
			}
			
			currentAnimation.update(deltaTime);
			
			/*
			 * if the player just arrived in a cell (when he can enter in it) he can change 
			 * his orientation or keep the previous one
			 */
			if(!isDisplacementOccurs()) {
				if(getOwnerArea().canEnterAreaCells(this, comingCellCoordinates)) {				
					orientate(desiredOrientation);
					
				}		
				move(currentSpeed);
			}
		}
		
		
		
		setIsInvulnerable(false); //determine if the player is invulnerable or not

		decrementGhostTimer(deltaTime); //reduce the time of invulnerability if it's positive
		decrementBananaTimer(deltaTime); //reduce the time of slowness if the timer is positive


		//when the timer is 0, the speed of the player come back to its initial value
		if(bananaTimer <= 0) {
			currentSpeed = SPEED;
		}
		
		/*method to force the display of only one boss if 
		 * 550 diamonds are collected
		 */
		if(bossNumber > 0 && collectedDiamonds >= 1200 ) {
			bossNumber -= 1;
			registerAreaBoss();
		}
		
		/*set the condition to scare the boss if all the
		 * Level2 are collected
		 */
		if(((SuperPacmanArea)getOwnerArea()).getSignal() == Logic.TRUE && collectedDiamonds >= 1200) {
			scareAreaBoss();
			setIsInvulnerable(true);
		}
		
		/*
		 * While the game is not paused, call the method update of the superClass
		 */
		if(!((SuperPacmanArea) getOwnerArea()).getPause()) {
			super.update(deltaTime);
	 	}
	 }
	 
	/*
	 * Orientate or Move this player in the given orientation if the given button is down
	 * @param orientation (Orientation): given orientation, not null
	 * @param b (Button): button corresponding to the given orientation, not null
	 */
	 protected Orientation desiredOrientation() {
		 Keyboard keyboard = getOwnerArea().getKeyboard();
		 if(keyboard.get(Keyboard.LEFT).isDown()){
			 return Orientation.LEFT;
		 }else if(keyboard.get(Keyboard.UP).isDown()) {
			 return Orientation.UP;
		 }else if(keyboard.get(Keyboard.RIGHT).isDown()) {
			 return Orientation.RIGHT;
		 }else if(keyboard.get(Keyboard.DOWN).isDown()) {
			 return Orientation.DOWN;
		 }
		 return null;
	 }
	 

	 /**
	  * * Leave an area by unregister this player
	  * */
	 public void leaveArea(){
		 getOwnerArea().unregisterActor(this);
	 }
	 
	//draw the animation of the superpacman player
	public void draw(Canvas canvas) {
		currentAnimation.draw(canvas);
		pacmanStatus.draw(canvas);
		
	}
	
	/*
	 * determine that no one can pass through the player
	 */
	@Override
	public boolean takeCellSpace() {
		return true;
	}
	
	/*
	 * determine that the player doesn't accept cell interaction
	 */
	@Override
	public boolean isCellInteractable() {
		return false;
	}
	
	/*
	 * determine that the player accept view interactions
	 */
	@Override
	public boolean isViewInteractable() {
		return true;
	}
	
	/*
	 * returns the player current coordinates
	 */
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}
	
	  
	/* 
	 * show the way how the player accept view interactions
	 */
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanInteractionVisitor)v).interactWith(this);
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return null;
	}
	


	/*
	 * show that the player want to handle cell interactions
	 */
	@Override
	public boolean wantsCellInteraction() {
		return true;
	}
	
	/* 
	 * show that the player doesn't want to handle view interactions
	 */
	@Override
	public boolean wantsViewInteraction() {
		return false;
	}
	
	/* 
	 * show the way how the player handle cell interactions
	 */
	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}
	
	/*
	 * return the score
	 */
	public int getScore() {
		return score;
	}
	
	/* 
	 * return the number of hp the player got
	 */
	public int getHp() {
		return hp;
	}
	
	/*
	 * set the score to the value that we want
	 */
	public void setScore(int i) {
		score = i;
	}
	
	/*
	 * set the invulnerability of the player
	 */
	public void setIsInvulnerable(boolean bool) {
		if(bool) {
			this.invulnerable = true;
		}else{
			this.invulnerable = (bonusTimer > 0);
		}
	}
	
	/*
	 * return if the player is invulnerable or not
	 */
	public boolean getIsInvulnerable() {
		return invulnerable;
	}
	
	/* 
	 * @param: float deltaTime is the same as the delta time of the update
	 * this method will decrement the ghost timer in every update(if the timer is positive)
	 */
	public void decrementGhostTimer(float deltaTime) {
		if(bonusTimer > 0) bonusTimer -= deltaTime;
	}
	
	/*
	 *  @param: float deltaTime is the same as the delta time of the update
	 * this method will decrement the banana timer in every update(if the timer is positive)
	 */
	public void decrementBananaTimer(float deltaTime) {
		if(bananaTimer > 0) bananaTimer -= deltaTime;
	}
	
	/*
	 * set the hp to the value we want
	 */
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public boolean getIsWinning() {
		return isWinning;
	}
	
	public boolean getIsLoosing() {
		return isLoosing;
	}
	
	/*
	 * this method simulate the fact that the player is eaten by the ghosts 
	 * the player leaves the area then return in it in his spawn position
	 * the ghost also go to their spawn position
	 */
	public void isEaten() {
		if(hp > 0) {
			setHp( hp -1);
			getOwnerArea().leaveAreaCells(this, getEnteredCells());
			abortCurrentMove();
			resetMotion();
			setCurrentPosition(((SuperPacmanArea) getOwnerArea()).getSpawnPosition().toVector());
			
			getOwnerArea().enterAreaCells(this, getCurrentCells());
			
			((SuperPacmanArea) getOwnerArea()).resetBehaviorGhostsPosition();
		}
	}
	
	/*
	 * this method register the boss on the area under some  conditions
	 * 1200 diamonds have to be collected since the beginning of the game
	 * once the boss is registered, the other main ghosts are unregistered and leave the area
	 */
	public void registerAreaBoss() {
		((SuperPacmanArea) getOwnerArea()).setBossBoolean(true);
		((SuperPacmanArea) getOwnerArea()).registerBoss();
		((SuperPacmanArea) getOwnerArea()).unregisterBehaviorGhosts();
		((SuperPacmanArea) getOwnerArea()).setBossBoolean(false);
		
	}
	
	/* 
	 * this method serve to make the boss afraid
	 */
	public void scareAreaBoss() {
		((SuperPacmanArea) getOwnerArea()).scareBoss();
	}
	
	/*
	 * this nested class handle all the active interactions between the player and the other interactables actors
	 */
	public class SuperPacmanPlayerHandler  implements SuperPacmanInteractionVisitor{
		
		/*
		 * set the interaction between the player and the door 
		 * when the player reach a door, he goes to the next level,
		 * the spawn position become the spawn position in the new level
		 */
		@Override
		public void interactWith(Door door){
			setIsPassingADoor(door);
			spawnPosition = getCurrentCells();
			
	    }
		
		/*
		 * set the interaction between the player and the bonus 
		 * @param Bonus: is the entity bonus
		 * when the player eats the bonus, he becomes invulnerable towards the ghosts while the timer given by the bonus is positive
		 */
		public void interactWith(Bonus bonus) {
			bonusTimer = 10.f;
			setIsInvulnerable(false);
		}
		
		/*
		 * set the interaction between the player and cherries
		 * @param cherry: is the entity cherry 
		 * the cherry is collected (it disappears from the area)
		 * Cherry Points: the corresponding points are added to the score */
		@Override
		public void interactWith(Cherry cherry) {
			setScore(score + cherry.addedPoints());
			cherry.isCollected();
		}
		
		/*
		 * set the interaction between the player and diamonds
		 * 	the diamond is collected (it disappears from the area)
		 * the corresponding points are added to the score */
		@Override
		public void interactWith (Diamond diamond) {
			setScore(score + diamond.addedPoints());
			diamond.isCollected();
			collectedDiamonds += 1;
		}
		
		/*
		 *  set the interaction between the player and the key
		 *  the key is collected (it disappears from the area)
		 */
		@Override
		public void interactWith(Key key) {
			key.isCollected();
			
		}
		
		 /*
		  * @param Ghost ghost: is the ghost with whom the player interact, if he is invulnerable the player 
		  * will eat the ghost, else the ghost will eat the player
		  * In case the ghost is a boss, the condition to make it afraid is different
		  * (all the diamonds of the area have to be collected)
		  */
		@Override
		public void interactWith(Ghost ghost) {
			if(invulnerable) {
				if(ghost.isAfraid && ghost.getGhostType() == "boss") {
					ghost.unregisterGhost();
					isWinning = true;
					((SuperPacmanArea) getOwnerArea()).setPause(true);
				}else if(!ghost.isAfraid && ghost.getGhostType() == "boss"){
					isEaten();
				}else {
					score += Ghost.GHOST_SCORE;
					ghost.ghostRespawn();
					ghost.resetMemory();
				}

			}else {
				isEaten();
			}
			
			if(hp <= 0) {
				isLoosing = true;
				((SuperPacmanArea)getOwnerArea()).setPause(true);
			}
		}
		
		/*
		 * set the interaction between the player and the banana
		 * the player's speed reduces, he became slower  during a limited time (the value of banana timer)
		 *  the banana is collected (it disappears from the area)
		 */
		@Override
		public void interactWith(Banana banana) {
			bananaTimer = 6.f;
			currentSpeed = currentSpeed+3;
			banana.isCollected();
			
		}
		
		/*
		 * set the interaction between the player and the energy
		 * the player's hps increase by one
		 *  the energy is collected (it disappears from the area)
		 */
		@Override
		public void interactWith(Energy energy) {
			if(hp < 5) hp += energy.addedHp();
			energy.isCollected();
		}

	}
	
	
	
	
	
}