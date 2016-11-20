package entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Color;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import sprites.SpriteSheet;
import sprites.SpriteStore;

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
	int runInterval = 150;
	
	// the current animation being used
	Animation currentAnim;
	
	/*
	Creates a new player
	*/
	public Player(int x, int y)
	{
		// sets position
		super(x, y, 16, 1);
		
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
	public void update(Input input, int delta)
	{
		
		this.isRunning = false;
		
		// checks if the player needs to move
		if (input.isKeyDown(Input.KEY_UP))
		{
			this.y -= this.speed * delta / 1000f;
			
			this.direction = Entity.DIR_UP;
			this.currentAnim  = this.runUp;
			this.isRunning = true;
		}
		else if (input.isKeyDown(Input.KEY_DOWN))
		{
			this.y += this.speed * delta / 1000f;
			
			this.direction = Entity.DIR_DOWN;
			this.currentAnim = this.runDown;
			this.isRunning = true;
		}
		if (input.isKeyDown(Input.KEY_LEFT))
		{
			this.x -= this.speed * delta / 1000f;
			
			this.direction = Entity.DIR_LEFT;
			this.currentAnim = this.runLeft;
			this.isRunning = true;
		}
		else if (input.isKeyDown(Input.KEY_RIGHT))
		{
			this.x += this.speed * delta / 1000f;

			this.direction = Entity.DIR_RIGHT;
			this.currentAnim = this.runRight;
			this.isRunning = true;
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
	 * Draws the player on the screen
	 * 
	 */
	public void render(Graphics g)
	{
		// draws the current sprite
		this.currentSprite.draw((int)this.x, (int)this.y, (int)this.w, (int)this.h);		
	}
}