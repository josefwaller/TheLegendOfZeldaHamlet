package entities;

import org.newdawn.slick.Graphics;

import entities.abstracts.MovingEntity;
import game.Game;

public class Soldier extends MovingEntity {

	public Soldier(int x, int y, Game g) {
		super(x, y, 16, 20, g);
	}
	
	public void update(int delta) {
		
	}
	
	public void render(Graphics g) {
		g.drawRect(this.x, this.y, this.w, this.h);
	}
	
}
