package entities;

import game.Game;

import sprites.SpriteStore;

public class NPC extends StaticEntity {

	public NPC (int x, int y, String spriteSheet, String dialog, Game g) {
		
		// sets position
		super(x, y, 16, g);
		
		// loads sprite
		this.sprite = SpriteStore.get().loadSprite(String.format("assets/images/%s.png", spriteSheet));
	}
	
	public void update() {
		
	}
	
}
