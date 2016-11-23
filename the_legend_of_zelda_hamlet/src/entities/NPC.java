package entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
		
		System.out.println(this.dialog);
	}
	
	public void update() {
		
	}
	
	public void onPlayerInteract() {
		System.out.println("Interacting!");
	}
	
}
