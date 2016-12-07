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
		System.out.println("I'm Hit!");
	}
}
