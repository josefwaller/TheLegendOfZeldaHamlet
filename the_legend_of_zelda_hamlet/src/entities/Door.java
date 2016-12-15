package entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import entities.abstracts.CollisionEntity;
import entities.abstracts.Entity;
import game.Game;

/*
 * A door the player can walk through
 * It will either take him to a new area 
 * Or a different room in the .tmx file
 */
public class Door extends CollisionEntity {
	
	// the path ID for the door
	// connects two doors with the same path ID
	private int pathId;
	
	// whether the door leads to a new map
	private boolean isNewMap = false;
	private String newMapName;
	
	public Door(int x, int y, int w, int h, int pathId, int direction, boolean isNewMap, String mapName, Game g)
	{
		// sets position
		super(x, y, w, h, g);
		
		// records whether it leads to a new map
		if (isNewMap) {
			this.isNewMap = true;
			this.newMapName = mapName;
		}
		this.direction = direction;
		
		// adds hitbox
		this.addHitbox();
		
		// sets path Id
		// determines which door the player will come out of after going through this door
		// ex: two doors with pathId = 1 will connect to each other
		this.pathId = pathId;
	}
	
	public void render (Graphics g) {
		
		if (this.game.isDebug()) {

			g.setColor(Color.green);
			g.drawRect(this.x, this.y, this.w, this.h);
		}
		
	}
	
	public void onPlayerCollide() {
		
		if (!this.isNewMap) {
			this.game.startTransition(this.pathId, this);
			
		} else {
			
			this.game.moveToMap(this.newMapName, this.pathId);
		}
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
			case Entity.DIR_UP: toReturn[1] = (int) (this.y - playerH); break;
			case Entity.DIR_LEFT: toReturn[0] = (int) (this.x - playerW); break;
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
