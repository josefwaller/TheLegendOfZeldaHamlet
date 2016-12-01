package entities;

import org.newdawn.slick.Graphics;

import sprites.Animation;
import sprites.AnimationStore;
import sprites.SpriteStore;
import entities.abstracts.EnemyEntity;
import game.Game;

/*
 * A soldier enemy who chases the player
 * 
 * May be one of three different difficulties:
 * Easy - Green
 * Medium - Blue
 * Hard - Red
 */
public class Soldier extends EnemyEntity {

	/*
	 * Initializes solder enemy and loads sprites
	 */
	public Soldier(int x, int y, Game g) {
		super(x, y, 16, 20, g);

		// the path to the image
		// will vary with different colors of soldier
		String imagePath = "assets/images/enemies/bluesoldier";
		
		// the string to the .sprites file
		// should remain constant unless the file is moved
		String spritesPath = "assets/images/enemies/soldier";
		// loads its sprites
		SpriteStore.get().loadSpriteSheet(
			imagePath,
			spritesPath);
		
		// loads its animations
		AnimationStore.get().loadAnimations(
				imagePath,
				spritesPath);
		
		Animation a = AnimationStore.get().getAnimation(
			imagePath, 
			"runup");
		
		this.setAnim(a, 100);
	}
	
	/*
	 * @see entities.abstracts.EnemyEntity#update(int)
	 */
	public void update(int delta) {
		
	}
	
}
