package entities;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Image;

import entities.abstracts.AnimatedEntity;
import entities.abstracts.Entity;
import entities.abstracts.InteractiveEntity;
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
	private int runInterval = 100;
	
	// the current animation being used
	private Animation currentAnim;
	
	/*
	Creates a new player
	*/
	public Player(int x, int y, Game g)
	{
		// sets position and game
		super(x, y, 18, 24, g);
		
		isRunning = false;
		
		// loads sprites
//		SpriteSheet sheet = SpriteStore.get().loadSpriteSheet(
//			"assets/images/linkspritesheet", 
//			9, 
//			9
//		);
		
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
		
		
		this.imgX = (this.w - 32) / 2;
		this.imgY = (this.h - 32) / 2;		
		
		// adds hitbox
		this.addHitbox(2, 2, 14, 20);
		
		// sets to go down for default
		this.currentAnim = this.runDown;
		this.currentSprite = this.currentAnim.getSprite();
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
			
			if (System.currentTimeMillis() - this.attackTime > this.attackDuration) {
				this.isAttacking = false;
			}
			
			this.currentAnim.update();
			
			this.currentSprite = this.currentAnim.getSprite();
			
		} else {

			// checks if the player needs to move
			if (input.isKeyDown(Input.KEY_UP))
			{
				this.tryToMove(this.x, this.y - (this.speed * delta / 1000f));
				
				this.direction = Entity.DIR_UP;
				this.currentAnim = this.runUpShield;
				this.isRunning = true;
			}
			else if (input.isKeyDown(Input.KEY_DOWN))
			{
				this.tryToMove(this.x, this.y + (this.speed * delta / 1000f));
				
				this.direction = Entity.DIR_DOWN;
				this.currentAnim = this.runDownShield;
				this.isRunning = true;
			}
			if (input.isKeyDown(Input.KEY_LEFT))
			{
				this.tryToMove(this.x - (this.speed * delta / 1000f), this.y);
				
				this.direction = Entity.DIR_LEFT;
				this.currentAnim = this.runSideShield;
				this.isRunning = true;
			}
			else if (input.isKeyDown(Input.KEY_RIGHT))
			{
				this.tryToMove(this.x + (this.speed * delta / 1000f), this.y);

				this.direction = Entity.DIR_RIGHT;
				this.currentAnim = this.runSideShield;
				this.isRunning = true;
			}
			
			if (input.isKeyPressed(Input.KEY_SPACE)) {
				
				// interacts with anything if it can, otherwise swings it's sword
				if (!this.tryToInteract()) {
					
					this.swingSword();
					
				}
			} else if (this.isRunning) {
				
				this.currentAnim.update();
				
				this.currentSprite = this.currentAnim.getSprite();
				
			} else {
				
				switch (this.direction) {
					case Entity.DIR_DOWN:
						this.currentAnim = this.standDownShield;
						break;
					case Entity.DIR_UP:
						this.currentAnim = this.standUpShield;
						break;
					case Entity.DIR_LEFT:
					case Entity.DIR_RIGHT:
						this.currentAnim = this.standSideShield;
						break;
				}
				
				this.currentSprite = this.currentAnim.getSprite();
			}
		}
	}
	
	/*
	 * Renders the player
	 */
	public void render(Graphics g) {
		
		Image sprite = this.currentSprite;

		int imgX = (int)this.x + this.w / 2;
		int imgY = (int)this.y + this.h / 2;
		
		if (this.direction == Entity.DIR_LEFT) {
			sprite = sprite.getFlippedCopy(true, false);
			imgX -= this.currentAnim.getOffX();
			
		} else {
			imgX += this.currentAnim.getOffX();
		}
		imgY += this.currentAnim.getOffY();
		
		sprite.drawCentered(
			imgX, 
			imgY);
		
		g.drawRect(this.x, this.y, this.w, this.h);
	}
	
	/*
	 * Sets up to play the sword attack animation 
	 * for whichever direction the player is facing and deals damage to entities
	 */
	private void swingSword() {
		
		// sets up to attack
		this.isAttacking = true;
		this.attackTime = System.currentTimeMillis();
		
		// sets animation to attack
		switch (this.direction) {
			case Entity.DIR_UP: 
				this.currentAnim = this.attackUp;
				break;
			case Entity.DIR_LEFT:
			case Entity.DIR_RIGHT:
				this.currentAnim = this.attackSide;
				break;
			case Entity.DIR_DOWN:
				this.currentAnim = this.attackDown;
				break;
		}
		
		// restarts that animation
		this.currentAnim.restart();
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
			this.x = newX;
			this.y = newY;
		}
		
	}
	
}