package entities.abstracts;

/*
 * A hitbox in an entity
 * Used for collision detection
 */
public class Hitbox {

	// the coordinates of the hitbox relative to the window
	private int x;
	private int y;
	
	// the coordinates of the hitbox relative to the entity
	private int offX;
	private int offY;
	
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
		this.offX = x;
		this.offY = y;
		this.w = w;
		this.h = h;
		this.entity = e;
		
		// updates x and y
		update();
	}
	
	public void update() {
		this.x = (int) (this.offX + this.entity.getX());
		this.y = (int) (this.offY + this.entity.getY());
	}
	
	/*
	 * Checks if the hitbox collides with another hitbox
	 */
	public boolean collidesWith(Hitbox h) {
		
		// gets the values of the other hitbox
		int oX = (int) (h.getX());
		int oY = (int) (h.getY());
		int oW = h.getW();
		int oH = h.getH();
		
		if (this.x < oX + oW) {
			if (this.x + this.w > oX) {
				if (this.y < oY + oH) {
					if (this.y + this.h > oY) {
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
