package sprites;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/*
 * SpriteSheet
 * 
 * Represents a sprite sheet for easily getting individual sprites
 */
public class SpriteSheet {

	// The sprite sheet image
	private Image sheet;
	
	/*
	 * Constructor
	 * 
	 * Loads the sprite sheet and sets the width and height of each sprite in pixel
	 */
	public SpriteSheet(Image sheet, int w, int h)
	{
		
	}
	
	/*
	 * getSprite(int x, int y)
	 * 
	 * Gets a single sprite at the x and y offset.
	 */
	public Image getSprite(int x, int y) throws SlickException
	{
		
		// currently just returns a test image
		return new Image("assets/test.jpg");
	}
	
}
