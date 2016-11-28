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
	 * loadSpriteSheet(String url, int w, int h)
	 * 
	 * Loads a new image and creates a SpriteSheet object with it
	 */
	public SpriteSheet loadSpriteSheet(String url, int per_row, int per_column)
	{
		
		// checks it hasn't already loaded the spritesheet
		
		if (!this.spriteSheets.containsKey(url)) {

			try {
				// loads the image
				Image sheetImage = new Image(url + ".png", false, Image.FILTER_NEAREST);				
				
				// adds it to the spritesheet
				this.spriteSheets.put(url, new SpriteSheet(sheetImage, url));
				
			} catch (SlickException e) {

				// prints that the image does not exist
				e.printStackTrace();
				System.err.format("The Image %d does not exist", url);
				System.exit(0);
			}
		}
		
		// loads the animations for the sheet
		AnimationStore.get().loadAnimationsForSheet(url);
		
		return this.spriteSheets.get(url);
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
