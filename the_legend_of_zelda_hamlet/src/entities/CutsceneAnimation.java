package entities;

import org.newdawn.slick.Graphics;

import entities.abstracts.Entity;
import game.Game;

/*
 * An entity that moves and acts according t a file. To be
 * used during animated cutscenes in the game.
 */
public class CutsceneAnimation extends Entity {

	/*
	 * creates a new CutsceneAnimation from the animation file name
	 */
	public CutsceneAnimation (String filename, Game g) {
		
		super (0, 0, 16, g);
	}
	
	public void render(Graphics g) {
		
	}
}
