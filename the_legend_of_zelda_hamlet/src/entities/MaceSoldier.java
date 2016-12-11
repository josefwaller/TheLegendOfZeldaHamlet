package entities;

import sprites.Animation;
import sprites.AnimationStore;
import entities.abstracts.EnemyEntity;
import entities.abstracts.Entity;
import game.Game;

/*
 * A soldier who wields a mace
 * Used as a final boss
 */
public class MaceSoldier extends EnemyEntity {

	// the initial spawning point of the MaceSoldier
	// he should always hang around here
	int targetX;
	int targetY;
	
	// how far away he can be from the target points
	// without moving back to them
	int maxDistance = 5;
	
	// animations
	Animation walkUp;
	Animation walkSide;
	Animation walkDown;
	
	// the time between switching frames when walking
	int walkDuration = 300;
	
	public MaceSoldier (int x, int y, Game g) {
		super(x, y, 16, 24, g);
		
		this.targetX = x;
		this.targetY = y;
		
		// loads sprites
		AnimationStore a = AnimationStore.get();
		
		String sprites = "assets/images/enemies/maceknight";

		this.walkUp = a.getAnimation(sprites, "walkup");
		this.walkSide = a.getAnimation(sprites, "walkside");
		this.walkDown = a.getAnimation(sprites, "walkdown");
		
		this.setAnim(walkUp, this.walkDuration);
		
		// adds a hitbox
		this.addHitbox(1,  0,  13,  22);
		
		// sets health
		this.health = 10;
	}
	
	public void update(int delta) {
		
		switch (this.state) {
		
			case EnemyEntity.STATE_FLINCHING:
				this.flinch(delta);
				break;
				
			case EnemyEntity.STATE_DYING:
				this.die();
				break;
				
			case EnemyEntity.STATE_IDLE:

				
				Player p = this.game.getPlayer();
				
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
					this.setAnim(this.walkSide, this.walkDuration);
					
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
				
				break;
		}
		
		this.animUpdate();
	}
}
