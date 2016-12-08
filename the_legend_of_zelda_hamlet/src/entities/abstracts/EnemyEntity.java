package entities.abstracts;

import org.newdawn.slick.Graphics;

import entities.Player;
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
	
	// how far the enemy is hit back
	protected int flinchDistance = 40;
	
	// where the enemy was hit
	private float flinchStartX;
	private float flinchStartY;
	
	// where the enemy is hit to
	protected float flinchX;
	protected float flinchY;
	
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
		
		if (this.state != EnemyEntity.STATE_FLINCHING) {

			
			// reduces health
			this.health -= 1;
			
			// sets state to flinching
			this.state = EnemyEntity.STATE_FLINCHING;
			this.flinchTime = System.currentTimeMillis();
			
			/*    finds the point the enemy needs to move to    */
			
			// records the original point
			this.flinchStartX = this.x;
			this.flinchStartY = this.y;
			
			// gets the different in position between it an the player
			Player p = this.game.getPlayer();
			float diffX = p.getX() - this.x;
			float diffY = p.getY() - this.y;
			double totalDiff = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY,  2));
			
			// gets the angle between it and the player
			double theta = Math.asin(diffY / totalDiff);
			
			// finds the opposite and by adding pi to the original
			double oppositeTheta = theta + Math.PI;
			
			// gets the x and y for that point relative to the enemy
			this.flinchX = (float) Math.abs(this.flinchDistance * Math.cos(oppositeTheta));
			this.flinchY = (float) Math.abs(this.flinchDistance * Math.sin(oppositeTheta));

			// gets the x and y relative to the window
			if (p.getX() > this.x) {
				
				this.flinchX *= -1;
			}
			
			if (p.getY() > this.y) {
				
				this.flinchY *= -1;
			}
			
		}
	}
	
	/*
	 * Default flinch procedure
	 * The enemy is hit away from the player and flashes
	 * between its normal colors and inversed colors
	 */
	protected void flinch(long delta)  {
		
		long since = System.currentTimeMillis() - this.flinchTime;
		
		// checks if the enemy is done flinching
		if (since >= this.flinchDuration) {
			this.state = EnemyEntity.STATE_IDLE;
			super.setFilter(1, 1, 1);
			
		} else if (since <= this.flinchDuration / 2f) {
			
			double percent = (since / (double)this.flinchDuration) * 2f;
			
			// sets position
			this.x = (float) (this.flinchStartX + (this.flinchX * percent));
			this.y = (float) (this.flinchStartY + (this.flinchY * percent));
		
			// sets the color
			int stage = (int) Math.floor(since / 10f);
			
			switch (stage % 3) {
				case 0:
					super.setFilter(1, 0, 0);
					break;
				case 1:
					super.setFilter(0, 1, 0);
					break;
				case 2:
					super.setFilter(0, 0, 1);
					break;
					
			}
		} else {
			super.setFilter(1, 1, 1);
		}
	}
}
