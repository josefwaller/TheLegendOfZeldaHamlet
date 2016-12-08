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
