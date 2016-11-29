package entities;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import sprites.Animation;
import sprites.AnimationStore;
import entities.abstracts.Entity;
import game.Game;

/*
 * An entity that moves and acts according to a file. To be
 * used during animated cutscenes in the game.
 */
public class AutomatedEntity extends Entity {

	// the different things the animation can do
	static final int MOVE = 0;
	static final int DIALOG = 1;
	static final int CHANGE_SPRITES = 2;
	static final int WAIT = 3;
	static final int REPEAT = 4;
	
	// what the current action is
	// uses the cutscene animation's static final variables
	int[] actions;
	
	// the different positions the animation will walk to
	int[][] positions;
	
	// the index of the current position
	int positionIndex;
	
	// the different dialogs the animation will say
	String[] dialog;
	
	// the index of the current dialog
	int dialogIndex;
	
	// the name of the spritesheet this animation uses
	String sprites;
	
	// the name of the different sprite animatons to change between
	String[] animNames;
	
	// the current sprite animation
	Animation currentAnim;
	
	/*
	 * creates a new CutsceneAnimation from the animation file name
	 */
	public AutomatedEntity (String filename, Game g) {
		
		// calls constructor
		super (0, 0, 16, g);
		
		// reads the file
		String fileContents = Game.readFile("assets/anim/" + filename);
		
		// records the contents of the file
		String[] actions = fileContents.toLowerCase().split("\\r?\\n");
		
		// temporary storage of the points the animation moves to
		// until we know how many points there are
		ArrayList<int[]> tempPoints = new ArrayList<int[]>();
		
		// same as tempPoints, but for the srite animations
		ArrayList<String> tempAnims = new ArrayList<String>();
		
		for (int i = 0; i < actions.length; i++) {
			String[] words = actions[i].split(" ");
			
			switch(words[0]) {
			
				// saves the spriteset the cutscene animation will use
				case "sprites":
					this.sprites = words[1];
					
					// preloads the sprites
					AnimationStore.get().loadAnimationsForSheet(this.sprites);
					break;
					
				// saves a sprite animation the cutscene animation wil use
				case "anim":
					tempAnims.add(words[1]);
					break;
					
				// saves a point the animation will walk to
				case "move":
					int[] point = {Integer.parseInt(words[1]), Integer.parseInt(words[2])};
					tempPoints.add(point);
					break;
					
				case "dialog":
					
					break;
					
				case "repeat":
					
					break;
			}
		}
		
		// saves points
		this.positions = tempPoints.toArray(new int[0][0]);
		
		// resets position
		this.x = 16 * this.positions[0][0];
		this.y = 16 * this.positions[0][1];
		
		// saves the animations
		this.animNames = tempAnims.toArray(new String[0]);
		
		// sets the first animation
		this.currentAnim = AnimationStore.get().getAnimation(this.sprites, this.animNames[0]);
	}
	
	public void update() {

		this.currentAnim.update();
	}
	
	public void render(Graphics g) {
		
		Image sprite = this.currentAnim.getSprite();
		
		sprite.draw(
			this.x + this.currentAnim.getOffX(),
			this.y + this.currentAnim.getOffY()
		);
		
	}
}
