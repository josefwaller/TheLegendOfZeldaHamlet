package entities;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import sprites.Animation;
import sprites.AnimationStore;
import sprites.SpriteStore;
import util.Hitbox;
import entities.abstracts.EnemyEntity;
import entities.abstracts.Entity;
import game.Game;

/*
 * A soldier who wields a mace
 * Used as a final boss
 */
public class MaceSoldier extends EnemyEntity {
	
	// the different state the mace knight can be in
	// in addition to the states in EnemtEntity
	public static final int STATE_THROWING = 10;
	public static final int STATE_WAITING = 11;
	public static final int STATE_PULLING = 12;

	// the initial spawning point of the MaceSoldier
	// he should always hang around here
	private int targetX;
	private int targetY;
	
	// how far away he can be from the target points
	// without moving back to them
	private int maxDistance = 5;
	
	// how fast the mace soldier moves
	private int speed = 20;
	
	// animations
	private Animation standUp;
	private Animation standSide;
	private Animation standDown;
	private Animation walkUp;
	private Animation walkSide;
	private Animation walkDown;
	
	// the ball images
	private Image ballSprite;
	private Image chainSprite;
	
	// where the mace knight's hand is relative to the mace knight
	private int handX;
	private int handY;
	
	// where the ball is relative to the mace knight
	private float ballX;
	private float ballY;
	
	// the size of the ball
	private int ballS;
	
	// the radius of the ball's orbit around the knight
	private float ballRadius = 20;
	
	// the current angle the ball is at
	private double ballAngle;
	
	// the speed at which the ball moves
	private int ballSpeed = 150;
	
	// the number of chain links between the knight and the ball
	private int chainLinks = 7;
	
	// the time between switching frames when walking
	private int walkDuration = 300;
	
	// the time since the mace knight last attacked
	private long lastAttackTime;
	
	// the last time the mace knight threw his ball
	private long lastThrowTime;
	
	// the delay between attacks
	private int attackDelay = 2000;
	
	// where the player was when the knight started its attack
	private float playerX;
	private float playerY;
	
	// the angle the ball needs to move to hit the player coords
	private double playerAngle;
	
	// the time the soldier started waiting
	// to pull the mace back in
	private long waitTime;
	
	// how long the soldier waits
	private int waitDuration = 1000;
	
	public MaceSoldier (int x, int y, Game g) {
		
		super(x, y, 16, 24, g);
		
		this.handX = 10;
		this.handY = 10;
		
		// sets ball values
		this.ballX = 0;
		this.ballY = this.ballRadius;
		this.ballAngle = Math.PI / 2;
		
		// records where the mace knight should return to
		this.targetX = x;
		this.targetY = y;
		
		// loads animations
		AnimationStore a = AnimationStore.get();
		
		String sprites = "assets/images/enemies/maceknight";

		this.standUp = a.getAnimation(sprites, "standup");
		this.standSide = a.getAnimation(sprites, "standside");
		this.standDown = a.getAnimation(sprites, "standDown");
		this.walkUp = a.getAnimation(sprites, "walkup");
		this.walkSide = a.getAnimation(sprites, "walkside");
		this.walkDown = a.getAnimation(sprites, "walkdown");
		
		// loads ball and chain sprites
		this.ballSprite = SpriteStore.get().loadSpriteSheet(sprites).getSprite("/Misc/Ball");
		this.chainSprite = SpriteStore.get().loadSpriteSheet(sprites).getSprite("/Misc/Chain");
		
		this.ballS = ballSprite.getWidth();
		
		this.setAnim(walkUp, this.walkDuration);
		
		// adds a hitbox
		this.addHitbox(1,  0,  13,  22);
		
		// sets health
		this.health = 10;
		
		this.lastAttackTime = System.currentTimeMillis();
	}
	
	public void update(int delta) {
		
		// sets hand direction
		switch (this.direction) {
		
			case Entity.DIR_DOWN:
				this.handX = 16;
				this.handY = 8;
				break;
				
			case Entity.DIR_RIGHT:
				
				this.handX = 6;
				this.handY = 10;
				break;
				
			case Entity.DIR_LEFT:
				
				this.handX = 10;
				this.handY = 10;
				break;
				
			case Entity.DIR_UP:
				this.handX = -1;
				this.handY = 8;
		}
		
		switch (this.state) {
		
			case EnemyEntity.STATE_FLINCHING:
				this.flinch(delta);
				break;
				
			case EnemyEntity.STATE_DYING:
				this.die();
				break;
				
			case EnemyEntity.STATE_IDLE:
				this.idle(delta);
				break;
				
			case MaceSoldier.STATE_THROWING:
				this.throwBall(delta);
				break;
				
			case MaceSoldier.STATE_WAITING:
				
				if (System.currentTimeMillis() - this.waitTime >= this.waitDuration) {
					this.state = MaceSoldier.STATE_PULLING;
				}
				break;
				
			case MaceSoldier.STATE_PULLING:
				
				this.pullBall(delta);
				
				break;
		}
		
		this.animUpdate();
	}
	
	/*
	 * Renders the mace knight, ball and chain
	 */
	public void render(Graphics g) {
		
		// draws the knight
		super.render(g);
		
		// draws the chain
		for (int i = 0; i < this.chainLinks; i++) {
			
			this.chainSprite.draw(
				this.x + this.handX + ((this.ballX + this.ballS / 2) * i / this.chainLinks) - this.chainSprite.getWidth() / 2,
				this.y + this.handY + ((this.ballY + this.ballS / 2) * i / this.chainLinks) - this.chainSprite.getHeight() / 2);
			
		}

		// draws the ball
		this.ballSprite.draw(this.x + this.handX + this.ballX, this.y + this.handY + this.ballY);
		
		if (this.game.isDebug()) {

			// draws a white rectangle around the ball
			g.setColor(Color.white);
			g.drawRect(this.x + this.handX + this.ballX, this.y + this.handY + this.ballY, this.ballS, this.ballS);
		}
	}
	
	/*
	 * Pulls the ball back in after it has been thrown
	 */
	private void pullBall(int delta) {
		
		// it wants the ball to be set back to the top
		float targetX = this.x + this.handX;
		float targetY = this.y + this.handY - this.ballRadius;

		if (this.ballIsAtPoint(targetX, targetY, 2)) {
			
			this.state = EnemyEntity.STATE_IDLE;
			this.ballAngle = Math.PI * 3/2;
			this.lastAttackTime = System.currentTimeMillis();
		
		} else {
			
			this.moveBallTowardsPoint(targetX, targetY, delta);
		}
		
	}
	
	/*
	 * Checks if the ball is at a point
	 */
	private boolean ballIsAtPoint(float x, float y, int error) {
		
		float ballX = this.x + this.handX + this.ballX;
		float ballY = this.y + this.handY + this.ballY;
		
		if (ballX < x + error) {
			if (ballX + error > x) {
				if (ballY < y + error) {
					if (ballY + error > y) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/*
	 * Moves the ball towards a point
	 */
	private void moveBallTowardsPoint(float x, float y, int delta) {
		
		// gets the absolute ball coordinates
		float ballX = this.x + this.handX + this.ballX;
		float ballY = this.y + this.handY + this.ballY;
		
		float hyp = (float) Math.sqrt(Math.pow(ballX - x, 2) + Math.pow(ballY - y, 2));

		// gets the angle
		double theta = Math.asin((ballY - y) / hyp);
		
		int factorX = 1;
		int factorY = 1;
		
		if (ballX > x) {
			factorX = -1;
		}
		if (ballY > y) {
			factorY = -1;
		}
		
		// moves the ball along the angle
		this.ballX += factorX * Math.abs(this.ballSpeed * (delta / 1000f) * Math.cos(theta));
		this.ballY += factorY * Math.abs(this.ballSpeed * (delta / 1000f) * Math.sin(theta));
	}
	
	/*
	 * Moves the ball towards where the player was when
	 * the mace knight started attacking
	 */
	private void throwBall(int delta) {	
			
		this.moveBallTowardsPoint(this.playerX, this.playerY, delta);
		
		// checks if the ball has reached the player's coords
		float ballX = this.x + this.handX + this.ballX;
		
		if ((ballX > this.playerX && this.x < this.playerX) || (ballX < this.playerX && this.x > this.playerX)) {
			this.state = MaceSoldier.STATE_WAITING;
			this.waitTime = System.currentTimeMillis();
		}
	
		this.checkForBallCollision();
	}
	
	/*
	 * Moves the mace soldier back to its point if it's far enough away
	 * Otherwise moves it to face the player.
	 */
	private void idle (int delta) {
		
		Player p = this.game.getPlayer();

		// checks if it hits the player
		if (this.collidesWithEntity(p)) {
			p.onHit();
		}
		
		// checks if it is within range of its target coordinates
		int disX = (int) (this.x - this.targetX);
		int disY = (int) (this.y - this.targetY);
		
		int distance = (int) Math.sqrt(Math.pow(disX, 2) + Math.pow(disY, 2));
		
		if (distance < this.maxDistance) {
			
			if (p.getX() < this.x) {
				this.direction = Entity.DIR_LEFT;
				
			} else {
				this.direction = Entity.DIR_RIGHT;
				
			}
			
			this.setAnim(this.standSide, 0);
			
			// checks if it should attack
			if (System.currentTimeMillis() - this.lastAttackTime >= this.attackDelay) {

				if (this.ballAngle < Math.PI * 1/2) {

					this.state = MaceSoldier.STATE_THROWING;
					this.lastAttackTime = System.currentTimeMillis();
					
					this.playerX = p.getX();
					this.playerY = p.getY();
					
					// gets the balls absolute coordinates
					float ballX = this.x + this.handX + this.ballX;
					float ballY = this.y + this.handY + this.ballY;
					
					float hyp = (float) Math.sqrt(Math.pow(ballX - this.playerX, 2) + Math.pow(ballY - this.playerY, 2));
					
					this.playerAngle = Math.asin((ballY - this.playerY) / hyp);
				}
			}
			
		} else {
			
			this.moveToPoint(this.targetX, this.targetY, 20 * delta / 1000f);
			
			switch (this.direction) {
			
				case Entity.DIR_DOWN:
					this.setAnim(this.walkDown, this.walkDuration);
					break;
					
				case Entity.DIR_LEFT:
				case Entity.DIR_RIGHT:
					this.setAnim(this.walkSide, this.walkDuration);
					break;
					
				case Entity.DIR_UP:
					this.setAnim(this.walkUp, this.walkDuration);
					break;
			}
		}
		
		this.spinBall(delta, this.ballSpeed);
		this.checkForBallCollision();
	}
	
	/*
	 * Changes the ball x and y to make it spin around the mace knight
	 */
	private void spinBall(int delta, int speed) {
		
		// gets the circumference of the ball's orbit
		int c = (int) (2 * Math.PI * this.ballRadius);
		
		// gets the percentage of the circumference the ball moves this frame
		double percent = (speed * delta / 1000f) / (double)c;
		
		// since we know it moves X% of the circumference this frame, we
		// now also know the angle must add X% of 2PI this frame
		this.ballAngle += (2 * Math.PI) * percent;
		
		this.ballAngle = this.ballAngle % (2 * Math.PI);
		
		// finds the new x and y
		this.ballX = (float) (this.ballRadius * Math.cos(this.ballAngle) - (this.ballS / 2));
		this.ballY = (float) (this.ballRadius * Math.sin(this.ballAngle) - (this.ballS / 2));
	}

	/*
	 * Checks if the knight's ball hits the player
	 */
	private void checkForBallCollision() {
		
		// gets the player
		Player p = this.game.getPlayer();
		
		// gets the player's hitboxes
		ArrayList<Hitbox> pH = game.getPlayer().getHitboxes();
	
		// creates a new hitbox based on the ball's position
		Hitbox h = new Hitbox(this.handX + (int)this.ballX, this.handY + (int)this.ballY, this.ballS, this.ballS, this);
		
		for (int i = 0; i < pH.size(); i++) {
		
			if (pH.get(i).collidesWith(h)) {
			
				p.onHit();
				break;
				
			}
			
		}
		
	}
}
