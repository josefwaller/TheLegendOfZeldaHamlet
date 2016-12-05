package entities.abstracts;

import game.Game;

/*
 * Entities which can be picked up and thrown
 * Pots, Bushes, rocks, etc
 */
public abstract class ThrowableEntity extends InteractiveEntity {

	// the time it was picked up
	long pickUpTime;
	
	// the x and y offsets to draw when it is being picked up
	int[][] offsets;
	
	/*
	 * Default Constructor
	 */
	public ThrowableEntity(int x, int y, int s, Game g) {
		super(x, y, s, g);
		
		
		// sets up where to move when being thrown
		int[][] offsets = {
			{
				0,
				0
			},
			{
				1,
				1
			}
		};
		
		this.offsets = offsets;
	}

	/*
	 * Tells the player to pick up the object 
	 * and sets the object to be picked up
	 */
	public void onPlayerInteract() {
		this.pickUpTime = System.currentTimeMillis();
	
		this.game.getPlayer().pickUpObject(this);
	}
	
}
