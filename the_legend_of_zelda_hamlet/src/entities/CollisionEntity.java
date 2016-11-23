package entities;

import game.Game;

/*
 * Entities that have special actions when hit by the player
 * and do not move.
 * Doors, buttons, etc
 */
public abstract class CollisionEntity extends StaticEntity {

	// just initializes the same as StaticEntity
	public CollisionEntity(int x, int y, int s, Game g) {
		super(x, y, s, g);
	}
	public CollisionEntity(int x, int y, int w, int h, Game g) {
		super(x, y, w, h, g);
	}

	/*
	 * Checks if the player is on the entity. If so, calls the onPlayerCollide method
	 */
	public void update() {
		
		Player p = this.game.getPlayer();
		
		if (this.collidesWithEntity(p)) {
			this.onPlayerCollide();
		}
		
	}
	
	/*
	 * The method to invoke when the player hits the entity
	 */
	protected abstract void onPlayerCollide();

	
}
