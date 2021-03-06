package hud;

import game.Game;

import java.awt.Font;
import java.io.InputStream;

import music.SoundStore;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.util.ResourceLoader;

import sprites.SpriteStore;

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
	
	private boolean showingMainMenu = true;
	
	// whether it is fading out and/or fading in
	private boolean fadingOut;
	private boolean fadingIn;
	
	// the time it began fading in/out
	private long fadeTime;
	
	// the time it takes to fade in/out
	private int fadeDuration = 400;
	
	// the current alpha of the black overlay
	private double fadeAlpha = 0.0;

	private UnicodeFont gameOverFontBig;
	private UnicodeFont gameOverFontSmall;
	
	// the heart image to use
	private Image heart;
	
	// the offsets with which to draw the hearts
	private int heartX = 20;
	private int heartY = 20;
	
	// the size of the hearts
	private int heartS = 40;
	
	// the main menu
	private MainMenu mainMenu;
	
	// whether the HuD is showing credits
	private boolean isShowingCredits = false;
	
	private UnicodeFont creditFont;
	private String creditText = "The End";
	private long creditTime;
	private int creditDuration = 2000;
	
	
	/*
	 * Constructor
	 * 
	 * Sets up Heads up display
	 */
	public HeadsUpDisplay(int w, int h, Game g) {

		// sets width and height
		this.w = w;
		this.h = h;
		
		this.mainMenu = new MainMenu(this.w, this.h);
		
		this.game = g;
		
		// initializes components of the hud
		this.dialog = new DialogManager(this.w, this.h, this);

		this.gameOverFontBig = HeadsUpDisplay.loadFont("RetGanon.ttf", 70);
		this.gameOverFontSmall = HeadsUpDisplay.loadFont("RetGanon.ttf", 45);
		this.creditFont = HeadsUpDisplay.loadFont("RetGanon.ttf", 60);
		
		this.heart = SpriteStore.get().loadSprite("assets/images/heart.png");
	}
	
	/*
	 * Checks for input that effects the HUD
	 */
	public void update(Input input ) {
		
		if (this.isShowingCredits) {
			if (System.currentTimeMillis() - this.creditTime >= this.creditDuration) {
				this.isShowingCredits = false;
				
				this.showingMainMenu = true;
				this.mainMenu.start();
			}
		}
		
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			
			if (this.showingDialog) {
				this.dialog.updateText();
			}
			
			if (this.showingMainMenu) {
				this.fadeOut();
			}
			
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

		if (this.showingMainMenu) {
			
			this.mainMenu.render(g);
			
		} else if (this.isShowingCredits) {
			
			g.setColor(Color.black);
			g.fillRect(0, 0, this.w, this.h);
			
			
			g.setFont(this.creditFont);
			g.setColor(Color.white);
			
			g.drawString(this.creditText, 
					(this.w - this.creditFont.getWidth(this.creditText)) / 2,
					(this.h - this.creditFont.getHeight(this.creditText)) / 2);
			
		} else {

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
			} else {
				
				// draws the health
				
				g.setColor(Color.black);
				g.fillRect(0, 0, this.w, 2 * this.heartY + this.heartS);
				
				// draws the hearts
				int hearts = this.game.getPlayer().getHealth();
				
				for (int i = 0; i < hearts; i++) {
					
					this.heart.draw(
						this.heartX + i * (this.heartS + this.heartX),
						this.heartY,
						this.heartS,
						this.heartS);
					
				}
				
				
			}
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
	 * Starts the credits
	 */
	public void startCredits() {
		
		this.isShowingCredits = true;
		this.creditTime = System.currentTimeMillis();
	}
	
	public void showMainMenu() {
		this.showingMainMenu = true;
	}
	
	public boolean showingMainMenu() {
		return this.showingMainMenu;
	}
	/*
	 * Slowly fades out
	 */
	public void fadeOut() {
		this.fadingOut = true;
		this.fadeTime = System.currentTimeMillis();
		
		// sets the music volume to fade out with the sound
		SoundStore.get().fadeMusic(this.fadeDuration, 0f, true);
	}
	
	/*
	 * Slowly fades in
	 */
	public void fadeIn() {
		this.fadingIn = true;
		this.fadeTime = System.currentTimeMillis();
		
		if (this.showingMainMenu) {
			this.showingMainMenu = false;
			this.mainMenu.stop();
		}
		
		// sets the music volume to fade in with the sound
		SoundStore.get().fadeMusic(this.fadeDuration, 1f, false);
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
