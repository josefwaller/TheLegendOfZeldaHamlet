package entities;

import entities.abstracts.CollisionEntity;
import game.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import sprites.SpriteStore;

public class Button extends CollisionEntity {
	
	Image sprite;
	
	public Button(int x, int y, Game g) {
		super(x, y - 16, 16, g);
		
		this.sprite = SpriteStore.get().loadSprite("assets/images/objects/button.png");
	}
	
	protected void onPlayerCollide() {
	}
	
	public void render(Graphics g) {
	
		this.sprite.draw((int) this.x, (int) this.y, (int)this.w, (int)this.h);
	}
}
