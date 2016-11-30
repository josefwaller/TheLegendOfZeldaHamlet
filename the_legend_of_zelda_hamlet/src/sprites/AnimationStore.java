package sprites;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 * Loads and stores different sprite animations for
 * .anim files. Gives out references to them for 
 * entities to use. Prevents several copies of the
 * animations using up RAM.
 */
public class AnimationStore {

	// the HashMap containing the animation information
	// the top HashMap contains the animations for a spritesheet at the 
	// key of the spritesheet's path
	// Ex: "assets/images/linkspritesheet" would contain Link's animations
	private HashMap<String, HashMap<String, Animation>> animations;
	
	// the single instance of the AnimationStore
	// so that animations are not loaded over and over again
	private static AnimationStore animStore = new AnimationStore();
	
	/*
	 * Constructor
	 * Creates a new HashMap to store the sprites
	 */
	public AnimationStore() {
		
		// initializes HashMap
		this.animations = new HashMap<String, HashMap<String, Animation>>();
		
	}
	
	/*
	 * Loads animations for a sprite sheet
	 */
	public void loadAnimationsForSheet(String sheetUrl) {
		
		// adds a new entry
		this.animations.put(sheetUrl, new HashMap<String, Animation>());
		
		// loads the XML file
		File xml = new File(sheetUrl + ".anim");
		
		// creates a new NodeList to hold the XML tags
		NodeList animTags = null;
		
		try {

			// parses the XML into a NodeList of the animation tags
			DocumentBuilderFactory dBFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dBFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xml);
			animTags = doc.getElementsByTagName("anim");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			System.exit(0);
		}
			
		// goes through all the <anim> tags
		for (int j = 0; j < animTags.getLength(); j++) {
			
			// gets the <anim> tag
			Element animTag = (Element)animTags.item(j);
			
			// finds all the <cell> tags in the <anim> tag
			// each one represents a frame in the animation
			NodeList children = animTag.getElementsByTagName("cell");
			
			// creates the array to store the information
			SpriteAnimationFrame[] anim = new SpriteAnimationFrame[children.getLength()];
			
			// cycles through the <cell> tags and adds the sprite path to the array
			for (int x = 0; x < children.getLength(); x++) {
				
				// gets the child <cell> tag
				Element child = (Element) children.item(x);
				
				// gets the <spr> tag
				Element sprite = (Element)child.getElementsByTagName("spr").item(0);
				
				// gets the path in the <spr> tag in it
				String path = sprite.getAttribute("name");
				
				// gets the x and y offset of the sprite
				int offX = Integer.parseInt(sprite.getAttribute("x"));
				int offY = Integer.parseInt(sprite.getAttribute("y"));
				
				// gets whether the sprite is mirrored
				boolean isMirrored = sprite.hasAttribute("flipH");
				
				// adds the path to the array
				anim[x] = new SpriteAnimationFrame(offX, offY, path, isMirrored);
			}
			
			// adds the new animation to the sprite animation
			this.animations.get(sheetUrl).put(animTag.getAttribute("name"),
				new Animation(sheetUrl, 100, anim));
		}
	}
	
	public Animation getAnimation(String sheetName, String animName) {
		
		// checks if it needs to load the animation
		if (!this.animations.containsKey(sheetName)) {
			
			this.loadAnimationsForSheet(sheetName);
		} 
		
		return this.animations.get(sheetName).get(animName);
	}
	
	public static AnimationStore get() {
		return AnimationStore.animStore;
	}
	
}
