package game;
// The Graphics module
// Uses Slick2D
import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.Image;






import entities.Button;
import entities.Entity;
// The player Entity
import entities.Player;

// the sprite store object
import sprites.SpriteStore;

/*
The main Game class.
This is the main class which runs the program
*/
public class Game extends BasicGame {
	
	/*    Static variables    */
	
	// the different values in the map array
	static final int MAP_EMPTY = 0;
	static final int MAP_BLOCKED = 1;

	
	/*    Non-static variables    */
	
	// The dimensions of the window
	private int w;
	private int h;
	
	// how many TiledMap tiles in a row are on one screen at once
	// used to tell how wide the TiledMap should be
	private int widthInTiles = 15;
	
	// The map
	private TiledMap map;
	
	// A 2D array of whether an entity can walk on these tiles
	private boolean[][] blocked;
	
	// the player entity
	private Player player;

	// all objects/entities on the current map
	private ArrayList<Entity> objects;
	
	/*
	Returns a new Game
	Sets up window and game
	*/
	public Game(int w, int h) {

		// sets the title
		super("The Legend of Zelda: Hamlet");
		
		// records width and height
		this.w = w;
		this.h = h;
	}

	/*
	 * Initializes all conponents of the game
	 */
	public void init(GameContainer container)
	{
		
		// loads the map
		loadMap("assets/maps/test.tmx");
		
		// loads the objects
		loadObjects();
	}
	
	/*
	 * Loads the .tmx file into a slick.TiledMap Object
	 * and sets up Game's blocked value, which is used
	 * for collision detection
	 */
	private void loadMap(String mapUrl) {
		
		// creates the TiledMap
		try {
			// loads the test map
			this.map = new TiledMap(mapUrl);
			
		} catch (SlickException e) {
			// prints error message
			e.printStackTrace();
			System.err.println("Couild not load test map");
			System.exit(0);
		}
		
		blocked = new boolean[this.map.getWidth()][this.map.getHeight()];
		
		for (int x = 0; x < blocked.length; x++) {
			for (int y = 0; y < blocked[x].length; y++) {
				
				int tileID = this.map.getTileId(x, y, this.map.getLayerIndex("Tile Layer"));
				
				String value = this.map.getTileProperty(tileID, "blocked", "false");
				
				if (value.equals("true")) {
					
					blocked[x][y] = true;
				}
				
			}
		}
		
	}

	/*
	 * Loads all the objects in the map
	 * 
	 */
	private void loadObjects() {

		// creates a new player at 0, 0
		this.player = new Player(50, 50, this);
		
		// initializes objects
		this.objects = new ArrayList<Entity>();
		
		for (int gi = 0; gi < this.map.getObjectGroupCount(); gi++) {
			
			for (int oi = 0; oi < this.map.getObjectCount(gi); oi++) {
				
				// gets the x and y for the object
				int objX = this.map.getObjectX(gi,  oi);
				int objY = this.map.getObjectY(gi, oi);
				
				// creates a new object
				switch (this.map.getObjectType(gi, oi)) {
					case "button": this.objects.add(new Button(objX, objY));
				
				}
			}
			
		}
	}
	
	/*
	 * Updates everything
	 * Position, game logic, etc
	 */
	public void update(GameContainer container, int delta)
	{
		
		// gets input
		Input input = container.getInput();
		
		// updates the player
		this.player.update(input, delta, this.blocked);
		
	}
	
	/*
	Starts the game
	*/
	public static void main(String[] args)
	{


        try
        {
        	// sets width and height
        	int size = 800;
        	
        	// creates the window
            AppGameContainer app = new AppGameContainer(new Game(size, size));
            
            // sets the window dimensions
            app.setDisplayMode(size, size, false);
            
            // hides FPS
            app.setShowFPS(false);
            
            // starts the game
            app.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }
	}

	/*
	Renders components on the window
	*/
	public void render (GameContainer container, Graphics g) {
		
		// scales the map to the proper size
		int factor = (this.w / this.widthInTiles * this.map.getWidth()) / (this.map.getWidth() * this.map.getTileWidth());
		g.scale(factor, factor);
		
		// this is the new width/height of the window after scaling after scaling
		int scaledWidth = (this.w / factor);
		int scaledHeight = (this.h / factor);
		
		// the width and height of the map
		int mapWidth = this.map.getWidth() * this.map.getTileWidth();
		int mapHeight = this.map.getHeight() * this.map.getTileHeight();
		
		// gets the amount to move the screen over to center on the player
		int cameraX = (int)((this.player.getX() + this.player.getW() / 2) - (scaledWidth / 2));
		int cameraY = (int)((this.player.getY() + this.player.getH() / 2) - (scaledWidth / 2));
		
		// checks that the camera has not gone too far left/right
		if (cameraX < 0) {
			cameraX = 0;
			
		} else if (cameraX + scaledWidth > mapWidth) {
			cameraX = mapWidth - scaledWidth;
		}
		
		// checks the camera has not gone too far up/down
		if (cameraY < 0) {
			cameraY = 0;
		} else if (cameraY + scaledHeight > mapHeight) {
			cameraY = mapHeight - scaledHeight;
		}
		
		// moves the camera over to center the player
		g.translate(
			- cameraX, 
			- cameraY
		);
		
		// draws the map
		this.map.render(0, 0);
		
		// draws any objects
		for (int i = 0; i < this.objects.size(); i++) {
			this.objects.get(i).render(g);
		}

		// renders the player
		this.player.render(g);

	}
	
	/*
	 * Checks whether a position is blocked based on a 2D array
	 * 
	 */
	public boolean isBlocked(float x, float y, int w, int h)
	{
		// converts to integers, because pixel-specific is good enough
		int intX = Math.round(x);
		int intY = Math.round(y);
		
		// the x, y coordinates of the corners of the entity
		int[][] corners = {
			{
				intX,
				intY
			},
			{
				intX + w,
				intY
			},
			{
				intX + w,
				intY + h
			},
			{
				intX,
				intY + h
			}
		};
		
		// checks if each corner is not hitting anything
		for (int i = 0; i < corners.length; i++) {
			
			// gets the index in the 2D array where the point would be
			int indexX = (int) Math.floor(corners[i][0] / (double)(this.map.getTileWidth()));
			int indexY = (int) Math.floor(corners[i][1] / (double)(this.map.getTileHeight()));
			
			// checks if it hits anything
			if (this.blocked[indexX][indexY]) {
				return true;
			}
			
		}
		return false;
	}

}