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
	
	// whether the object is being thrown
	// as opposed to picked up
	boolean isThrown;
	
	// the time it was thrown
	long throwTime;
	
	// how long it can be thrown before smashing
	int throwDuration = 1000;
	
	// how fast it is thrown
	int throwSpeed = 10;
	
	// where the pot was when it was thrown
	float startX;
	float startY;
	
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
		this.isThrown = false;
	}

	
	public void update() {
		
		if (this.isThrown) {
			
			int since = (int) (System.currentTimeMillis() - this.throwTime);
			
			double percent =  since / (float) this.throwDuration;

			this.x = (float) (this.startX + 16f * 3f * percent);
			this.y = (float) (this.startY + 16f * 3f * percent);
			

			if (since > this.throwDuration) {
				this.isThrown = false;
			}
			
		}
		
	}
	
	/*
	 * Tells the player to pick up the object 
	 * and sets the object to be picked up
	 */
	public void onPlayerInteract() {
		this.pickUpTime = System.currentTimeMillis();
	
		this.game.getPlayer().pickUpObject(this);
	}
	
	/*
	 * Starts the object being thrown
	 */
	public void startThrow (int direction) {
		
		this.isThrown = true;
		this.throwTime = System.currentTimeMillis();
		
		this.startX = this.x;
		this.startY = this.y;
	}
	
}
