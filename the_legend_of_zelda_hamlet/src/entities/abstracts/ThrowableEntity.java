package entities.abstracts;

import game.Game;

/*
 * Entities which can be picked up and thrown
 * Pots, Bushes, rocks, etc
 */
public abstract class ThrowableEntity extends InteractiveEntity {

	/*
	 * Default Constructor
	 */
	public ThrowableEntity(int x, int y, int s, Game g) {
		super(x, y, s, g);
	}

	/*
	 * Tells the player to pick up the object 
	 * and sets the object to be picked up
	 */
	public void onPlayerInteract() {
		
	}
	
}
