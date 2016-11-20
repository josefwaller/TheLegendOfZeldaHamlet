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
	int speed = 30;
	
	// the running animations
	Animation runUp;
	Animation runDown;
	Animation runLeft;
	Animation runRight;
	
	Animation currentAnim;
	
	/*
	Creates a new player
	*/
	public Player(int x, int y)
	{
		// sets position
		super(x, y, 30, 50);
		
		// loads sprites
		SpriteSheet sheet = SpriteStore.get().loadSpriteSheet(
			"assets/images/testspritesheet.png", 
			8, 
			4
		);
		
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
		
		this.runDown = new Animation(runDownImages, 300, false);
		this.runLeft = new Animation(runLeftImages, 300, false);
		this.runUp = new Animation(runUpImages, 300, false);
		this.runRight = new Animation(runRightImages, 300, false);
		
		this.currentAnim = this.runDown;
		
	}
	
	/*
	 * Updates the player position
	 * Moves, takes/deals damage, etc
	 */
	public void update(Input input, int delta)
	{
		
		boolean isRunning = false;
		
		// checks if the player needs to move
		if (input.isKeyDown(Input.KEY_UP))
		{
			this.y -= this.speed * delta / 1000f;
			
			this.currentAnim  = this.runUp;
			isRunning = true;
		}
		else if (input.isKeyDown(Input.KEY_DOWN))
		{
			this.y += this.speed * delta / 1000f;
			
			this.currentAnim = this.runDown;
			isRunning = true;
		}
		if (input.isKeyDown(Input.KEY_LEFT))
		{
			this.x -= this.speed * delta / 1000f;
			
			this.currentAnim = this.runLeft;
			isRunning = true;
		}
		else if (input.isKeyDown(Input.KEY_RIGHT))
		{
			this.x += this.speed * delta / 1000f;
			
			this.currentAnim = this.runRight;
			isRunning = true;
		}
		
		if (isRunning) {
			
			this.currentAnim.update(delta);
			
		} else {
			switch (this.direction) {
				case Entity.DIR_UP: 
				case Entity.DIR_DOWN: 
				case Entity.DIR_LEFT: 
				case Entity.DIR_RIGHT: 
			}
		}
	}
	
	/*
	 * Draws the player on the screen
	 * 
	 */
	public void render(Graphics g)
	{
		
		
		 this.currentAnim.draw((int)this.x, (int)this.y);
		
	}
}