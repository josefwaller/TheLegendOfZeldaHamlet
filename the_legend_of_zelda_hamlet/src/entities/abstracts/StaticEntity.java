package entities.abstracts;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import sprites.SpriteStore;
import game.Game;

/*
 * A static entity. It has a set X and Y, and doesn't move.
 * It also only has one frame.
 */
public abstract class StaticEntity extends Entity {

	// the one sprite
	protected Image sprite;
	
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
	
	// Static entities don't need anything to update
	public abstract void update();
	
	/*
	 * Loads an image, and sets width and height relative to the image
	 */
	public void loadImageWithDimensions(String url) {
		
		// loads the image
		this.sprite = SpriteStore.get().loadSprite(url);
		
		// sets width and height relative
		this.w = this.sprite.getWidth();
		this.h = this.sprite.getHeight();
		
		// adds a default hitbox
		this.addHitbox(0,  0,  this.w,  this.h);
		
	}
	
	/*
	 * Default rendering method
	 * Just draws the sprite at the x and y coordinate
	 */
	public void render(Graphics g) {
	
		this.sprite.draw((int)this.x, (int)this.y, this.w, this.h);
		
		// draws the hitbox
		this.drawHitboxes(g);
		
	}
	
	/*
	 * Adds a default hitbox with the entity's width and height
	 */
	protected void addHitbox() {
		super.addHitbox(0, 0, this.w, this.h);
	}
	
}
