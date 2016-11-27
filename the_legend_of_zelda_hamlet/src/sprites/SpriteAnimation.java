package sprites;

import java.util.HashMap;

/*
 * An Animation for several sprites. Works with the 
 * SpriteSheet to draw animations correctly
 */
public class SpriteAnimation {

	// the spritesheet the animations are for
	private SpriteSheet sheet;
	
	// the animations
	private HashMap<String, String[]> animations;
	
	public SpriteAnimation(SpriteSheet sheet) {
		this.sheet = sheet;
	}
	
	public void drawAnimation(String Animation, int index, int x, int y) {
		
	}
	
}
