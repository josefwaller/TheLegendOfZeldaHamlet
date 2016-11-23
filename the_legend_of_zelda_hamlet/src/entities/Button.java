package entities;

import game.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import sprites.SpriteStore;

public class Button extends InteractiveEntity {
	
	Image sprite;
	
	public Button(int x, int y, Game g) {
		super(x, y - 16, 16, g);
		
		this.sprite = SpriteStore.get().loadSprite("assets/images/objects/button.png");
	}
	
	public void update() {
		
	}
	
	protected void onPlayerCollide() {
		
	}
	
	public void onPlayerInteract() {
		
	}
	
	public void render(Graphics g) {
	
		this.sprite.draw((int) this.x, (int) this.y, (int)this.w, (int)this.h);
	}
}
