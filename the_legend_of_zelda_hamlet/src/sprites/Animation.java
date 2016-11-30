package sprites;

import org.newdawn.slick.Image;

/*
 * An Animation for several sprites. Works with the 
 * SpriteSheet to draw animations correctly
 */
public class Animation {

	// the spritesheet the animations are for
	private String sheetUrl;
	
	// the frames
	private SpriteAnimationFrame[] frames;
	
	// the time between updating
	private int duration;
	
	// the last time the image changed
	private long lastChange;
	
	// the index of the current image
	private int index;
	
	public Animation(String sheetUrl, int duration, SpriteAnimationFrame[] frames) {
		
		// saves the sheet
		this.sheetUrl = sheetUrl;
		
		this.duration = duration;
		
		this.frames = frames;
		
		this.lastChange = System.currentTimeMillis();
	}
	

	/*
	 * Checks if the sprite animation should change sprites
	 */
	public void update() {
		
		if (System.currentTimeMillis() - this.lastChange > this.duration) {
			this.index = (this.index + 1) % this.frames.length;
			this.lastChange = System.currentTimeMillis();
		}
	}
	/*
	 * Gets the current frame of the animation
	 */
	public Image getSprite(int i) {
		
		// gets the image
		Image sprite = SpriteStore.get().loadSpriteSheet(this.sheetUrl)
				.getSprite(this.frames[i].spritePath);
		
		// checks if the image should be mirrored
		if (this.frames[i].isMirrored) {
			
			// mirrors the sprite
			sprite = sprite.getFlippedCopy(true, false);
		}
		
		return sprite;
	}
	
	/*
	 * Get/Set methods
	 */
	public int getAnimLength() {
		return this.frames.length;
	}
	public int getOffX(int i) {
		return this.frames[i].x;
	}
	public int getOffY(int i) {
		return this.frames[i].y;
	}
	public void setDuration(int d) {
		this.duration = d;
	}
}
