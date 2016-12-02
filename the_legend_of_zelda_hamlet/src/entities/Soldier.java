package entities;

import org.newdawn.slick.Graphics;

import sprites.Animation;
import sprites.AnimationStore;
import sprites.SpriteStore;
import entities.abstracts.EnemyEntity;
import entities.abstracts.Entity;
import game.Game;

/*
 * A soldier enemy who chases the player
 * 
 * May be one of three different difficulties:
 * Easy - Green
 * Medium - Blue
 * Hard - Red
 */
public class Soldier extends EnemyEntity {

	// the other state the soldier can be
	// when he is looking aruond at the end of hiss patrol
	public static final int STATE_LOOKING = 11;
	
	// its standing animations
	private Animation standUp;
	private Animation standSide;
	private Animation standDown;
	
	// its running animations
	private Animation runUp;
	private Animation runSide;
	private Animation runDown;

	// its attacking animations
	private Animation attackUp;
	private Animation attackSide;
	private Animation attackDown;
	
	// the points which it patrols to
	private int[][] patrols;
	
	// the current patrol the enemy is walking to
	private int currentPatrol;
	
	// the speed the soldier patrols at
	private int patrolSpeed = 20;
	
	// the speed the soldier chases the player at
	private int chaseSpeed = 100;
	
	// the time it takes to change frame when patrolling
	private int patrolDuration= 150;
	
	// 
	
	/*
	 * Initializes solder enemy and loads sprites
	 */
	public Soldier(int x, int y, int pX, int pY, Game g) {
		super(x, y, 16, 20, g);
		
		// the path to the image
		// will vary with different colors of soldier
		String imagePath = "assets/images/enemies/bluesoldier";
		
		// the string to the .sprites file
		// should remain constant unless the file is moved
		String spritesPath = "assets/images/enemies/soldier";
		// loads its sprites
		SpriteStore.get().loadSpriteSheet(
			imagePath,
			spritesPath);
		
		// loads its animations
		AnimationStore.get().loadAnimations(
				imagePath,
				spritesPath);

		this.standUp = AnimationStore.get().getAnimation(imagePath, "standup");
		this.standSide = AnimationStore.get().getAnimation(imagePath, "standside");
		this.standDown = AnimationStore.get().getAnimation(imagePath, "standdown");
		this.runUp = AnimationStore.get().getAnimation(imagePath, "runup");
		this.runSide = AnimationStore.get().getAnimation(imagePath, "runside");
		this.runDown = AnimationStore.get().getAnimation(imagePath, "rundown");
		this.attackUp = AnimationStore.get().getAnimation(imagePath, "attackup");
		this.attackSide = AnimationStore.get().getAnimation(imagePath, "attackside");
		this.attackDown = AnimationStore.get().getAnimation(imagePath, "attackdown");
		
		this.setAnim(this.runSide, 100);

		// sets up its point
		this.setUpPatrol((int) this.x, (int) this.y, pX * 16, pY * 16);
		
		// sets to walk around to start
		this.state = EnemyEntity.STATE_IDLE;
	}
	
	/*
	 * @see entities.abstracts.EnemyEntity#update(int)
	 */
	public void update(int delta) {
		
		switch (this.state) {
			case EnemyEntity.STATE_IDLE:
				this.patrol(delta);
		}
	}
	
	/*
	 * Moves the soldier to follow his patrol
	 */
	private void patrol(int delta) {

		// gets the point
		int[] point = this.patrols[currentPatrol];

		if (!this.canSeePlayer()) {
			
			if (this.isAtPoint(point[0], point[1], 1)) {
				
				// walks to its next point
				this.currentPatrol = (this.currentPatrol + 1) % this.patrols.length;
			}
				
			// walks to its point
			this.moveToPoint(point[0], point[1], this.patrolSpeed * delta / 1000f);
			
			switch (this.direction) {
				case Entity.DIR_DOWN:
					this.setAnim(this.runDown, this.patrolDuration);
					break;
				case Entity.DIR_UP:
					this.setAnim(this.runUp, this.patrolDuration);
					break;
				case Entity.DIR_LEFT:
					this.setAnim(this.runSide, this.patrolDuration);
					break;
				case Entity.DIR_RIGHT:
					this.setAnim(this.runSide, this.patrolDuration);
					break;
			}
			
		}
		
		this.animUpdate();
	}
	
	/*
	 * Checks if the soldier can see the player
	 */
	private boolean canSeePlayer() {
		return false;
	}
	
	/*
	 * Sets up patrol points from a box's coordinates
	 * the enemy will walk to each of the corners
	 */
	private void setUpPatrol(int x, int y, int pX, int pY) {
		
		int[][] patrols = {
			{
				x,
				y
			},
			{
				x,
				y + pY
			},
			{
				x + pX,
				y + pY
			},
			{
				x + pX,
				y
			}
		};
		
		this.patrols = patrols;
		
		this.currentPatrol = 1;
	}
	
}
