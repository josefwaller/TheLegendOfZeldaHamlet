package entities;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import sprites.Animation;
import sprites.AnimationStore;
import entities.abstracts.Entity;
import entities.abstracts.MovingEntity;
import game.Game;

/*
 * An entity that moves and acts according to a file. To be
 * used during animated cutscenes in the game.
 */
public class AutomatedEntity extends MovingEntity {

	// the different things the animation can do
	static final int MOVE = 0;
	static final int DIALOG = 1;
	static final int CHANGE_ANIM = 2;
	static final int WAIT = 3;
	
	// what the current action is
	// uses the automated entity's static final variables
	private int[] actions;
	
	// the index of the current action
	private int actionIndex = 0;
	
	// the different positions the animation will walk to
	private int[][] positions;
	
	// the index of the current position
	private int positionIndex;
	
	// the different dialogs the animation will say
	private String[] dialog;
	
	// the index of the current dialog
	private int dialogIndex;
	
	// the name of the spritesheet this animation uses
	private String sprites;
	
	// the name of the different sprite animatons to change between
	private String[] animNames;
	
	// the current index of the animation in animNames;
	private int animIndex;
	
	// the current animation
	private Animation currentAnim;
	
	// the speed at which the entity moves
	private int speed = 20;
	
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
					
				// saves the base speed the entity will move at
				// Note: individual movements may have their own speeds, which will override this one
				case "speed":
					
					this.speed = Integer.parseInt(words[1]);
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
					
					int[] point = {Integer.parseInt(words[1]), Integer.parseInt(words[2]), 0};
					if (words.length == 4) {
						point[2] = Integer.parseInt(words[3]);
					}
					tempPoints.add(point);
					break;
					
				case "dialog":
					
					// adds an action
					tempActions.add(AutomatedEntity.DIALOG);
					
					break;
					
				case "init_anim":
					
					// sets the initial animation
					this.currentAnim = AnimationStore.get().getAnimation(this.sprites, words[1]);
					break;
			}
		}
		
		// saves points
		this.positions = tempPoints.toArray(new int[0][0]);
		
		// resets position
		this.x = this.positions[0][0];
		this.y = this.positions[0][1];
		
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
		
		// sets all indexes to 0
		this.actionIndex = 0;
		this.positionIndex = 0;
		
		// action index is set to -1, so that when it changes it will start at 0
		this.animIndex = -1;

		System.out.println(this.actions.length);
		System.out.println(this.animNames.length);
	}
	
	/*
	 * Updates the entity
	 */
	public void update(int delta) {
		
		// checks the entity hasn't run out of things to do
		if (this.actionIndex < this.actions.length) {

			// updates the current animation
			this.currentAnim.update();
			
			// performs different things based on the type of action
			switch (this.actions[this.actionIndex]) {
			
				case AutomatedEntity.MOVE:
					this.moveToNextPoint(delta);
					break;
					
				case AutomatedEntity.CHANGE_ANIM:
					
					this.changeAnimation();
					break;
				default:
					System.out.println(this.actions[this.actionIndex]);
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * @see entities.abstracts.Entity#render(org.newdawn.slick.Graphics)
	 */
	public void render(Graphics g) {
		
		Image sprite = this.currentAnim.getSprite();
		
		sprite.draw(
			this.x + this.currentAnim.getOffX(),
			this.y + this.currentAnim.getOffY()
		);
		
	}
	
	/*
	 * Changes the entity's animation
	 */
	private void changeAnimation() {
		
		this.animIndex++;
		this.currentAnim = AnimationStore.get().getAnimation(this.sprites, this.animNames[this.animIndex]);
		
		this.actionIndex++;
	}
	
	/*
	 * Moves the automated entity near to the net point
	 */
	private void moveToNextPoint(int delta) {
		
		int[] nextPoint = this.positions[this.positionIndex];
		
		// checks if the entity's center is within 2 pixels of the point
		if (this.closeToPoint(nextPoint[0], nextPoint[1])) {

			this.actionIndex++;
			this.positionIndex++;
		} else {
			
			int speed = this.speed;
			
			// checks if the movement has a custom speed
			if (nextPoint[2] != 0) {
				speed = nextPoint[2];
			}
			
			this.moveToPoint(nextPoint[0], nextPoint[1], speed * delta / 1000f);
		}
		
	}
	
	/*
	 * Checks if the entity's center is within 2 pixels of the point
	 */
	private boolean closeToPoint(int x, int y) {
		
		// how far the x and y coordinates can be off
		int radius = 2;
		
		if (this.x + radius >= x) {
			if (this.x <= x + radius) {
				if (this.y + radius >= y) {
					if (this.y <= y + radius) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
