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

	// The dimensions of the window
	private int w;
	private int h;
	
	// how many TiledMap tiles in a row are on one screen at once
	// used to tell how wide the TiledMap should be
	private int widthInTiles = 10;
	
	private TiledMap map;
	
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
		this.player = new Player(50, 50);
		
		// loads the test map
		try {
			this.map = new TiledMap("assets/maps/test.tmx");
			
		} catch (SlickException e) {
			// prints error message
			e.printStackTrace();
			System.err.println("Couild not load test map");
			System.exit(0);
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
		this.player.update(input, delta);
		
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

}