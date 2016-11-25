package entities;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import entities.abstracts.Entity;
import entities.abstracts.InteractiveEntity;
import sprites.SpriteSheet;
import sprites.SpriteStore;
import game.Game;

/*
The main player class.
*/
public class Player extends Entity{

	// the speed at which the player runs
	int speed = 90;
	
	// whether to play the running animation or not
	boolean isRunning;
	
	// the standing sprites
	Image standUp;
	Image standDown;
	Image standLeft;
	Image standRight;
	
	// sprite used if no animation is being used
	Image currentSprite;
	
	// the running animations
	Animation runUp;
	Animation runDown;
	Animation runLeft;
	Animation runRight;
	
	// the time inbetween sprite changes while running
	int runInterval = 100;
	
	// the current animation being used
	Animation currentAnim;
	
	/*
	Creates a new player
	*/
	public Player(int x, int y, Game g)
	{
		// sets position and game
		super(x, y, 16, g);
		
		isRunning = false;
		
		// loads sprites
		SpriteSheet sheet = SpriteStore.get().loadSpriteSheet(
			"assets/images/testspritesheet.png", 
			8, 
			4
		);
		
		// loads standing sprites
		this.standUp = sheet.getSprite(0,  3);
		this.standDown = sheet.getSprite(0, 0);
		this.standLeft = sheet.getSprite(0, 1);
		this.standRight = sheet.getSprite(0, 2);
		
		this.currentSprite = standDown;
		
		// loads running sprites
		Image[] runDownImages = new Image[8];
		Image[] runLeftImages = new Image[7];
		Image[] runUpImages = new Image[8];
		Image[] runRightImages = new Image[7];
		
		for (int i = 0; i < 8; i++) {
			
			runDownImages[i] = sheet.getSprite(i, 0);
			runUpImages[i] = sheet.getSprite(i, 3);
			
			if (i < 7) {

				runLeftImages[i] = sheet.getSprite(i, 1);
				runRightImages[i] = sheet.getSprite(i, 2);
			}
		}
		
		this.runDown = new Animation(runDownImages, this.runInterval, false);
		this.runLeft = new Animation(runLeftImages, this.runInterval, false);
		this.runUp = new Animation(runUpImages, this.runInterval, false);
		this.runRight = new Animation(runRightImages, this.runInterval, false);
		
		this.currentAnim = this.runDown;
		
		// resizes its dimensions based on sprite size
		this.h = (int)(this.w * (this.standUp.getHeight() / (double)this.standUp.getWidth()));
		
	}
	
	/*
	 * Updates the player position
	 * Moves, takes/deals damage, etc
	 */
	public void update(Input input, int delta, boolean[][] blocked)
	{
		
		this.isRunning = false;
		
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
			this.currentAnim = this.runLeft;
			this.isRunning = true;
		}
		else if (input.isKeyDown(Input.KEY_RIGHT))
		{
			this.tryToMove(this.x + (this.speed * delta / 1000f), this.y);

			this.direction = Entity.DIR_RIGHT;
			this.currentAnim = this.runRight;
			this.isRunning = true;
		}
		
		// checks if it is interacting with anything
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			
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
						
						break;
						
					}
				}
				
			}
			
		}
		
		if (this.isRunning) {
			
			this.currentAnim.update(delta);
			
			this.currentSprite = this.currentAnim.getCurrentFrame();
			
		} else {
			switch (this.direction) {
				case Entity.DIR_UP: this.currentSprite = this.standUp; break;
				case Entity.DIR_DOWN: this.currentSprite = this.standDown; break;
				case Entity.DIR_LEFT: this.currentSprite = this.standLeft; break;
				case Entity.DIR_RIGHT: this.currentSprite = this.standRight; break;
			}
		}
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
	
	/*
	 * Draws the player on the screen
	 * 
	 */
	public void render(Graphics g)
	{
		// draws the current sprite
		this.currentSprite.draw((int)this.x, (int)this.y, (int)this.w, (int)this.h);		
	}
}