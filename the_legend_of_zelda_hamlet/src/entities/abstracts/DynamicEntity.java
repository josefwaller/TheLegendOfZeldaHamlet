package entities.abstracts;

import game.Game;

public abstract class DynamicEntity extends Entity {

	public DynamicEntity(int x, int y, int w, int h, Game g) {
		super(x, y, w, h, g);
	}
	public DynamicEntity(int x, int y, int s, Game g) {
		super(x, y, s, g);
	}

	/*
	 * Checks if the entity collides with a certain point
	 */
	public boolean collidesWithPoint(int x, int y) {
		
		if (this.x <= x) {
			if (this.x + this.w >= x) {
				if (this.y <= y) {
					if (this.y + this.h >= y) {
						return true;
					}
				}
			}
		}
		
		return false;
		
	}
	/*
	 * Returns the x and y point that is directly in front of the player
	 * Used for interacting, attacking, etc
	 */
	protected  int[] getCoordsInFront(int range) {
		
		// starts with the center position
		int x = (int) this.x + this.w / 2;
		int y = (int) this.y + this.h / 2;
		
		// changes depending on direction
		switch (this.direction) {
		
			case Entity.DIR_UP:
				y = (int) (this.y - range);
				break;
				
			case Entity.DIR_DOWN:
				y = (int) (this.y + this.h + range);
				break;
				
			case Entity.DIR_LEFT:
				x = (int) (this.x - range);
				break;
				
			case Entity.DIR_RIGHT:
				x = (int) (this.x + this.h + range);
				break;
		}
		
		int[] toReturn = {x, y};
		
		return toReturn;
	}
	
	/*
	 * Checks if the new position is valid.
	 * If so, moves the entity.
	 * If not, doesn't move the entity
	 */
	protected void tryToMove(float newX, float newY) {
		
		if (!this.game.isBlocked(newX, newY, this.w, this.h)) {
			
			boolean blocked = false;
			
			StaticEntity[] objs = this.game.getObjects();
			
			for (int i = 0; i < objs.length; i++) {
				
				StaticEntity obj = objs[i];
				
				if (obj.getIsSolid()) {
					
					// records the old position in case it hits the entity
					float oldX = this.x;
					float oldY = this.y;
					
					// sets position
					this.x = newX;
					this.y = newY;
					
					// updates hitboxes
					this.updateHitboxes();
					
					// checks if it collides
					if (this.collidesWithEntity(obj)) {
						
						// reverts back to old position
						blocked = true;
						this.x = oldX;
						this.y = oldY;
						
					}
				}
			}
			
			if (!blocked) {

				this.x = newX;
				this.y = newY;
			}
		}
		
	}

}
