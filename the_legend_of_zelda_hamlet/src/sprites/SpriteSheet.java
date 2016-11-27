package sprites;

import game.Game;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import org.newdawn.slick.Image;

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
	
	// the data associated with the sprite sheet
	private HashMap sheetData;
	
	/*
	 * Constructor
	 * 
	 * Loads the sprites and their data file,
	 */
	public SpriteSheet (Image sheet, String fileName) {
		
		// loads the sheet
		this.sheet = sheet;
		
		// loads the data
		String xmlData = Game.readFile(fileName + ".sprites");
		
		System.out.println(xmlData);
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
		
		return this.sheet;//.getSubImage(croppedX, croppedY, this.w, this.h);
	}
}
