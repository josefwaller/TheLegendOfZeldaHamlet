package hud;

import java.awt.Font;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class HeadsUpDisplay {

	// the width and height of the window
	private int w;
	private int h;
	
	// the dialog manager
	private DialogManager dialog;
	
	private boolean showingDialog;
	
	/*
	 * Constructor
	 * 
	 * Sets up Heads up display
	 */
	public HeadsUpDisplay(int w, int h) {
		
		// sets width and height
		this.w = w;
		this.h = h;
		
		// initializes components of the hud
		this.dialog = new DialogManager(this.w, this.h);
		
	}
	
	/*
	 * Renders all the different components on the screen
	 */
	public void render(Graphics g) {

		// resets scale to stop the font being blurry
		g.resetTransform();

		// draws dialog if there is any
		if (this.showingDialog) {
			
			this.dialog.render(g);
		}
	}
	
	/*
	 * Loads a .ttf file into a UnicodeFont Object with
	 */
	public static UnicodeFont loadFont(String url) {
		
		// loads the font
		Font f = new Font("Arial", Font.PLAIN, 30);
		
		// the font to return
		UnicodeFont toReturn = new UnicodeFont(f);
		
		// initializes the font
		try {
			toReturn.getEffects().add(new ColorEffect(java.awt.Color.white));
		    toReturn.addAsciiGlyphs();
			toReturn.loadGlyphs();
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// returns the font
		return toReturn;
	}
	
	/*
	 * Sets up to show dialog with the 
	 */
	public void startDialog(String dialog) {
		
		// sets show dialog
		this.showingDialog = true;
		
		// processes dialog
		this.dialog.startDialog(dialog);
	}
	
}
