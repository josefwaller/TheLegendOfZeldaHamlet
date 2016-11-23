package entities.abstracts;

import game.Game;

/*
 * An entity that does not move and performs a
 * special action when the player interacts 
 * with it.
 * Ex: NPCs, signs, chests
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
	 * The method to invoke when the player interacts with the entity
	 * This will be called by the player, thus it is public
	 */
	public abstract void onPlayerInteract();
	
}
