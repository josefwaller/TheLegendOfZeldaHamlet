package entities;

/*
An abstract entity class
All entities inheirit from this 
class
*/
public abstract class Entity
{

	// the position
	protected float x;
	protected float y;
	protected int w;
	protected int h;
	
	// the direction the entity is facing
	protected int direction;
	
	// the possible directions
	static final int DIR_UP = 0;
	static final int DIR_DOWN = 1;
	static final int DIR_LEFT = 2;
	static final int DIR_RIGHT = 3;
	
	
	/*
	 * Constructor
	 * Sets position
	 */
	public Entity (int x, int y, int w, int h)
	{
		
		this.x = (float)x;
		this.y = (float)y;
		this.w = w;
		this.h = h;
		
		this.direction = Entity.DIR_DOWN;
	}
	
	/*
	 * Another Constructor
	 * Sets position if width and height are the same
	 */
	public Entity (int x, int y, int s)
	{
	
		this.x = (float)x;
		this.y = (float)y;
		this.w = s;
		this.h = s;
		
	}
}