package entities.abstracts;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import util.Hitbox;
import game.Game;
/*
An abstract entity class
All entities inheirit from this 
class
*/
public abstract class Entity
{
	
	// The game in which the Entity exists
	protected Game game;

	// the position
	protected float x;
	protected float y;
	protected int w;
	protected int h;
	
	// the direction the entity is facing
	protected int direction;
	
	// the possible directions
	static public final int DIR_UP = 0;
	static public final int DIR_DOWN = 1;
	static public final int DIR_LEFT = 2;
	static public final int DIR_RIGHT = 3;
	
	// the entity's hitboxes
	private ArrayList<Hitbox> hitboxes;
	
	/*
	 * Constructor
	 * Sets position
	 */
	public Entity (int x, int y, int w, int h, Game g)
	{
		
		this.x = (float)x;
		this.y = (float)y;
		this.w = w;
		this.h = h;
		this.game = g;
		
		this.direction = Entity.DIR_DOWN;
		
		this.hitboxes = new ArrayList<Hitbox>();
	}
	
	/*
	 * Another Constructor
	 * Sets position if width and height are the same
	 */
	public Entity (int x, int y, int s, Game g)
	{
	
		this.x = (float)x;
		this.y = (float)y;
		this.w = s;
		this.h = s;
		this.game = g;
		
		this.direction = Entity.DIR_DOWN;
		
		this.hitboxes = new ArrayList<Hitbox>();
	}
	
	// Entities must be able to render
	public abstract void render(Graphics g);
	
	/*
	 * Checks if this entity collides with another entity
	 */
	protected boolean collidesWithEntity(Entity e) {
		
		float otherX = e.getX();
		float otherY = e.getY();
		int otherW = e.getW();
		int otherH = e.getH();
		
		if (this.x < otherX + otherW) {
			if (this.x + this.w > otherX) {
				if (this.y < otherY + otherH) {
					if (this.y + this.h > otherY) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/*
	 * Updates the entity's hitboxes' positions
	 */
	protected void updateHitboxes() {
		for (int i = 0; i < this.hitboxes.size(); i++) {
			this.hitboxes.get(i).update();
		}
	}
	
	/*
	 * Draws the entity's hitboxes
	 */
	protected void drawHitboxes(Graphics g) {
		
		for (int i = 0; i < this.hitboxes.size(); i++) {
			
			Hitbox h = this.hitboxes.get(i);
			
			g.drawRect(
				h.getX(),
				h.getY(),
				h.getW(),
				h.getH()
			);
		}
	}
	
	/*
	 * Adds a hitbox to the entity
	 */
	protected void addHitbox(int x, int y, int w, int h) {
		this.hitboxes.add(new Hitbox(x, y, w, h, this));
	}
	
	/*
	 * Get and set methods for Entity
	 */
	public float getX() {
		return this.x;
	}
	public float getY() {
		return this.y;
	}
	public int getW() {
		return this.w;
	}
	public int getH() {
		return this.h;
	}
	public int getDirection() {
		return this.direction;
	}
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
}