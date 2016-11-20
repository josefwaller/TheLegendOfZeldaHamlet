package game;
// The Graphics module
// Uses Slick2D
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.Image;



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
	private int widthInTiles = 10;
	
	// The map
	private TiledMap map;
	
	// A 2D array of whether an entity can walk on these tiles
	private boolean[][] blocked;
	
	// the player entity
	private Player player;

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
		
		// creates a new player at 0, 0
		this.player = new Player(50, 50, this);
		
		try {
			// loads the test map
			this.map = new TiledMap("assets/maps/test.tmx", "assets/maps");
			
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
		
		// draws the map
		this.map.render(0, 0);

		// renders the player
		this.player.render(g);

	}
	
	/*
	 * Checks whether a position is blocked based on a 2D array
	 * 
	 */
	public boolean isBlocked(float x, float y)
	{
		int xIndex = (int) Math.floor(x / this.map.getTileWidth());
		int yIndex = (int) Math.floor(y / this.map.getTileWidth());
		
		if (this.blocked[xIndex][yIndex] || this.blocked[xIndex + 1][yIndex]) {
			if (this.blocked[xIndex][yIndex] || this.blocked[xIndex][yIndex + 1]) {
				return true;
			}
		}
		
		return false;
	}

}