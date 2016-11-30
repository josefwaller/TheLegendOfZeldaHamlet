package entities.abstracts;

import game.Game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import sprites.Animation;

/*
 * An entity that is animated. It's main use is to
 * automatically draw the currentSprite (which 
 * should be set by the child class) with an offset,
 * so sprites with different positioning can be
 * easily used together.
 */
public abstract class AnimatedEntity extends Entity {
	
	// the current animation the entity is playing
	protected Animation currentAnim;
	
	/*
	 * Constructors
	 * 
	 * Exactly the same as entity, just sets them up
	 */
	public AnimatedEntity(int x, int y, int w, int h, Game g) {
		super(x, y, w, h, g);
	}
	public AnimatedEntity(int x, int y, int s, Game g) {
		super(x, y, s, g);
	}
	
	/*
	 * Renders the sprite on the screen
	 */
	public void render(Graphics g) {
		
		// gets the sprite to use now
		Image sprite = this.currentAnim.getSprite();
		
		// gets the coordinates to draw the sprite
		int x = (int) (this.x + this.w / 2);
		int y = (int) (this.y + this.h / 2);
		
		if (this.direction == Entity.DIR_LEFT) {
			
			// moves the sprite over and mirrors it
			x -= this.currentAnim.getOffX();
			sprite = sprite.getFlippedCopy(true, false);
			
		} else {
			
			// just move the sprite over
			x += this.currentAnim.getOffX();
		}
		
		// moves the sprite up
		y += this.currentAnim.getOffY();
		
		// draws the sprite with the proper x and y offset
		sprite.drawCentered(x, y);
		
		// draws hitboxes
		if (this.game.isDebug()) {
			this.drawHitboxes(g);
		}
	}
	
}
