package entities;

import java.util.ArrayList;

import sprites.Animation;
import sprites.AnimationStore;
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
	private String[] dialogs;
	
	// the index of the current dialog
	private int dialogIndex;
	
	// the name of the spritesheet this animation uses
	private String sprites;
	
	// the name of the different sprite animatons to change between
	private String[] animNames;
	
	// the current index of the animation in animNames;
	private int animIndex;
	
	// how long to wait in seconds
	private int[] waitTimes;
	private int waitIndex;
	
	// whether the entity is currently waiting
	private boolean isWaiting;
	
	// the time the entity started waiting
	private long waitTime;
	
	// the speed at which the entity moves
	private int speed = 20;
	
	// the duration between switching frames
	private int duration = 100;
	
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
		ArrayList<Integer> tempActions = new ArrayList<Integer>();
		ArrayList<int[]> tempPoints = new ArrayList<int[]>();
		ArrayList<String> tempAnims = new ArrayList<String>();
		ArrayList<String> tempDialogs = new ArrayList<String>();
		ArrayList<Integer> tempWait = new ArrayList<Integer>();
		
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
					
					// reads the dialog file
					String dialog = Game.readFile("assets/dialog/" + words[1]);
					tempDialogs.add(dialog);
					break;
					
				case "init_anim":
					
					// sets the initial animation
					this.setAnim(AnimationStore.get().getAnimation(this.sprites, words[1]), this.duration);
					break;
					
				case "wait":
					
					// adds how long to wait
					tempWait.add(Integer.parseInt(words[1]));
					
					// adds an action
					tempActions.add(AutomatedEntity.WAIT);
			}
		}
		
		// saves points
		this.positions = tempPoints.toArray(new int[0][0]);
		
		// resets position
		this.x = this.positions[0][0];
		this.y = this.positions[0][1];
		
		// converts the ArrayLists into arrays for storage
		this.animNames = tempAnims.toArray(new String[0]);
		this.dialogs = tempDialogs.toArray(new String[0]);
		this.actions = this.intArrayListToArray(tempActions);
		this.waitTimes = this.intArrayListToArray(tempWait);
		
		// sets the current animation
		this.setAnim(AnimationStore.get().getAnimation(this.sprites, this.animNames[0]), this.duration);
		
		// set indexes to 0
		this.actionIndex = 0;
		this.positionIndex = 0;
		this.animIndex = 0;
		this.waitIndex = 0;
	}
	
	/*
	 * Updates the entity
	 */
	public void update(int delta) {
		
		// checks the entity hasn't run out of things to do
		if (this.actionIndex < this.actions.length) {

			// updates the current animation
			this.animUpdate();
			
			// performs different things based on the type of action
			switch (this.actions[this.actionIndex]) {
			
				case AutomatedEntity.MOVE:
					this.moveToNextPoint(delta);
					break;
					
				case AutomatedEntity.CHANGE_ANIM:
					this.changeAnimation();
					break;
					
				case AutomatedEntity.DIALOG:
					this.startDialog();
					break;
					
				case AutomatedEntity.WAIT:
					this.pause();
					break;
					
				default:
					System.out.println(this.actions[this.actionIndex]);
			}
		}
	}
	
	/*
	 * Pauses the entity for a certain amount of time
	 */
	private void pause() {
		
		// checks if the entity was already waiting
		if (!this.isWaiting){
			
			// since the entity wasn't waiting , this must be 
			// the first time pause() is called for this pause
			// so it sets up to wait
			this.isWaiting = true;
			this.waitTime = System.currentTimeMillis();
		}
		
		// check if it is done waiting
		if (System.currentTimeMillis() - this.waitTime >= this.waitTimes[this.waitIndex]) {
			
			this.isWaiting = false;
			this.waitIndex++;
			this.actionIndex++;
		}
	}
	
	/*
	 * Turns an ArrayList of Integers into an int array
	 */
	private int[] intArrayListToArray(ArrayList<Integer> x) {
		
		int[] toReturn = new int[x.size()];
		
		for (int i = 0; i < x.size(); i++) {
			toReturn[i] = x.get(i);
		}
		
		return toReturn;
	}
	
	/*
	 * Starts dialog
	 */
	private void startDialog() {
		
		// starts the dialog
		this.game.startDialog(this.dialogs[this.dialogIndex]);
		
		// advances to next action
		this.actionIndex++;
		this.dialogIndex++;
		
	}
	
	/*
	 * Changes the entity's animation
	 */
	private void changeAnimation() {
		
		// sets animation
		String animName =  this.animNames[this.animIndex];
		Animation thisAnim = AnimationStore.get().getAnimation(this.sprites, animName);
		this.setAnim(thisAnim, this.duration);

		// moves to next action
		this.animIndex++;
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
