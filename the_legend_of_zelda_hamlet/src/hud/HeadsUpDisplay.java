package hud;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.util.ResourceLoader;

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
		this.dialog = new DialogManager(this.w, this.h, this);
		
	}
	
	/*
	 * Checks for input that effects the HUD
	 */
	public void update(Input input ) {
		
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			
			this.dialog.updateText();
			
		}
		
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
	 * Draws text on the screen with a border
	 */
	public void drawBorderedText(UnicodeFont f, Color fill, Color border, int x, int y, int w, String text) {
		
		// draws 8 copies of the text around the initial position
		for (int xOff = -1; xOff < 2; xOff++) {
			for (int yOff = -1; yOff < 2; yOff++) {
				
				if (!(xOff == 0 && yOff == 0)) {
					
					// draws the text
					f.drawString(x + w * xOff, y + w * yOff, text, border);
					
				}
				
			}
		}
		
		// draws the center copy
		f.drawString(x, y, text, fill);
		
	}
	
	/*
	 * Loads a .ttf file into a UnicodeFont Object with
	 */
	public static UnicodeFont loadFont(String url) {
		
		// loads the font
		Font f;
		try {
			// creates a new input stream to load the font
			InputStream is = ResourceLoader.getResourceAsStream(
					String.format("assets/fonts/%s", url));
			
			// creates a new font object
			f = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is);
			
			// sets basic style
			f = f.deriveFont(java.awt.Font.PLAIN, 50);
	        
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
		// the font to return
		UnicodeFont toReturn = new UnicodeFont(f);
		
		// initializes the font
		try {
			toReturn.getEffects().add(new ColorEffect(java.awt.Color.white));
		    toReturn.addAsciiGlyphs();
			toReturn.loadGlyphs();
			
		} catch (SlickException e) {
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
	
	/*
	 * Stops rendering the dialog box and text
	 */
	public void stopDialog() {
		this.showingDialog = false;
	}
	
	/*
	 * Get/Set variables
	 */
	public DialogManager getDialog() {
		return this.dialog;
	}
	
}
