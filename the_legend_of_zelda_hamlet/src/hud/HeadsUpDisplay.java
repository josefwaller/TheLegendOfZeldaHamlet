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
	
	private boolean showingGameOver = false;

	private UnicodeFont gameOverFontBig;
	private UnicodeFont gameOverFontSmall;
	
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

		this.gameOverFontBig = HeadsUpDisplay.loadFont("RetGanon.ttf", 70);
		this.gameOverFontSmall = HeadsUpDisplay.loadFont("RetGanon.ttf", 45);
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
		} else if (this.showingGameOver) {
			
			String gameOver = "Game Over";
			String message = "Press SPACE to continue.";
			
			this.drawBorderedText(
				this.gameOverFontBig, 
				Color.white, 
				Color.blue, 
				(this.w - this.gameOverFontBig.getWidth(gameOver)) / 2,
				this.h * 1/4, 
				2, 
				gameOver);
			
			this.drawBorderedText(
				this.gameOverFontSmall, 
				Color.white, 
				Color.blue, 
				(this.w - this.gameOverFontSmall.getWidth(message)) / 2,
				this.h * 1/2, 
				2, 
				message);
		}
	}
	
	/*
	 * Sets up to show the game over screen
	 */
	public void showGameOver() {
		this.showingGameOver = true;
	}
	
	/*
	 * Hides the game over screen
	 */
	public void hideGameOver() {
		this.showingGameOver = false;
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
	
	/*
	 * Loads a .ttf file into a UnicodeFont Object with
	 */
	@SuppressWarnings("unchecked")
	public static UnicodeFont loadFont(String url, int size) {
		
		// loads the font
		Font f;
		try {
			// creates a new input stream to load the font
			InputStream is = ResourceLoader.getResourceAsStream(
					String.format("assets/fonts/%s", url));
			
			// creates a new font object
			f = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is);
			
			// sets basic style
			f = f.deriveFont(java.awt.Font.PLAIN, size);
	        
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
	 * Loads a font with the default size of 50
	 */
	public static UnicodeFont loadFont(String url) {
		return HeadsUpDisplay.loadFont(url, 50);
	}

	
}
