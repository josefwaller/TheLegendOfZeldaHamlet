package entities;

import org.newdawn.slick.Graphics;

import sprites.SpriteStore;
import game.Game;

public class Door extends StaticEntity {
	
	private int pathId;
	
	public Door(int x, int y, int pathId, boolean isHorizontal, Game g)
	{
		// sets position
		super(x, y, 16, g);
		
		// changes width or height, depending on orientation
		if (isHorizontal) {
			this.w = 32;
		} else {
			this.h = 32;
		}
		
		// sets path Id
		// determines which door the player will come out of after going through this door
		// ex: two doors with pathId = 1 will connect to each other
		this.pathId = pathId;
		
		// sets image
		this.sprite = SpriteStore.get().loadSprite("assets/images/objects/door.png");
	}
	
	public void update()
	{
		Player p = this.game.getPlayer();
		
		if (this.collidesWithEntity(p)) {
			
			// transition to next room
		}
	}
}
