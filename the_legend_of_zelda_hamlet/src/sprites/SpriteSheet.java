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
	
	// the width and height of an individual sprite
	private int w;
	private int h;
	
	/*
	 * Constructor
	 * 
	 * Loads the sprite sheet and sets the width and height of each sprite in pixel
	 * per_row and per_column are how many sprites per row and per column
	 */
	public SpriteSheet(Image sheet, int per_row, int per_column)
	{
		this.sheet = sheet;
		
		this.w = sheet.getWidth()/ per_row;
		this.h = sheet.getHeight() / per_column;
	}
	
	/*
	 * getSprite(int x, int y)
	 * 
	 * Gets a single sprite at the x and y offset.
	 */
	public Image getSprite(int x, int y)
	{
		// gets the new x and y coordinates		
		int croppedX = x * this.w;
		int croppedY = y * this.h;
		
		return this.sheet.getSubImage(croppedX, croppedY, this.w, this.h);
	}
	
}
