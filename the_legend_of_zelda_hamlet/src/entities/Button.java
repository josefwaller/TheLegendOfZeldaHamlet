package entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Button extends Entity {
	
	public Button(int x, int y) {
		super(x, y, 16);
	}
	
	public void update(int delta) {
		
	}
	
	public void render(Graphics g) {
	
		System.out.println(this.y);
		g.setColor(Color.red);
		g.fillRect(this.x, this.y, this.w, this.h);
	}
}
