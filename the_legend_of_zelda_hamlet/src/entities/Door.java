package entities;

import org.newdawn.slick.Graphics;

import sprites.SpriteStore;
import game.Game;

public class Door extends StaticEntity {
	
	public Door(int x, int y, boolean isHorizontal, Game g)
	{
		// sets position
		super(x, y, 16, g);
		
		// changes width or height, depending on orientation
		if (isHorizontal) {
			this.w = 32;
		} else {
			this.h = 32;
		}
		
		// sets image
		this.sprite = SpriteStore.get().loadSprite("assets/images/objects/door.png");
	}
	
	public void update()
	{
		
	}
}
