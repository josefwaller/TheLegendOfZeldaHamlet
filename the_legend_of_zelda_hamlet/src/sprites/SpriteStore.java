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
	public SpriteStore() {
		
	}
	
	/*
	 * loadImage(String url)
	 * 
	 * Loads a new image and saves it to the Hashmap
	 */
	public Image loadSprite(String url) throws SlickException
	{
		
		// currently returns a test image
		return new Image("assets/images/test.jpg");
	}
	/*
	 * loadSpriteSheet(String url, int w, int h)
	 * 
	 * Loads a new image and creates a SpriteSheet object with it
	 */
	public SpriteSheet loadSpriteSheet(String url, int w, int h) throws SlickException
	{
		// currently returns a SpriteSheet with a test image
		return new SpriteSheet(new Image("assets/images/test.jpg"), 0, 0);
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
