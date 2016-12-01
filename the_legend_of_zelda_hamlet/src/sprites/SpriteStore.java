package sprites;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/*
 * Stores and loads sprites, then gives references to them 
 * when entities need to render. Ensures that a sprite is
 * not loaded twice
 */
public class SpriteStore {

	// The one instance of a SpriteStore, which entities can get through SpriteStore.get()
	private static SpriteStore spriteStore = new SpriteStore();
	
	// a HashMap of all the images loaded
	Map<String, Image> images = new HashMap<String, Image>();
	
	// a HashMap of all the SpriteSheets created (and therefore sprite sheets loaded)
	Map<String, SpriteSheet> spriteSheets = new HashMap<String, SpriteSheet>();
	
	/*
	 * Constructor
	 * 
	 * Sets up a new SpriteStore
	 */
	public SpriteStore()
	{
		
		// initializes both HashMaps
		this.images = new HashMap<String, Image>();
		this.spriteSheets = new HashMap<String, SpriteSheet>();
		
	}
	
	/*
	 * loadImage(String url)
	 * 
	 * Loads a new image and saves it to the HashMap
	 */
	public Image loadSprite(String url)
	{
		
		// checks if the SpriteStore has not loaded this image before
		if (!this.images.containsKey(url))
		{
			try {

				// loads the sprite
				this.images.put(url, new Image(url, false, Image.FILTER_NEAREST));
			} catch (SlickException e) {
				
				// prints error message
				e.printStackTrace();
				System.err.format("The image at %d could not be found", url);
				System.exit(0);
				
				return null;
			}
		}
		
		return this.images.get(url);
	}
	
	/*
	 * loadSpriteSheet
	 * 
	 * Loads a new image and creates a SpriteSheet object with it
	 */
	public SpriteSheet loadSpriteSheet(String imagePath, String spritesPath)
	{
		
		// checks it hasn't already loaded the spritesheet
		
		if (!this.spriteSheets.containsKey(imagePath)) {

			try {
				// loads the image
				Image sheetImage = new Image(imagePath + ".png", false, Image.FILTER_NEAREST);				
				
				// adds it to the spritesheet
				this.spriteSheets.put(imagePath, new SpriteSheet(sheetImage, spritesPath));
				
			} catch (SlickException e) {

				// prints that the image does not exist
				e.printStackTrace();
				System.err.format("The Image %d does not exist", imagePath);
				System.exit(0);
			}
		}
		
		return this.spriteSheets.get(imagePath);
	}
	
	/*
	 * Loads a sprite sheet if the .sprites file and .anim file are the same
	 */
	public SpriteSheet loadSpriteSheet(String path) {
		return this.loadSpriteSheet(path, path);
	}
	
	/*
	 * get()
	 * 
	 * Returns the one instance of a SpriteStore for entities to use.
	 */
	public static SpriteStore get() {
		return SpriteStore.spriteStore;
	}
	
}
