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
		this.player = new Player(0, 0);
		
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
        	int width = 400;
        	int height = 400;
        	
        	// creates the window
            AppGameContainer app = new AppGameContainer(new Game(width, height));
            
            // sets the window dimensions
            app.setDisplayMode(width, height, false);
            
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

		this.player.render(g);

	}

}