package entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import game.Game;

/*
 * A static entity. It has a set X and Y, and doesn't move.
 * It also only has one frame.
 */
public abstract class StaticEntity extends Entity {

	// the one sprite
	private Image sprite;
	
	/*
	 * Sets up position and dimensions
	 */
	public StaticEntity(int x, int y, int w, int h, Game g)
	{
		super(x, y, w, h, g);
	}
	
	/*
	 * Another constructor in case width and height are the same
	 */
	public StaticEntity(int x, int y, int s, Game g)
	{
		super(x, y, s, g);
	}
	
	/*
	 * Default rendering method
	 * Just draws the sprite at the x and y coordinate
	 */
	public void render(Graphics g) {
	
		this.sprite.draw((int)this.x, (int)this.y, this.w, this.h);
		
	}
	
}
