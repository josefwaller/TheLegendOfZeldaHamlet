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
		super(x + 2, y, 14, g);
		
		this.loadImageWithDimensions("assets/images/objects/pot.png");
		
		this.isSolid = true;
	}

}
