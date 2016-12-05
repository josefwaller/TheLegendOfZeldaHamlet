package entities;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Image;

import entities.abstracts.AnimatedEntity;
import entities.abstracts.Entity;
import entities.abstracts.InteractiveEntity;
import entities.abstracts.StaticEntity;
import sprites.AnimationStore;
import sprites.Animation;
import game.Game;

/*
The main player class.
*/
public class Player extends AnimatedEntity{

	// the speed at which the player runs
	private int speed = 90;
	
	// whether to play the running animation or not
	private boolean isRunning;
	
	// whether the player is attacking
	boolean isAttacking;
	
	// how long the attack takes
	private int attackDuration = 300;
	
	// when the attack started
	private long attackTime;
	
	// the standing animations
	private Animation standUp;
	private Animation standDown;
	private Animation standSide;
	private Animation standUpShield;
	private Animation standDownShield;
	private Animation standSideShield;
	
	// the running animations
	private Animation runUp;
	private Animation runDown;
	private Animation runSide;
	private Animation runUpShield;
	private Animation runDownShield;
	private Animation runSideShield;
	
	// the attacking animations
	private Animation attackUp;
	private Animation attackDown;
	private Animation attackSide;
	
	// the time inbetween sprite changes while running
	private int runDuration = 100;
	
	/*
	Creates a new player
	*/
	public Player(int x, int y, Game g)
	{
		// sets position and game
		super(x, y, 18, 24, g);
		
		isRunning = false;
		
		String sheet = "assets/images/linkspritesheet";
		
		// loads standing sprite
		this.standDown = AnimationStore.get().getAnimation(sheet, "standdown");
		this.standSide = AnimationStore.get().getAnimation(sheet, "standside");
		this.standUp = AnimationStore.get().getAnimation(sheet, "standup");
		this.standDownShield = AnimationStore.get().getAnimation(sheet, "standdownshield");
		this.standSideShield = AnimationStore.get().getAnimation(sheet, "standsideshield");
		this.standUpShield = AnimationStore.get().getAnimation(sheet, "standupshield");
		
		// creates animations
		this.runUp = AnimationStore.get().getAnimation(sheet, "runup");
		this.runDown = AnimationStore.get().getAnimation(sheet, "rundown");
		this.runSide = AnimationStore.get().getAnimation(sheet, "runside");
		this.runUpShield = AnimationStore.get().getAnimation(sheet, "runupshield");
		this.runDownShield = AnimationStore.get().getAnimation(sheet, "rundownshield");
		this.runSideShield = AnimationStore.get().getAnimation(sheet, "runsideshield");
		
		// these durations are set after, bacuse they depend on the animation's length
		this.attackUp = AnimationStore.get().getAnimation(sheet, "attackup");
		this.attackSide = AnimationStore.get().getAnimation(sheet, "attackside");
		this.attackDown = AnimationStore.get().getAnimation(sheet, "attackdown");
		
		// sets the attack animation's duration
		this.attackUp.setDuration(this.attackDuration / this.attackUp.getAnimLength());
		this.attackSide.setDuration(this.attackDuration / this.attackSide.getAnimLength());
		this.attackDown.setDuration(this.attackDuration / this.attackDown.getAnimLength());
		
		// adds hitbox
		this.addHitbox(2, 2, 14, 20);
		
		// sets to go down for default
		this.setAnim(this.runDown, this.runDuration);
	}
	
	/*
	 * Updates the player position
	 * Moves, takes/deals damage, etc
	 */
	public void update(Input input, int delta, boolean[][] blocked)
	{
		
		// updates hitboxes
		this.updateHitboxes();
		
		this.isRunning = false;
		
		if (this.isAttacking) {
			
			if (System.currentTimeMillis() - this.attackTime >= this.attackDuration) {
				this.isAttacking = false;
			}
			
			this.animUpdate();
			
		} else {

			// checks if the player needs to move
			if (input.isKeyDown(Input.KEY_LEFT))
			{
				this.tryToMove(this.x - (this.speed * delta / 1000f), this.y);
				this.direction = Entity.DIR_LEFT;
				
				this.setAnim(this.runSideShield, this.runDuration);
				this.isRunning = true;
			}
			else if (input.isKeyDown(Input.KEY_RIGHT))
			{
				this.tryToMove(this.x + (this.speed * delta / 1000f), this.y);

				this.direction = Entity.DIR_RIGHT;
				this.setAnim(this.runSideShield, this.runDuration);
				this.isRunning = true;
			}
			if (input.isKeyDown(Input.KEY_UP))
			{
				this.tryToMove(this.x, this.y - (this.speed * delta / 1000f));
				
				// checks it is not just overwriting an animation that was already set
				// earlier this frame.
				if (!(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_RIGHT))) {
					
					this.direction = Entity.DIR_UP;
					this.setAnim(this.runUpShield, this.runDuration);
				}
				this.isRunning = true;
			}
			else if (input.isKeyDown(Input.KEY_DOWN))
			{
				this.tryToMove(this.x, this.y + (this.speed * delta / 1000f));
				
				if (!(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_RIGHT))) {
					
					this.direction = Entity.DIR_DOWN;
					this.setAnim(this.runDownShield, this.runDuration);
				}
				this.isRunning = true;
			}
			
			if (input.isKeyPressed(Input.KEY_SPACE)) {
				
				// interacts with anything if it can, otherwise swings it's sword
				if (!this.tryToInteract()) {
					
					this.swingSword();
					
				}
			} else if (this.isRunning) {
				
				this.loop = true;
				this.animUpdate();
				
			} else {
				
				switch (this.direction) {
					case Entity.DIR_DOWN:
						this.setAnim(this.standDownShield, this.runDuration);
						break;
					case Entity.DIR_UP:
						this.setAnim(this.standUpShield, this.runDuration);
						break;
					case Entity.DIR_LEFT:
					case Entity.DIR_RIGHT:
						this.setAnim(this.standSideShield, this.runDuration);
						break;
				}
			}
		}
	}
	
	/*
	 * Sets up to play the sword attack animation 
	 * for whichever direction the player is facing and deals damage to entities
	 */
	private void swingSword() {
		
		// sets up to attack
		this.isAttacking = true;
		this.attackTime = System.currentTimeMillis();
		this.loop = false;
		
		// sets animation to attack
		switch (this.direction) {
			case Entity.DIR_UP: 
				this.setAnim(
					this.attackUp, 
					this.attackDuration / this.attackUp.getAnimLength());
				break;
			case Entity.DIR_LEFT:
			case Entity.DIR_RIGHT:
				this.setAnim(
					this.attackSide, 
					this.attackDuration / this.attackSide.getAnimLength());
				break;
			case Entity.DIR_DOWN:
				this.setAnim(
					this.attackDown, 
					this.attackDuration / this.attackDown.getAnimLength());
				break;
		}
	}
	
	/*
	 * Called when the player hits space. Returns
	 * true if there is an object the player interacts with,
	 * and false if there is not
	 */
	private boolean tryToInteract() {
		
		// the position it should check for the interactive
		int x = (int) this.x + this.w / 2;
		int y = (int) this.y + this.h / 2;
		
		// changes depending on direction
		switch (this.direction) {
			case Entity.DIR_UP:
				y = (int) (this.y - this.h / 2);
				break;
			case Entity.DIR_DOWN:
				y = (int) (this.y + this.h * 3 / 2);
				break;
			case Entity.DIR_LEFT:
				x = (int) (this.x - this.w / 2);
				break;
			case Entity.DIR_RIGHT:
				x = (int) (this.x + this.h * 3 / 2);
				break;
		}
		
		// gets all the interactive objects
		ArrayList<InteractiveEntity> objs = this.game.getInteractiveObjects();
		
		for (int i = 0; i < objs.size(); i++) {
			InteractiveEntity obj = objs.get(i);
			
			// checks if they are within range to interact with
			if (obj.getX() < x && obj.getX() + obj.getW() > x) {
				if (obj.getY() < y && obj.getY() + obj.getH() > y) {
					
					// interacts with it
					obj.onPlayerInteract();
					
					return true;
					
				}
			}
			
		}
		
		return false;
		
	}
	
	/*
	 * Checks if the new position is valid.
	 * If so, moves the player.
	 * If not, doesn't move the player
	 */
	private void tryToMove(float newX, float newY) {
		
		if (!this.game.isBlocked(newX, newY, this.w, this.h)) {
			
			boolean blocked = false;
			
			StaticEntity[] objs = this.game.getObjects();
			
			for (int i = 0; i < objs.length; i++) {
				
				StaticEntity obj = objs[i];
				
				if (obj.getIsSolid()) {
					
					// records the old position in case it hits the entity
					float oldX = this.x;
					float oldY = this.y;
					
					// sets position
					this.x = newX;
					this.y = newY;
					
					// updates hitboxes
					this.updateHitboxes();
					
					// checks if it collides
					if (this.collidesWithEntity(obj)) {
						
						// reverts back to old position
						blocked = true;
						this.x = oldX;
						this.y = oldY;
						
					}
				}
			}
			
			if (!blocked) {

				this.x = newX;
				this.y = newY;
			}
		}
		
	}
	
}