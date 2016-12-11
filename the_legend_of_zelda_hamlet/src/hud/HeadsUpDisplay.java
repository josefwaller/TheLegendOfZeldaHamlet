package hud;

import game.Game;

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

	// the game the HUD belongs to
	private Game game;
	
	// the width and height of the window
	private int w;
	private int h;
	
	// the dialog manager
	private DialogManager dialog;
	
	private boolean showingDialog;
	
	private boolean showingGameOver = false;
	
	// whether it is fading out and/or fading in
	private boolean fadingOut;
	private boolean fadingIn;
	
	// the time it began fading in/out
	private long fadeTime;
	
	// the time it takes to fade in/out
	private int fadeDuration = 1000;
	
	// the current alpha of the black overlay
	private double fadeAlpha = 0.0;

	private UnicodeFont gameOverFontBig;
	private UnicodeFont gameOverFontSmall;
	
	/*
	 * Constructor
	 * 
	 * Sets up Heads up display
	 */
	public HeadsUpDisplay(int w, int h, Game g) {
		
		// sets width and height
		this.w = w;
		this.h = h;
		
		this.game = g;
		
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
		
		if (this.fadingIn || this.fadingOut) {
			
			int start = 0;
			int end = 0;
			
			if (this.fadingIn) {
				start = 1;
			} else if (this.fadingOut) {
				end = 1;
			}
			
			double percent = (System.currentTimeMillis() - this.fadeTime) / (double) this.fadeDuration;
			if (percent >= 1){
				this.fadeAlpha = end;
				
				if (this.fadingIn) {
					this.fadingIn = false;
					this.game.onFinishedFadeIn();
				} else {
					this.fadingOut = false;
					this.game.onFinishedFadeOut();
				}
				
			} else {
				
				this.fadeAlpha = start + (end - start) * percent;
				
			}
			
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
		
		if (this.fadeAlpha >= 0) {
			
			g.setColor(new Color(0, 0, 0, (int)(this.fadeAlpha * 255)));
			g.fillRect(0, 0, this.w, this.h);
			
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
	 * Slowly fades out
	 */
	public void fadeOut() {
		this.fadingOut = true;
		this.fadeTime = System.currentTimeMillis();
	}
	
	/*
	 * Slowly fades in
	 */
	public void fadeIn() {
		this.fadingIn = true;
		this.fadeTime = System.currentTimeMillis();
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
