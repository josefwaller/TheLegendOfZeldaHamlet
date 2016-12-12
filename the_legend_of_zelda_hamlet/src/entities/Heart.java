package entities;

import entities.abstracts.CollisionEntity;
import game.Game;

/*
 * A heart the player can pick up
 * Replenishes one of their health
 */
public class Heart extends CollisionEntity {

	public Heart(int x, int y, Game g) {
		
		super(x, y, 10, g);
		this.loadImageWithDimensions("assets/images/heart.png");
		this.addHitbox(0, 0, this.w, this.h);
	}

	public void onPlayerCollide() {
		
		this.game.getPlayer().onHeart();
		this.game.removeConsumable(this);
	}
	
}
