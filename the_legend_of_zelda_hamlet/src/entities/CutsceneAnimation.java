package entities;

import org.newdawn.slick.Graphics;

import entities.abstracts.Entity;
import game.Game;

/*
 * An entity that moves and acts according t a file. To be
 * used during animated cutscenes in the game.
 */
public class CutsceneAnimation extends Entity {

	String[] actions;
	
	/*
	 * creates a new CutsceneAnimation from the animation file name
	 */
	public CutsceneAnimation (String filename, Game g) {
		
		// calls constructor
		super (0, 0, 16, g);
		
		// reads the file
		String fileContents = Game.readFile("assets/anim/" + filename);
		
		// records the contents of the file
		this.actions = fileContents.split("\n");
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g) {
		
	}
}
