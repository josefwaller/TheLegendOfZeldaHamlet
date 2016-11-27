package entities;

import java.util.ArrayList;

import org.newdawn.slick.Input;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import entities.abstracts.AnimatedEntity;
import entities.abstracts.Entity;
import entities.abstracts.InteractiveEntity;
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
	private int attackDuration = 500;
	
	// when the attack started
	private long attackTime;
	
	// the standing sprites
	private Image standUp;
	private Image standDown;
	private Image standLeft;
	private Image standRight;
	
	// the running animations
	private Animation runUp;
	private Animation runDown;
	private Animation runSide;
	
	// the attacking animations
	private Animation attackUp;
	private Animation attackSide;
	private Animation attackDown;
	
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
		SpriteSheet sheet = SpriteStore.get().loadSpriteSheet(
			"assets/images/linkspritesheet.png", 
			9, 
			9
		);
		
		// loads standing sprites
		this.standUp = sheet.getSprite(2,  0);
		this.standDown = sheet.getSprite(1, 0);
		this.standLeft = sheet.getSprite(3, 0);
		this.standRight = this.standLeft.getFlippedCopy(true, false);
		
		this.currentSprite = standDown;
		
		// saves running sprites in array to be used in animation
		Image[] runDownImages = new Image[8];
		Image[] runUpImages = new Image[8];
		Image[] runSideImages = new Image[6];
		
		// holds attacking sprites in array to be used in animation
		Image[] attackDownImages = new Image[6];
		Image[] attackSideImages = new Image[5];
		Image[] attackUpImages = new Image[5];
		
		for (int i = 0; i < 8; i++) {
			
			runDownImages[i] = sheet.getSprite(i, 1);
			runUpImages[i] = sheet.getSprite(i, 2);
			
			if (i < 6) {

				runSideImages[i] = sheet.getSprite(i, 3);
				
				attackDownImages[i] = sheet.getSprite(i, 4);
				
				if (i < 5) {

					attackSideImages[i] = sheet.getSprite(i, 5);
					attackUpImages[i] = sheet.getSprite(i, 6);
				}
			}
		}
		
		// creates animations from arrays of images
		this.runDown = new Animation(runDownImages, this.runInterval, false);
		this.runUp = new Animation(runUpImages, this.runInterval, false);
		this.runSide = new Animation(runSideImages, this.runInterval, false);
		
		// creates animations for attacking
		this.attackDown = new Animation(attackDownImages, this.attackDuration / 6, false);
		this.attackSide = new Animation(attackSideImages, this.attackDuration / 5, false);
		this.attackUp = new Animation(attackUpImages, this.attackDuration / 5, false);
		
		// sets the default animation to running down
		this.currentAnim = this.runDown;
		
		this.imgX = (this.w - 32) / 2;
		this.imgY = (this.h - 32) / 2;		
		
		// adds hitbox
		this.addHitbox(2, 2, 14, 20);
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
			
			this.currentAnim.update(delta);
			
			this.currentSprite = this.currentAnim.getCurrentFrame();
			
		} else {

			// checks if the player needs to move
			if (input.isKeyDown(Input.KEY_UP))
			{
				this.tryToMove(this.x, this.y - (this.speed * delta / 1000f));
				
				this.direction = Entity.DIR_UP;
				this.currentAnim  = this.runUp;
				this.isRunning = true;
			}
			else if (input.isKeyDown(Input.KEY_DOWN))
			{
				this.tryToMove(this.x, this.y + (this.speed * delta / 1000f));
				
				this.direction = Entity.DIR_DOWN;
				this.currentAnim = this.runDown;
				this.isRunning = true;
			}
			if (input.isKeyDown(Input.KEY_LEFT))
			{
				this.tryToMove(this.x - (this.speed * delta / 1000f), this.y);
				
				this.direction = Entity.DIR_LEFT;
				this.currentAnim = this.runSide;
				this.isRunning = true;
			}
			else if (input.isKeyDown(Input.KEY_RIGHT))
			{
				this.tryToMove(this.x + (this.speed * delta / 1000f), this.y);

				this.direction = Entity.DIR_RIGHT;
				this.currentAnim = this.runSide;
				this.isRunning = true;
			}
			
			if (input.isKeyPressed(Input.KEY_SPACE)) {
				
				// interacts with anything if it can, otherwise swings it's sword
				if (!this.tryToInteract()) {
					
					this.swingSword();
					
				}
			}
			
			if (this.isRunning) {
				
				this.currentAnim.update(delta);
				
				this.currentSprite = this.currentAnim.getCurrentFrame();
				
				if (this.direction == Entity.DIR_RIGHT) {
					this.currentSprite = this.currentSprite.getFlippedCopy(true,  false);
				}
				
			} else {
				switch (this.direction) {
					case Entity.DIR_UP: this.currentSprite = this.standUp; break;
					case Entity.DIR_DOWN: this.currentSprite = this.standDown; break;
					case Entity.DIR_LEFT: this.currentSprite = this.standLeft; break;
					case Entity.DIR_RIGHT: this.currentSprite = this.standRight; break;
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
		
		// finds the correct animation to play
		switch (this.direction) {
			case Entity.DIR_DOWN:
				this.currentAnim = this.attackDown;
				break;
			case Entity.DIR_LEFT:
			case Entity.DIR_RIGHT:
				this.currentAnim = this.attackSide;
				break;
			case Entity.DIR_UP:
				this.currentAnim = this.attackUp;
				break;
		}
		
		// restarts it
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