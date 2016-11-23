package entities;

import entities.abstracts.InteractiveEntity;
import game.Game;

public class NPC extends InteractiveEntity {

	public NPC (int x, int y, String spriteSheet, String dialog, Game g) {
		
		// sets position
		super(x, y, 16, g);
		
		// loads sprite
		this.loadImageWithDimensions(String.format("assets/images/%s.png", spriteSheet));
	}
	
	public void update() {
		
	}
	
	public void onPlayerInteract() {
		
	}
	
}
