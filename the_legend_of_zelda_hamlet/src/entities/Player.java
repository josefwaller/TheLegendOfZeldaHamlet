package entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Color;

import sprites.SpriteSheet;
import sprites.SpriteStore;

/*
The main player class.
*/
public class Player extends Entity{

	int speed = 30;
	
	SpriteSheet sprites;
	
	/*
	Creates a new player
	*/
	public Player(int x, int y)
	{
		
		super(x, y, 30, 50);
		
		this.sprites = SpriteStore.get().loadSpriteSheet(
			"assets/images/testspritesheet.png", 
			8, 
			4);
		
	}
	
	/*
	 * Updates the player position
	 * Moves, takes/deals damage, etc
	 */
	public void update(Input input, int delta)
	{
		
		// checks if the player needs to move
		if (input.isKeyDown(Input.KEY_UP))
		{
			this.y -= this.speed * delta / 1000f;
		}
		else if (input.isKeyDown(Input.KEY_DOWN))
		{
			this.y += this.speed * delta / 1000f;
		}
		if (input.isKeyDown(Input.KEY_LEFT))
		{
			this.x -= this.speed * delta / 1000f;
		}
		else if (input.isKeyDown(Input.KEY_RIGHT))
		{
			this.x += this.speed * delta / 1000f;
		}
	}
	
	/*
	 * Draws the player on the screen
	 * 
	 */
	public void render(Graphics g)
	{
		
		
		this.sprites.getSprite(2, 0).draw((int)this.x, (int)this.y);
		
	}
}