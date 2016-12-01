package entities;

import org.newdawn.slick.Graphics;

import sprites.Animation;
import sprites.AnimationStore;
import sprites.SpriteStore;
import entities.abstracts.EnemyEntity;
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
	int[][] patrols;
	
	// the current patrol the enemy is walking to
	int currentPatrol;
	
	// the speed the soldier patrols at
	int patrolSpeed = 50;
	
	// the speed the soldier chases the player at
	int chaseSpeed = 100;
	
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
	}
	
	/*
	 * @see entities.abstracts.EnemyEntity#update(int)
	 */
	public void update(int delta) {
		
		// gets the point
		int[] point = this.patrols[currentPatrol];

		if (!this.isAtPoint(point[0], point[1], 1)) {
			
			// walks to its point
			this.moveToPoint(point[0], point[1], this.patrolSpeed * delta / 1000f);
			
		} else {
			
			// walks to its next point
			this.currentPatrol = (this.currentPatrol + 1) % this.patrols.length;
		}
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
