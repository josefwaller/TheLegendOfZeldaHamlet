package entities;

import entities.abstracts.InteractiveEntity;
import game.Game;

/*
 * An NPC the player can talk to
 */
public class NPC extends InteractiveEntity {
	
	// the dialog the NPC will say
	String dialog;

	/*
	 * Constructor
	 */
	public NPC (int x, int y, String spriteSheet, String dialog, Game g) {
		
		// sets position
		super(x, y, 16, g);
		
		// loads sprite
		this.loadImageWithDimensions(String.format("assets/images/npcs/%s.png", spriteSheet));
		
		// loads dialog
		this.dialog = Game.readFile(String.format("assets/dialog/%s.txt", dialog));

	}
	
	/*
	 * (non-Javadoc)
	 * @see entities.abstracts.StaticEntity#update()
	 */
	public void update() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see entities.abstracts.InteractiveEntity#onPlayerInteract()
	 */
	public void onPlayerInteract() {
		
		// starts new dialog
		this.game.startDialog(this.dialog);
	}
	
}
