package entities;

import org.newdawn.slick.Graphics;

import sprites.SpriteStore;
import game.Game;

public class Door extends StaticEntity {
	
	// the path ID for the door
	// connects two doors with the same path ID
	private int pathId;
	
	// the number of degrees to rotate the image
	private int degrees;
	
	public Door(int x, int y, int pathId, int direction, Game g)
	{
		// sets position
		super(x, y, 16, g);
		
		// changes width or height, depending on orientation
		this.direction = direction;
		if (this.direction == Entity.DIR_DOWN || this.direction == Entity.DIR_UP) {
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
		
		switch(this.direction) {
			case Entity.DIR_LEFT: this.degrees = 0; break;
			case Entity.DIR_UP: this.degrees = 90; break;
			case Entity.DIR_RIGHT: this.degrees = 180; break;
			case Entity.DIR_DOWN: this.degrees = 270; break;
		}
	}
	
	public void update()
	{
		Player p = this.game.getPlayer();
		
		if (this.collidesWithEntity(p)) {
			
			// transition to next room
			this.game.startTransition(this.pathId, this);
		}
	}
	
	public void render(Graphics g) {
		
		// rotates the sprite the correct number of degrees
		this.sprite.setCenterOfRotation(this.w / 2, this.h / 2);
		this.sprite.rotate(this.degrees);
		
		// draws the sprite
		this.sprite.draw((int) this.x, (int) this.y, this.w, this.h);
		
		// rotates the sprite back
		this.sprite.rotate(- this.degrees);
	}
	
	/*
	 * Returns a 2D array of where the player should appear after coming out the door
	 */
	public int[] getExitPos(int playerW, int playerH) {
		
		// the position to return
		int[] toReturn = new int[2];
		
		// sets the return position by default to the door's position
		toReturn[0] = (int) this.x + (this.w - playerW) / 2;
		toReturn[1] = (int) this.y + (this.h - playerH) / 2;
		
		switch (this.direction) {
			case Entity.DIR_DOWN: toReturn[1] = (int) (this.y + this.h); break;
			case Entity.DIR_UP: toReturn[1] = (int) (this.y - this.h); break;
			case Entity.DIR_LEFT: toReturn[0] = (int) (this.x - this.w); break;
			case Entity.DIR_RIGHT: toReturn[0] = (int) (this.x + this.w); break;
		}
		
		return toReturn;
	}
	
	/*
	 * Get/Set variables
	 */
	public int getPathID() {
		return this.pathId;
	}
}
