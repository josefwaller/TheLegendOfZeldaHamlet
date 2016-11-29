package entities;

import java.util.ArrayList;
import java.util.List;

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
	static final int CHANGE_ANIM = 2;
	static final int WAIT = 3;
	static final int REPEAT = 4;
	
	// what the current action is
	// uses the automated entity's static final variables
	int[] actions;
	
	// the index of the current action
	int actionIndex = 0;
	
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
	
	// the current animation
	Animation currentAnim;
	
	/*
	 * creates a new AutomatedEntity from the animation file name
	 */
	public AutomatedEntity (String filename, Game g) {
		
		// calls constructor
		super (0, 0, 16, g);
		
		// reads the file
		String fileContents = Game.readFile("assets/anim/" + filename);
		
		// records the contents of the file
		String[] actions = fileContents.toLowerCase().split("\\r?\\n");
		
		// temporary storage of the entity's arrays
		// because we do not yet know the size of the arrays
		// these will be converted to a normal array after the for loop
		List<Integer> tempActions = new ArrayList<Integer>();
		ArrayList<int[]> tempPoints = new ArrayList<int[]>();
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
					
					// adds an action
					tempActions.add(AutomatedEntity.CHANGE_ANIM);
					tempAnims.add(words[1]);
					break;
					
				// saves a point the animation will walk to
				case "move":
					
					// adds an action
					tempActions.add(AutomatedEntity.MOVE);
					
					int[] point = {Integer.parseInt(words[1]), Integer.parseInt(words[2])};
					tempPoints.add(point);
					break;
					
				case "dialog":
					
					// adds an action
					tempActions.add(AutomatedEntity.DIALOG);
					
					break;
					
				case "repeat":
					
					// adds an action
					tempActions.add(AutomatedEntity.REPEAT);
					
					break;
			}
		}
		
		// saves points
		this.positions = tempPoints.toArray(new int[0][0]);
		
		// resets position
		this.x = 16 * this.positions[0][0];
		this.y = 16 * this.positions[0][1];
		
		// converts the ArrayLists into arrays for storage
		this.animNames = tempAnims.toArray(new String[0]);
		
		this.actions = new int[tempActions.size()];
		// has to do the Integer ArrayLists in a for loop because
		// Java can't cast from Integer to int
		for (int i = 0; i < tempActions.size(); i++) {
			this.actions[i] = tempActions.get(i);
		}
		
		// sets the current animation
		this.currentAnim = AnimationStore.get().getAnimation(this.sprites, this.animNames[0]);
	}
	
	public void update() {

		this.currentAnim.update();
		
		switch (this.actions[this.actionIndex]) {
			case AutomatedEntity.MOVE:
				this.moveToNextPoint();
				break;
		}
	}
	
	public void render(Graphics g) {
		
		Image sprite = this.currentAnim.getSprite();
		
		sprite.draw(
			this.x + this.currentAnim.getOffX(),
			this.y + this.currentAnim.getOffY()
		);
		
	}
	
	/*
	 * Moves the automated entity near to the net point
	 */
	private void moveToNextPoint() {
		
		
		
	}
}
