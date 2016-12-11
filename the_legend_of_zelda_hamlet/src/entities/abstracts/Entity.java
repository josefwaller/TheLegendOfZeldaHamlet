package entities.abstracts;

import java.util.ArrayList;

import org.newdawn.slick.Color;
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
	
	// the possible directions
	static public final int DIR_UP = 0;
	static public final int DIR_DOWN = 1;
	static public final int DIR_LEFT = 2;
	static public final int DIR_RIGHT = 3;
	
	// The game in which the Entity exists
	protected Game game;

	// the position
	protected float x;
	protected float y;
	protected int w;
	protected int h;
	
	// the direction the entity is facing
	protected int direction;
	
	// the index of the section the entity is in
	// -1 means the section is not set
	protected int section = -1;
	
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
		
		for (int i = 0; i < this.hitboxes.size(); i++) {
			
			// gets this hitbox
			Hitbox tH = this.hitboxes.get(i);
			
			for (int x = 0; x < e.getHitboxes().size(); x++) {
				
				// gets the other hitbox
				Hitbox oH = e.getHitboxes().get(x);
				
				if (tH.collidesWith(oH)) {
					return true;
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
	 * Returns the section the eneity is in
	 * If the section is null, it first finds
	 * its section
	 */
	public int getSection() {
		if (this.section == -1) {
			
			this.section = this.game.getSectionForPoint((int)this.x, (int)this.y, this.w, this.h);
			
		}
		
		return this.section;
	}
	
	/*
	 * Draws the entity's hitboxes
	 */
	protected void drawHitboxes(Graphics g) {
		
		g.setColor(Color.pink);
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
	public ArrayList<Hitbox> getHitboxes() {
		return this.hitboxes;
	}
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void setDirection(int d) {
		this.direction = d;
	}
}