package entities;

import entities.abstracts.ThrowableEntity;
import game.Game;

/*
 * A pot the player can pick up and throw
 */
public class Pot extends ThrowableEntity {

	/*
	 * Constructor
	 */
	public Pot(int x, int y, Game g) {
		super(x, y, 16, g);
	}

	/*
	 * Updates the pot's position if it needs to be moved over to match the player's
	 */
	public void update() {

	}

}
