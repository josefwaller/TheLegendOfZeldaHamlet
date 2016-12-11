package entities;

import sprites.Animation;
import sprites.AnimationStore;
import entities.abstracts.EnemyEntity;
import game.Game;

public class MaceSoldier extends EnemyEntity {

	Animation walkUp;
	Animation walkSide;
	Animation walkDown;
	
	public MaceSoldier (int x, int y, Game g) {
		super(x, y, 16, 24, g);
		
		AnimationStore a = AnimationStore.get();
		
		this.walkUp = a.getAnimation("assets/images/enemies/maceknight", "walkup");
		
		this.setAnim(walkUp, 500);
		
		this.health = 10;
	}
	
	public void update(int delta) {
		
		this.animUpdate();
	}
}
