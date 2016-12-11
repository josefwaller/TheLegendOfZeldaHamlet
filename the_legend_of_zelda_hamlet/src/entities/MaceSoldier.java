package entities;

import sprites.Animation;
import sprites.AnimationStore;
import entities.abstracts.EnemyEntity;
import entities.abstracts.Entity;
import game.Game;

public class MaceSoldier extends EnemyEntity {

	Animation walkUp;
	Animation walkSide;
	Animation walkDown;
	
	public MaceSoldier (int x, int y, Game g) {
		super(x, y, 16, 24, g);
		
		AnimationStore a = AnimationStore.get();
		
		String sprites = "assets/images/enemies/maceknight";

		this.walkUp = a.getAnimation(sprites, "walkup");
		this.walkSide = a.getAnimation(sprites, "walkside");
		this.walkDown = a.getAnimation(sprites, "walkDown");
		
		this.setAnim(walkUp, 500);
		
		this.addHitbox(1,  0,  13,  22);
		
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
				
				if (p.getX() < this.x) {
					this.direction = Entity.DIR_LEFT;
					
				} else {
					this.direction = Entity.DIR_RIGHT;
					
				}
				
				this.setAnim(this.walkSide, 300);
				
				
				if (this.collidesWithEntity(p)) {
					p.onHit();
				}
				break;
		}
		
		this.animUpdate();
	}
}
