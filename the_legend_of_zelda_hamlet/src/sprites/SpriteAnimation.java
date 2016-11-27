package sprites;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * An Animation for several sprites. Works with the 
 * SpriteSheet to draw animations correctly
 */
public class SpriteAnimation {

	// the spritesheet the animations are for
	private SpriteSheet sheet;
	
	// the animations
	private HashMap<String, SpriteAnimationFrame[]> animations;
	
	public SpriteAnimation(SpriteSheet sheet) {
		
		// saves the sheet
		this.sheet = sheet;
		
		// records the animation from the XML file
		SpriteAnimation.getAnimationDataFromXML(sheet.getPath() + ".anim");
	}
	
	public void drawAnimation(String Animation, int index, int x, int y) {
		
	}
	
	public static HashMap<String, SpriteAnimationFrame[]> getAnimationDataFromXML(String xml) {
		
		// creates a new HashMap to return
		HashMap<String, SpriteAnimationFrame[]> animData = new HashMap<String, SpriteAnimationFrame[]>();

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
			
			// finds all the child nodes
			NodeList children = tag.getElementsByTagName("cell");
			
			// creates the array to store the information
			SpriteAnimationFrame[] animPaths = new SpriteAnimationFrame[children.getLength()];
			
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
				animPaths[x] = new SpriteAnimationFrame(offX, offY, path);
				
				System.out.println(String.format("%s, %d, %d", path, offX, offY));
			}
			
			// adds the animation to the HashMap
			animData.put(tag.getAttribute("name"), animPaths);
			
		}
		
		// returns the completed HashMap
		return animData;
	}
	
}
