package entities;

import org.newdawn.slick.Graphics;

import entities.abstracts.EnemyEntity;
import game.Game;

/*
 * A soldier enemy who chases the player
 * 
 * May be one of three different difficulties:
 * Easy - Green
 * Medium - Blue
 * Hard - Red
 */
public class Soldier extends EnemyEntity {

	public Soldier(int x, int y, Game g) {
		super(x, y, 16, 20, g);
		System.out.println("Hello world");
	}
	
	/*
	 * @see entities.abstracts.EnemyEntity#update(int)
	 */
	public void update(int delta) {
		
	}
	
	/*
	 * @see entities.abstracts.Entity#render(org.newdawn.slick.Graphics)
	 */
	public void render(Graphics g) {
		g.drawRect(this.x, this.y, this.w, this.h);
	}
	
}
