package sprites;

import game.Game;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.newdawn.slick.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	private HashMap<String, SpriteData> sheetData;
	
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
		
		this.sheetData = SpriteSheet.getSpriteDataFromXML(new File(fileName + ".sprites"));
	}
	
	/*
	 * getSprite(int x, int y)
	 * 
	 * Gets a single sprite at the x and y offset.
	 */
	public Image getSprite(int x, int y)
	{
		
		return this.sheet;//.getSubImage(croppedX, croppedY, this.w, this.h);
	}
	
	/*
	 * Parses XML data
	 */
	public static HashMap<String, SpriteData> getSpriteDataFromXML(File xml) {
		
		// creates a new HashMap to return
		HashMap<String, SpriteData> spriteData = new HashMap<String, SpriteData>();
		
		// parses the XML into a NodeList
		NodeList tags;
		try {

			DocumentBuilderFactory dBFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dBFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xml);
			tags = doc.getElementsByTagName("spr");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		// for each tag
		for (int i = 0; i < tags.getLength(); i++) {
			
			// gets the node
			Element tag = (Element) tags.item(i);
			
			/*
			 * Sets the key of the SpriteData to the path to the sprite
			 * So an example could be /Attacking/Front/0
			 * for the first frame when attacking downward
			 */
			String key = tag.getAttribute("name");
			
			Element thisElement = tag;
			
			while (true) {
				thisElement = (Element)(thisElement.getParentNode());
				
				String elementName = thisElement.getAttribute("name");
				
				if (elementName.equals("/")) {
					break;
				} else {
					key = elementName + "/" + key;
				}
			}
			
			key = "/" + key;
			
			// creates a new sprite data for the sprite
			SpriteData data = new SpriteData(
				Integer.parseInt(tag.getAttribute("x")),
				Integer.parseInt(tag.getAttribute("y")),
				Integer.parseInt(tag.getAttribute("w")),
				Integer.parseInt(tag.getAttribute("h"))
			);
			
			// adds this sprite to the hashmap
			spriteData.put(key, data);
		}
		
		// returns the parsed XML
		return spriteData;
	}
}
