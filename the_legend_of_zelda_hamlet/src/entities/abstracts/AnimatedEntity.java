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
	private Animation currentAnim;
	
	// the duration between animation frame changes
	private int currentDuration;
	
	// the current index of the frame
	private int index;
	
	// the last time the frame changed
	private long lastChangeTime;
	
	// whether to loop the animation or not
	protected boolean loop;
	
	/*
	 * Constructors
	 * 
	 * Calls the Entity constructor and sets up animation variables
	 */
	public AnimatedEntity(int x, int y, int w, int h, Game g) {
		super(x, y, w, h, g);
		
		this.index = 0;
		this.lastChangeTime = System.currentTimeMillis();
		this.currentDuration = 0;
		this.loop = true;
	}
	public AnimatedEntity(int x, int y, int s, Game g) {
		super(x, y, s, g);
		
		this.index = 0;
		this.lastChangeTime = System.currentTimeMillis();
		this.currentDuration = 0;
		this.loop = true;
	}
	
	/*
	 * Updates the animation the entity is playing
	 */
	protected void animUpdate() {
		
		if (System.currentTimeMillis() - this.lastChangeTime >= this.currentDuration) {
			
			if (this.loop || this.index < this.currentAnim.getAnimLength() - 1) {

				this.index = (this.index + 1) % this.currentAnim.getAnimLength();
				this.lastChangeTime = System.currentTimeMillis();
			}
			
		}
		
	}
	/*
	 * Sets the animation and restarts it if it is different
	 */
	protected void setAnim(Animation newAnim, int duration) {

		if (newAnim != this.currentAnim || duration != this.currentDuration) {
		
			this.currentAnim = newAnim;
			this.currentDuration = duration;
			this.index = 0;
		}
	}
	
	/*
	 * Renders the sprite on the screen
	 */
	public void render(Graphics g) {
		
		// gets the sprite to use now
		Image sprite = this.currentAnim.getSprite(this.index);
		
		// gets the coordinates to draw the sprite
		int x = (int) (this.x + this.w / 2);
		int y = (int) (this.y + this.h / 2);
		
		if (this.direction == Entity.DIR_LEFT) {
			
			// moves the sprite over and mirrors it
			x -= this.currentAnim.getOffX(this.index);
			sprite = sprite.getFlippedCopy(true, false);
			
		} else {
			
			// just move the sprite over
			x += this.currentAnim.getOffX(this.index);
		}
		
		// moves the sprite up
		y += this.currentAnim.getOffY(this.index);
		
		// draws the sprite with the proper x and y offset
		sprite.drawCentered(x, y);
		
		// draws hitboxes
		if (this.game.isDebug()) {
			this.drawHitboxes(g);
		}
	}
	
	/*
	 * Updates the current animation
	 */
	
}
