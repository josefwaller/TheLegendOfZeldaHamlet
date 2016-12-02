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
	
	// its looking animations
	private Animation lookUp;
	private Animation lookSide;
	private Animation lookDown;
	
	// the points which it patrols to
	private int[][] patrols;
	
	// the current patrol the enemy is walking to
	private int currentPatrol;
	
	// the speed the soldier patrols at
	private int patrolSpeed = 20;
	
	// the time it takes to change frame when patrolling
	private int patrolDuration= 150;
	
	// the speed the soldier chases the player at
	private int chaseSpeed = 80;
	
	// the time between changing sprites when chasing the player
	private int chaseDuration = 50;
	
	// the duration of the looking animations
	private int lookDuration = 2000;
	
	// the time the entity started looking
	// used to time looking around
	private long lookTime;
	
	// the direction the entity is looking in
	// used to determine if e can see the player
	private int lookDirection;
	
	// the distance this enemy can look
	private int lookDistance = 200;
	
	/*
	 * Initializes solder enemy and loads sprites
	 */
	public Soldier(int x, int y, int pX, int pY, Game g) {
		super(x, y, 16, 20, g);
		
		// the path to the image
		// will vary with different colors of soldier
		String imagePath = "assets/images/enemies/redsoldier";
		
		// the string to the .sprites file
		// should remain constant unless the file is moved
		String spritesPath = "assets/images/enemies/soldier";
		
		// loads its sprites
		SpriteStore.get().loadSpriteSheet(
			imagePath,
			spritesPath);
		
		// gest the animation for easy reference
		AnimationStore a = AnimationStore.get();
		
		// loads its animations
		AnimationStore.get().loadAnimations(
				imagePath,
				spritesPath);

		this.standUp = a.getAnimation(imagePath, "standup");
		this.standSide = a.getAnimation(imagePath, "standside");
		this.standDown = a.getAnimation(imagePath, "standdown");
		this.runUp = a.getAnimation(imagePath, "runup");
		this.runSide = a.getAnimation(imagePath, "runside");
		this.runDown = a.getAnimation(imagePath, "rundown");
		this.attackUp = a.getAnimation(imagePath, "attackup");
		this.attackSide = a.getAnimation(imagePath, "attackside");
		this.attackDown = a.getAnimation(imagePath, "attackdown");
		this.lookUp = a.getAnimation(imagePath, "lookup");
		this.lookSide = a.getAnimation(imagePath, "lookside");
		this.lookDown = a.getAnimation(imagePath, "lookdown");
		
		this.setAnim(this.runSide, this.patrolDuration);

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
				break;
				
			case EnemyEntity.STATE_CHASING:
				this.chase(delta);
				break;
				
			case Soldier.STATE_LOOKING:
				this.lookAround();
				break;
		}
	}
	
	/*
	 * Chases the player
	 */
	private void chase(int delta) { 
		
		if (!this.canSeePlayer()) {
			this.state = Soldier.STATE_LOOKING;
		} else {
			
			Player p = this.game.getPlayer();
			int x = (int) (p.getX());
			int y = (int) (p.getY());
			
			this.moveToPoint(x, y, this.chaseSpeed * delta / 1000f);
			
			this.setRunAnim(this.chaseDuration);
		}
		
	}
	
	/*
	 * Makes the entity look left and right to see if it can see the player
	 */
	private void lookAround() {
		
		// when looking around, the enemy looks to his left,
		// then back, then his right, then back. So the
		// enemy finds the directions in terms of absolute 
		// position and later sets lookDirection
		
		// the direction when the enemy turns his head left
		int dirLeft = 0;
		
		// the direction when the enemy turns his head right
		int dirRight = 0;
		
		switch (this.direction) {
		
			case Entity.DIR_DOWN:
				this.setAnim(
					this.lookDown, 
					this.lookDuration / this.lookDown.getAnimLength());
				// sets direction
				dirLeft = Entity.DIR_RIGHT;
				dirRight = Entity.DIR_LEFT;
				
				break;
				
			case Entity.DIR_RIGHT:				
			case Entity.DIR_LEFT:
				this.setAnim(
						this.lookSide, 
						this.lookDuration / this.lookSide.getAnimLength());

				// sets direction
				dirLeft = Entity.DIR_UP;
				dirRight = Entity.DIR_DOWN;
				break;
				
			case Entity.DIR_UP:
				this.setAnim(
						this.lookUp, 
						this.lookDuration / this.lookUp.getAnimLength());
				
				// sets direction
				dirLeft = Entity.DIR_LEFT;
				dirRight = Entity.DIR_RIGHT;
				break;
		}
		
		// gets the time since the start of the looking animation
		long since = System.currentTimeMillis() - this.lookTime;
		
		if (since >= this.lookDuration) {
			
			this.state = EnemyEntity.STATE_IDLE;
			this.loop = true;
		} else {
			
			// sets lookDirection based on how long the animation has been going
			// sets it so that it matches the enemy's head
			switch ((int)Math.floor((since) / (this.lookDuration * 4))) {
			
				case 0:
					this.lookDirection = dirLeft;
					break;
					
				case 2:
					this.lookDirection = dirRight;
					break;
					
				case 1:
				case 3:
					this.lookDirection = this.direction;
					break;
			}
			
		}
		
		this.animUpdate();
	}
	
	/*
	 * Moves the soldier to follow his patrol
	 */
	private void patrol(int delta) {

		// gets the point
		int[] point = this.patrols[currentPatrol];
		
		// the enemy is always looking ahead when patroling
		// so sets his looking direction to directin
		this.lookDirection = this.direction;

		if (this.canSeePlayer()) {
			
			// sets to chase the player
			this.state = EnemyEntity.STATE_CHASING;
			
		} else {
			
			if (this.isAtPoint(point[0], point[1], 0.01f)) {
				
				// sets to walk to next point
				this.currentPatrol = (this.currentPatrol + 1) % this.patrols.length;
			
				// sets to look around
				this.state = Soldier.STATE_LOOKING;
				this.lookTime = System.currentTimeMillis();
				this.loop = false;
				
			} else {

				// walks to its point
				this.moveToPoint(point[0], point[1], this.patrolSpeed * delta / 1000f);
				
				this.setRunAnim(this.patrolDuration);
			}
			
		}
	}
	
	/*
	 * Checks if the soldier can see the player
	 */
	private boolean canSeePlayer() {
		
		// gets the player
		Player p = this.game.getPlayer();

		// first checks that the player is close enough
		double disX = this.x - p.getX();
		double disY = this.y - p.getY();
		
		double totalDis = Math.sqrt(Math.pow(disX, 2) + Math.pow(disY, 2));
		
		if (totalDis < this.lookDistance) {
			
			// checks if it can see the player based on what direction it is looking			
			switch (this.lookDirection) {
				case Entity.DIR_DOWN:
					if (p.getY() > this.y) {
						return true;
					}
					break;
					
				case Entity.DIR_UP:
					if (p.getY() < this.y) {
						return true;
					}
					break;
					
				case Entity.DIR_LEFT:
					if (p.getX() < this.x) {
						return true;
					}
					break;
					
				case Entity.DIR_RIGHT:
					if (p.getX() > this.x) {
						return true;
					}
					break;
			}
			
		}
		
		
		return false;
	}
	
	/*
	 * Sets the animation for when the enemy is just walking
	 * Based on which direction it is facing
	 */
	private void setRunAnim(int dur) {
		
		switch (this.direction) {
		
			case Entity.DIR_DOWN:
				this.setAnim(
					this.runDown,
					dur);
				
				break;
				
			case Entity.DIR_RIGHT:				
			case Entity.DIR_LEFT:
				this.setAnim(
					this.runSide,
					dur);
				break;
				
			case Entity.DIR_UP:
				this.setAnim(
					this.runUp,
					dur);
				break;
		}
		
		this.animUpdate();
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
