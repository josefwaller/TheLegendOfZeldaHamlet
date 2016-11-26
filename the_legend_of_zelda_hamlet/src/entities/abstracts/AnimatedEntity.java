package entities.abstracts;

import game.Game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/*
 * An entity that is animated. It's main use is to
 * automatically draw the currentSprite (which 
 * should be set by the child class) with an offset,
 * so sprites with different positioning can be
 * easily used together.
 */
public abstract class AnimatedEntity extends Entity {
	
	// the sprite to render
	protected Image currentSprite;
	
	// how much to move the sprites over so that they are positioned correctly
	protected int imgX;
	protected int imgY;
	
	/*
	 * Constructor for when the entity does not have equal width and height
	 */
	public AnimatedEntity(int x, int y, int w, int h, Game g) {
		super(x, y, w, h, g);
	}
	/*
	 * Constructor for when the entity does have equal width and height
	 */
	public AnimatedEntity(int x, int y, int s, Game g) {
		super(x, y, s, g);
	}
	
	/*
	 * Renders the sprite on the screen
	 */

	public void render(Graphics g) {
		
		// draws the sprite
		this.currentSprite.draw(
			(int)this.x + this.imgX, 
			(int)this.y + this.imgY, 
			this.currentSprite.getWidth(), 
			this.currentSprite.getHeight()
		);	
		
		// draws hitboxes
		this.drawHitboxes(g);
	}
	
}
