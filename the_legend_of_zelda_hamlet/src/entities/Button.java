package entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import sprites.SpriteStore;

public class Button extends Entity {
	
	Image sprite;
	
	public Button(int x, int y) {
		super(x, y - 16, 16);
		
		this.sprite = SpriteStore.get().loadSprite("assets/images/objects/button.png");
	}
	
	public void update(int delta) {
		
	}
	
	public void render(Graphics g) {
	
		this.sprite.draw((int) this.x, (int) this.y, (int)this.w, (int)this.h);
	}
}
