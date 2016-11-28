package sprites;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.newdawn.slick.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
	
	// the index of the curretn image
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
	 * Loads an animation and animation data into a HashMap
	 */
	public static SpriteAnimationFrame[] getAnimationDataFromXML(String xml, String animName) {
		
		// Creates the array to return, but doesn't actually store anything because we don't know its length yet
		SpriteAnimationFrame[] animData = new SpriteAnimationFrame[0];

		// parses the XML into a NodeList of the animation tags
		NodeList tags;
		
		try {

			DocumentBuilderFactory dBFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dBFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xml);
			tags = doc.getElementsByTagName("anim");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		// cycles through the nodes
		for (int i = 0; i < tags.getLength(); i++) {
			
			// casts to element
			Element tag = (Element)(tags.item(i));
			
			// finds the one relating to the animation
			if (tag.getAttribute("name").equals(animName)) {
				
				// finds all the child nodes
				NodeList children = tag.getElementsByTagName("cell");
				
				// creates the array to store the information
				animData = new SpriteAnimationFrame[children.getLength()];
				
				// cycles through the cell tags and adds the sprite path to the array
				for (int x = 0; x < children.getLength(); x++) {
					
					// gets the child
					Element child = (Element) children.item(x);
					
					// gets the sprite tag
					Element sprite = (Element)child.getElementsByTagName("spr").item(0);
					
					// gets the path in the <spr> tag in it
					String path = sprite.getAttribute("name");
					
					// gets the x and y offset
					int offX = Integer.parseInt(sprite.getAttribute("x"));
					int offY = Integer.parseInt(sprite.getAttribute("y"));
					
					// adds the path to the array
					animData[x] = new SpriteAnimationFrame(offX, offY, path);
				}
				
				break;
			}
			
		}
		
		// returns the completed Array
		return animData;
	}
	/*
	 * Restarts the animation
	 */
	public void restart() {
		this.index = 0;
		this.lastChange = System.currentTimeMillis();
	}
	
	/*
	 * Get/Set methods
	 */
	public int getAnimLength() {
		return this.frames.length;
	}
	public Image getSprite() {
		return SpriteStore.get().loadSpriteSheet(this.sheetUrl).getSprite(this.frames[this.index].spritePath);
	}
	public int getOffX() {
		return this.frames[this.index].x;
	}
	public int getOffY() {
		return this.frames[this.index].y;
	}
	public void setDuration(int d) {
		this.duration = d;
	}
}
