package entities.abstracts;

/*
 * A hitbox in an entity
 * Used for collision detection
 */
public class Hitbox {

	// the coordinates of the hitbox relative to the entity
	private int x;
	private int y;
	
	// the width and height of the hitbox
	private int w;
	private int h;
	
	// the entity the hitbox belongs to
	private Entity entity;
	
	/*
	 * Creates a new hitbox
	 */
	public Hitbox(int x, int y, int w, int h, Entity e) {
		
		// sets properties
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.entity = e;
	}
	
	/*
	 * Checks if the hitbox collides with another hitbox
	 */
	public boolean collidesWith(Hitbox h) {
		
		// gets new values based on the entity's position
		int x = (int) (this.x + this.entity.getX());
		int y = (int) (this.y + this.entity.getY());
		
		// gets the values of the other hitbox
		Entity oE = h.getEntity();
		int oX = (int) (h.getX() + oE.getX());
		int oY = (int) (h.getY() + oE.getY());
		int oW = h.getW();
		int oH = h.getH();
		
		if (x < oX + oW) {
			if (x + this.w > oX) {
				if (y < oY + oH) {
					if (y + this.h > oY) {
						return true;
					}
				}
			}
		}
		
		return false;		
	}
	
	/*
	 * Get/Set methods
	 */
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int getW() {
		return this.w;	
	}
	public int getH() {
		return this.h;
	}
	public Entity getEntity() {
		return this.entity;
	}
	
}
