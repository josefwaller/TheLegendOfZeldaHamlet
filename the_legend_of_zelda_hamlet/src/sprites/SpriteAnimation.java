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
	private HashMap<String, String[]> animations;
	
	public SpriteAnimation(SpriteSheet sheet) {
		
		// saves the sheet
		this.sheet = sheet;
		
		// records the animation from the XML file
		SpriteAnimation.getAnimationDataFromXML(sheet.getPath() + ".anim");
	}
	
	public void drawAnimation(String Animation, int index, int x, int y) {
		
	}
	
	public static HashMap<String, String[]> getAnimationDataFromXML(String xml) {
		
		// creates a new HashMap to return
		HashMap<String, String[]> animData = new HashMap<String, String[]>();

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
			String[] animPaths = new String[children.getLength()];
			
			// cycles through the cell tags and adds the sprite path to the array
			for (int x = 0; x < children.getLength(); x++) {
				
				// gets the child
				Element child = (Element) children.item(x);
				
				// gets the path in the <spr> tag in it
				String path = ((Element)child.getElementsByTagName("spr").item(0)).getAttribute("name");
				
				// adds the path to the array
				animPaths[x] = path;
				
				System.out.println(path);
			}
			
			// adds the animation to the HashMap
			animData.put(tag.getAttribute("name"), animPaths);
			
		}
		
		// returns the completed HashMap
		return animData;
	}
	
}
