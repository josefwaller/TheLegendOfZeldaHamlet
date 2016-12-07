package entities.abstracts;

import game.Game;

/*
 * Superclass for all enemies in the game
 * Includes different states which are used by 
 * enemies to track their movements
 */
public abstract class EnemyEntity extends MovingEntity {

	// different states for enemies to use
	public static final int STATE_IDLE = 0;
	public static final int STATE_FLINCHING = 1;
	public static final int STATE_STUNNED = 2;
	public static final int STATE_CHASING = 3;
	
	// the current state of the enemy
	protected int state = EnemyEntity.STATE_IDLE;
	
	// the health the enemy has
	protected int health;
	
	// the time the enemy was last hit
	protected long flinchTime;
	
	// how long it takes the enemy to recover from being hit
	protected int flinchDuration = 800;
	
	public EnemyEntity(int x, int y, int w, int h, Game g) {
		super(x, y, w, h, g);
	}

	public EnemyEntity(int x, int y, int s, Game g) {
		super(x, y, s, g);
	}

	// enemies must be able to update
	public abstract void update(int delta);
	
	// enemies must also be able to be hit
	public  void onHit() {
		
		this.health -= 1;
		this.state = EnemyEntity.STATE_FLINCHING;
		this.flinchTime = System.currentTimeMillis();
	}
	
	/*
	 * Default flinch procedure
	 * The enemy is hit away from the player and flashes
	 * between its normal colors and inversed colors
	 */
	protected void flinch()  {
		
		// checks if the enemy is done flinching
		if (System.currentTimeMillis() - this.flinchTime >= this.flinchDuration) {
			this.state = EnemyEntity.STATE_IDLE;
		}
	}
}
