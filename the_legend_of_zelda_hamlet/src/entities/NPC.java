package entities;

import entities.abstracts.InteractiveEntity;
import game.Game;

public class NPC extends InteractiveEntity {
	
	String dialog;

	public NPC (int x, int y, String spriteSheet, String dialog, Game g) {
		
		// sets position
		super(x, y, 16, g);
		
		// loads sprite
		this.loadImageWithDimensions(String.format("assets/images/%s.png", spriteSheet));
		
		// loads dialog
		this.dialog = Game.readFile(dialog);

	}
	
	public void update() {
		
	}
	
	public void onPlayerInteract() {
		this.game.startDialog(this.dialog);
	}
	
}
