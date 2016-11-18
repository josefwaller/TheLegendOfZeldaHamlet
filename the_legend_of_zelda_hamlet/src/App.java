// The Graphics module
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

// The player Entity
import entities.Player;

/*
The main App class.
This is the main class which runs the program
*/
public class App extends Frame {

	Player player;

	/*
	Returns a new App
	Sets up window and game
	*/
	public App() {

		// sets the title
		super("The Legend of Zelda: Hamlet");

		// sets the size
		setSize(400, 400);

		// exits when the close button is pressed
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}        
		});

		// creates a new player
		this.player = new Player();
	}

	/*
	Starts the game
	*/
	public static void main(String[] args) {

		App app = new App();
		app.setVisible(true);

		System.out.println("Hello World!");
	}

	/*
	Renders components on the window
	*/
	@Override
	public void paint (Graphics g) {

		g.setColor(Color.RED);
		g.drawRect(0, 0, 200, 200);

	}

}