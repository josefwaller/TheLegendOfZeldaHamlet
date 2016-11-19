package entities;

/*
An abstract entity class
All entities inheirit from this 
class
*/
public abstract class Entity
{

	// the position
	protected int x;
	protected int y;
	protected int w;
	protected int h;
	
	
	/*
	 * Constructor
	 * Sets position
	 */
	public Entity (int x, int y, int w, int h)
	{
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	/*
	 * Another Constructor
	 * Sets position if width and height are the same
	 */
	public Entity (int x, int y, int s)
	{
	
		this.x = x;
		this.y = y;
		this.w = s;
		this.h = s;
		
	}
}