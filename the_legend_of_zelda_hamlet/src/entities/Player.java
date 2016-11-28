package entities;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import entities.abstracts.AnimatedEntity;
import entities.abstracts.Entity;
import entities.abstracts.InteractiveEntity;
import sprites.SpriteAnimationSet;
import sprites.SpriteSheet;
import sprites.SpriteStore;
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
	private SpriteAnimationSet standUp;
	private SpriteAnimationSet standDown;
	private SpriteAnimationSet standSide;
	private SpriteAnimationSet standUpShield;
	private SpriteAnimationSet standDownShield;
	private SpriteAnimationSet standSideShield;
	
	// the running animations
	private SpriteAnimationSet runUp;
	private SpriteAnimationSet runDown;
	private SpriteAnimationSet runSide;
	private SpriteAnimationSet runUpShield;
	private SpriteAnimationSet runDownShield;
	private SpriteAnimationSet runSideShield;
	
	// the attacking animations
	private SpriteAnimationSet attackUp;
	private SpriteAnimationSet attackDown;
	private SpriteAnimationSet attackSide;
	
	// the time inbetween sprite changes while running
	private int runInterval = 100;
	
	// the current animation being used
	private SpriteAnimationSet currentAnim;
	
	/*
	Creates a new player
	*/
	public Player(int x, int y, Game g)
	{
		// sets position and game
		super(x, y, 18, 24, g);
		
		isRunning = false;
		
		// loads sprites
		SpriteSheet sheet = SpriteStore.get().loadSpriteSheet(
			"assets/images/linkspritesheet", 
			9, 
			9
		);
		
		// loads standing sprite
		this.standDown = new SpriteAnimationSet(sheet, "standdown", 1);
		this.standSide = new SpriteAnimationSet(sheet, "standside", 1);
		this.standUp = new SpriteAnimationSet(sheet, "standup", 1);
		this.standDownShield = new SpriteAnimationSet(sheet, "standdownshield", 1);
		this.standSideShield = new SpriteAnimationSet(sheet, "standsideshield", 1);
		this.standUpShield = new SpriteAnimationSet(sheet, "standupshield", 1);
		
		// creates animations
		this.runUp = new SpriteAnimationSet(sheet, "runup", this.runInterval);
		this.runDown = new SpriteAnimationSet(sheet, "rundown", this.runInterval);
		this.runSide = new SpriteAnimationSet(sheet, "runside", this.runInterval);
		this.runUpShield = new SpriteAnimationSet(sheet, "runupshield", this.runInterval);
		this.runDownShield = new SpriteAnimationSet(sheet, "rundownshield", this.runInterval);
		this.runSideShield = new SpriteAnimationSet(sheet, "runsideshield", this.runInterval);
		
		// these durations are set after, bacuse they depend on the animation's length
		this.attackUp = new SpriteAnimationSet(sheet, "attackup", 0);
		this.attackSide = new SpriteAnimationSet(sheet, "attackside", 0);
		this.attackDown = new SpriteAnimationSet(sheet, "attackdown", 0);
		
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