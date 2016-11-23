package entities;

import game.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class NPC extends StaticEntity {

	public NPC (int x, int y, Game g) {
		super(x, y, 16, g);
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g) {
		
		g.setColor(Color.red);
		g.drawRect(this.x, this.y, this.w, this.h);
	}
	
}
