package sprites;

/*
 * Represents a single frame in an animation. Used by 
 * SpriteAnimation to record animations with x and y
 * offsets
 */
public class SpriteAnimationFrame {

	// the x and y offset of the sprite
	public int x;
	public int y;
	
	// the path to the sprite in the spritesheet
	// ex: "/Attacking/Side/4"
	public String spritePath;
	
	/*
	 * Creates a new frame in a SpriteAnimation
	 */
	public SpriteAnimationFrame(int x, int y, String spritePath) {
		
		// saves offsets
		this.x = x;
		this.y = y;
		
		// saves sprite path
		this.spritePath = spritePath;
	}
	
}
