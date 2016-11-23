package entities;

import game.Game;

/*
 * An entity that does not move and interacts with the player when they either move 
 * onto it or interact with it using space
 * Ex: Doors, buttons, NPCs, etc
 */
public abstract class InteractiveEntity extends StaticEntity {

	/*
	 * Constructors
	 * Basically the same as the StaticEntity constructors
	 */
	public InteractiveEntity(int x, int y, int s, Game g) {
		super(x, y, s, g);
	}

	public InteractiveEntity(int x, int y, int w, int h, Game g) {
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
	
	/*
	 * The method to invoke when the player interacts with the entity
	 * This will be called by the player, thus it is public
	 */
	public abstract void onPlayerInteract();
	
}
