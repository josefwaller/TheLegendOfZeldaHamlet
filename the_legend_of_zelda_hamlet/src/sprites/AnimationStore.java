package sprites;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AnimationStore {

	private HashMap<String, HashMap<String, SpriteAnimation>> animations;
	
	private static AnimationStore animStore = new AnimationStore();
	
	public AnimationStore() {
		
		this.animations = new HashMap<String, HashMap<String, SpriteAnimation>>();
		
	}
	
	private void loadAnimationsForSheet(String sheetUrl) {
		
		// adds a new entry
		this.animations.put(sheetUrl, new HashMap<String, SpriteAnimation>());
		
		// loads the xml file
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
		
		System.out.println("Cycling through XML tags.");
			
		// goes through all the animation tags
		for (int j = 0; j < animTags.getLength(); j++) {
			
			Element animTag = (Element)animTags.item(j);
			
			// finds all the child nodes
			NodeList children = animTag.getElementsByTagName("cell");
			
			System.out.println(String.format("Animation node %s",  animTag.getNodeName()));
			
			// creates the array to store the information
			SpriteAnimationFrame[] anim = new SpriteAnimationFrame[children.getLength()];
			
			// cycles through the cell tags and adds the sprite path to the array
			for (int x = 0; x < children.getLength(); x++) {
				
				// gets the child
				Element child = (Element) children.item(x);
				
				System.out.println(String.format("Final child element: %s", child.getNodeName()));
				
				// gets the sprite tag
				Element sprite = (Element)child.getElementsByTagName("spr").item(0);
				
				// gets the path in the <spr> tag in it
				String path = sprite.getAttribute("name");
				
				System.out.println("Loaded sprite path " + path);
				
				// gets the x and y offset
				int offX = Integer.parseInt(sprite.getAttribute("x"));
				int offY = Integer.parseInt(sprite.getAttribute("y"));
				
				// adds the path to the array
				anim[x] = new SpriteAnimationFrame(offX, offY, path);
			}
			
			// adds the new animation to the sprite animation
			this.animations.get(sheetUrl).put(animTag.getAttribute("name"),
				new SpriteAnimation(sheetUrl, 100, anim));
		}
	}
	
	public SpriteAnimation getAnimation(String sheetName, String animName) {
		
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
