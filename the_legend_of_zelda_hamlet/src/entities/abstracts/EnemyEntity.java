package entities.abstracts;

import music.SoundStore;

import org.newdawn.slick.Sound;

import sprites.Animation;
import sprites.AnimationStore;
import entities.Heart;
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
	public static final int STATE_DYING = 4;
	
	// the current state of the enemy
	protected int state = EnemyEntity.STATE_IDLE;
	
	// the health the enemy has
	protected int health;
	
	// the time the enemy was last hit
	protected long flinchTime;
	
	// how long it takes the enemy to recover from being hit
	protected int flinchDuration = 800;
	
	// how far the enemy is hit back
	protected int flinchSpeed = 40;
	
	// the angle at which the enemy should move back
	private double flinchAngle;
	
	// whether to move up or down
	// 1 for down, -1 for up
	private int flinchY;
	private int flinchX;
	
	// the enemy death animation
	private Animation deathAnim;
	
	// when the enemy started dying
	protected long deathTime;
	
	// how long the death animation plays
	protected long deathDuration = 500;
	
	// the sounds
	private Sound hitSound;
	private Sound deathSound;
	
	public EnemyEntity(int x, int y, int w, int h, Game g) {
		super(x, y, w, h, g);
		this.init();
	}

	public EnemyEntity(int x, int y, int s, Game g) {
		super(x, y, s, g);
		
		this.init();
	}

	private void init() {
		
		String deathAnimSheet = "assets/images/enemies/death";
		
		this.deathAnim = AnimationStore.get()
			.getAnimation(deathAnimSheet, "death");
		
		this.hitSound = SoundStore.get().getSound("assets/sfx/enemyhit.wav");
		this.deathSound = SoundStore.get().getSound("assets/sfx/enemydeath.wav");
	}
	
	// enemies must be able to update
	public abstract void update(int delta);
	
	// enemies must also be able to be hit
	public  void onHit() {
		
		if (this.state != EnemyEntity.STATE_FLINCHING && this.state != EnemyEntity.STATE_DYING) {
			
			// reduces health
			this.health -= 1;
			
			if (health <= 0) {
				
				this.deathSound.play();
				
				this.state = EnemyEntity.STATE_DYING;
				this.deathTime = System.currentTimeMillis();
				
				this.setAnim(this.deathAnim, (int)(this.deathDuration / this.deathAnim.getAnimLength()));
				this.loop = true;
				
			} else {
				
				this.hitSound.play();

				// sets state to flinching
				this.state = EnemyEntity.STATE_FLINCHING;
				this.flinchTime = System.currentTimeMillis();
				
				/*    finds theangle the enemy needs to follow    */
				
				// gets the different in position between it an the player
				Player p = this.game.getPlayer();
				
				float diffX = p.getX() - this.x;
				float diffY = p.getY() - this.y;
				double totalDiff = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY,  2));
				
				// gets the angle between it and the player
				double theta = Math.asin(diffY / totalDiff);
				
				// finds the opposite and by adding pi to the original
				this.flinchAngle = theta + Math.PI;
				
				// records whether to move left/right and up/down
				this.flinchX = 1;
				this.flinchY = 1;

				if (p.getX() > this.x) {
					
					this.flinchX = -1;
				}
				
				if (p.getY() > this.y) {
					
					this.flinchY = -1;
				}
			}
			
		}
	}
	
	/*
	 * Default death procedure
	 * Plays the death animation and then removes itself
	 */
	protected void die() {
		
		if (System.currentTimeMillis() - this.deathTime >= this.deathDuration) {
		
			this.onDeath();
		}
		
		this.animUpdate();
	}
	
	/*
	 * Code to execute on death
	 */
	protected void onDeath() {

		// spawns a heart
		this.game.addConsumable(new Heart(
				(int)this.x + this.w / 2, 
				(int)this.y + this.h / 2, 
				this.game));
		
		// removes self
		this.game.removeEnemy(this);
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
			
			// sets position
			float x = this.x + (float) (this.flinchX * Math.abs(this.flinchSpeed * delta / 1000f * Math.cos(this.flinchAngle)));
			float y = this.y + (float) (this.flinchY * Math.abs(this.flinchSpeed * delta / 1000f * Math.sin(this.flinchAngle)));
			
			// tries to move
			this.tryToMove(x, this.y);
			this.tryToMove(this.x, y);
			
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
