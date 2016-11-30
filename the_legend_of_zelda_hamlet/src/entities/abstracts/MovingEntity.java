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
			
			// changes the x and y to the new x and y
			this.x += xMod * newX;
			this.y += yMod * newY;
		}
		
	}
	
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
	
}
