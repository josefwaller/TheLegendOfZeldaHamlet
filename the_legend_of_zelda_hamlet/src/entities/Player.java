package entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;

/*
The main player class.
*/
public class Player extends Entity{

	/*
	Creates a new player
	*/
	public Player(int x, int y)
{
		
		super(x, y, 30, 50);
		
	}
	
	/*
	 * Updates the player position
	 * Moves, takes/deals damage, etc
	 */
	public void update(int delta)
	{
		
	}
	
	/*
	 * Draws the player on the screen
	 * 
	 */
	public void render(Graphics g)
	{
		
		g.setColor(Color.red);
		g.fillRect(this.x, this.y, this.w, this.h);
		
	}
}