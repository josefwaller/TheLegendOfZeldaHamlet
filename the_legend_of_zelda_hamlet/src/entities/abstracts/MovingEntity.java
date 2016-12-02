package entities.abstracts;

import game.Game;

/*
 * Used for entities that chase something or move towards a point
 */
public abstract class MovingEntity extends AnimatedEntity {

	/*
	 * Constructors
	 * Simply call the Entity constructors
	 */
	public MovingEntity(int x, int y, int w, int h, Game g) {
		super(x, y, w, h, g);
	}
	public MovingEntity(int x, int y, int s, Game g) {
		super(x, y, s, g);
	}
	
	/*
	 * Move to point
	 * 
	 * Moves towards a point with the distance given
	 */
	protected void moveToPoint(int x, int y, float distance) {
			
		// gets the difference in x and y between the entity and the point
		int diffX = (int)Math.abs(this.x - x);
		int diffY = (int)Math.abs(this.y - y);
		
		// gets the distance between the point and the entity
		double disToPoint = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
		
		// checks that the double did not round and make disToPoint 0
		if (disToPoint == 0) {
			
			// moves the entity onto the point
			this.x = x;
			this.y = y;
			
		} else {
			
			// gets the angle the entity needs to move at
			double theta = Math.asin(diffY / disToPoint);
			
			// finds the new x the entity should move
			// since it needs to move the distance closer
			float newX = (float)(distance * Math.cos(theta));
			float newY = (float)(distance * Math.sin(theta));
			
			// figures out if the entity needs to add or subtract from its x and y
			int xMod = 1;
			int yMod = 1;
			
			if (this.x > x) {
				xMod = -1;
			}
			if (this.y > y) {
				yMod = -1;
			}
			
			// changes the entity's direction depending on where he is going
			if (Math.abs(newX) > Math.abs(newY)) {
				if (xMod > 0) {
					this.direction = Entity.DIR_RIGHT;
					
				} else {
					this.direction = Entity.DIR_LEFT;
				}
			} else {
				if (yMod > 0) {
					this.direction = Entity.DIR_DOWN;
							
				} else {
					this.direction = Entity.DIR_UP;
				}
			}
			
			// changes the x and y to the new x and y
			this.x += xMod * newX;
			this.y += yMod * newY;
		}
		
	}
	
	/*
	 * Checks if the soldier collides with a certain point
	 */
	protected boolean collidesWithPoint(int x, int y) {
		
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
	 * Checks if the center of the entity is within a certain 
	 * distance of the point
	 */
	protected boolean isAtPoint(int x, int y, float dis) {
		
		if (this.x + dis > x) {
			if (this.x < x + dis) {
				if (this.y + dis > y) {
					if (this.y < y + dis) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
