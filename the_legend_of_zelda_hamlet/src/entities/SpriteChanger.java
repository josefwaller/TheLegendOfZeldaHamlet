package entities;


import org.newdawn.slick.Image;

import sprites.SpriteStore;
import entities.abstracts.InteractiveEntity;
import game.Game;


/*
 * An entity that changes sprites when the player interacts with it
 */
public class SpriteChanger extends InteractiveEntity {
	
	// the sprite to change to
	private Image spriteTwo;
	
	public SpriteChanger(int x, int y, String spriteOne, String spriteTwo, Game g) {
		
		super(x, y, 0, g);
		
		// loads first image
		this.loadImageWithDimensions("assets/images/" + spriteOne + ".png");
		
		// loads second image
		this.spriteTwo = SpriteStore.get().loadSprite("assets/images/" + spriteTwo + ".png");
	}

	public void update() {
		
	}
	
	/*
	 * Changes the sprite to the second sprite
	 */
	public void onPlayerInteract() {

		this.sprite = this.spriteTwo;
		
	}

}
