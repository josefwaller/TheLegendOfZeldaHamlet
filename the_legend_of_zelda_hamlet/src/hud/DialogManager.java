package hud;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;

import sprites.SpriteStore;

/*
 * Handles dialog splitting, rendering, or anything else
 * needed to properly display dialog
 */
public class DialogManager {
	
	// the lines of dialog
	private ArrayList<String> dialogLines;
	
	// the index of the current dialog line being shown at the top of the box
	private int dialogIndex;
	
	// the dimensions of the dialog box
	private int dialogX;
	private int dialogY;
	private int dialogW;
	private int dialogH;
	
	// the width of the dialog border
	private int dialogBorderW;
	
	// the space in between the dialog border and the text
	private int dialogPadding;
	
	// the images that make up the dialog box's border
	private Image dialogBottom;
	private Image dialogBottomLeft;
	private Image dialogLeft;
	private Image dialogTopLeft;
	private Image dialogTop;
	private Image dialogTopRight;
	private Image dialogRight;
	private Image dialogBottomRight;
	
	// the dialog font
	private UnicodeFont dialogFont;

	private HeadsUpDisplay hud;
	
	public DialogManager (int w, int h, HeadsUpDisplay hud){
		
		// the dimensions of the dialog box 
		this.dialogW = w  * 3/4;
		this.dialogX = (w - this.dialogW) / 2;
		this.dialogH = h * 1/4;
		this.dialogY = h * 4/7;
		
		this.hud = hud;
		
		// the width of the border around the dialog
		this.dialogBorderW = this.dialogH / 10;
		
		// this space inbetween the border and the test
		this.dialogPadding = this.dialogH / 10;
		
		// the lines of dialog currently being shown
		this.dialogLines = new ArrayList<String>();
		
		// loads the dialog iamges
		String dialogUrl = "assets/images/dialog/%s.png";

		this.dialogBottom = SpriteStore.get().loadSprite(String.format(dialogUrl, "bot"));
		this.dialogBottomRight = SpriteStore.get().loadSprite(String.format(dialogUrl, "botrightcorner"));
		this.dialogRight = SpriteStore.get().loadSprite(String.format(dialogUrl, "right"));
		this.dialogTopRight = SpriteStore.get().loadSprite(String.format(dialogUrl, "toprightcorner"));
		this.dialogTop = SpriteStore.get().loadSprite(String.format(dialogUrl, "top"));
		this.dialogTopLeft = SpriteStore.get().loadSprite(String.format(dialogUrl, "topleftcorner"));
		this.dialogLeft = SpriteStore.get().loadSprite(String.format(dialogUrl, "left"));
		this.dialogBottomLeft = SpriteStore.get().loadSprite(String.format(dialogUrl, "botleftcorner"));
		
		// loads the dialog font
		this.dialogFont = HeadsUpDisplay.loadFont("RetGanon.ttf");
	}
	
	/*
	 * Draws the dialog box and dialog on the screen
	 */
	public void render(Graphics g) {
		
		// how wide the border should be
		int borderWidth = this.dialogBorderW;
		
		// draws the top and bottom dialog outline
		for (int i = 1; i <= this.dialogW / borderWidth; i++) {
			
			// this coordinates of this tile of the border
			int borderX = this.dialogX + borderWidth * i;
			
			// draws the top border of the box
			this.dialogTop.draw(borderX, this.dialogY, borderWidth, borderWidth);
			
			// draws the bottom
			this.dialogBottom.draw(borderX, this.dialogY + this.dialogH, borderWidth, borderWidth);
		}
		
		// draws the left and right dialog outline
		for (int i = 1; i <= this.dialogH / borderWidth; i++) {
			
			int borderY = this.dialogY + borderWidth * i;
			
			// draws the left side
			this.dialogLeft.draw(this.dialogX, borderY, borderWidth, borderWidth);
			
			// draws the right side
			this.dialogRight.draw(this.dialogX + this.dialogW, borderY, borderWidth, borderWidth);
			
		}
		
		// draws the corners
		this.dialogTopLeft.draw(this.dialogX, this.dialogY, borderWidth, borderWidth);
		this.dialogTopRight.draw(this.dialogX + this.dialogW, this.dialogY, borderWidth, borderWidth);
		this.dialogBottomLeft.draw(this.dialogX, this.dialogY + this.dialogH, borderWidth, borderWidth);
		this.dialogBottomRight.draw(this.dialogX + this.dialogW, this.dialogY + this.dialogH, borderWidth, borderWidth);
		
		// draws the lines of dialog
		for (int i = 0; (i + this.dialogIndex) < this.dialogLines.size() && (i + 1) * this.dialogFont.getLineHeight() < this.dialogH; i++) {
			this.hud.drawBorderedText(
				this.dialogFont,
				Color.white,
				new Color(4, 0, 128),
				this.dialogX + this.dialogPadding, 
				this.dialogY + this.dialogPadding + i * this.dialogFont.getLineHeight(),
				2,
				this.dialogLines.get(i + this.dialogIndex));
		}
	}
	
	/*
	 * Updates the text in the dialog box
	 */
	public void updateText() {
		
		// adds the number of lines in the dialog box
		this.dialogIndex += Math.floor(this.dialogH / (double)this.dialogFont.getLineHeight());
	}
	
	/*
	 * Returns whether the NPC or sign or whatever is done talking
	 */
	public boolean isDone() {
		return (this.dialogIndex > this.dialogLines.size());
	}
	
	/*
	 * Sets up to show the dialog given
	 */
	public void startDialog(String dialog) {

		// resets dialog lines
		this.dialogLines = new ArrayList<String>();
		this.dialogIndex = 0;
		
		// breaks the dialog up into lines
		String[] words = dialog.split(" ");
		
		// adds the first word
		this.dialogLines.add(words[0]);
		
		for (int i = 1; i < words.length; i++){
			
			// gets the current line of dialog
			int lastLine = this.dialogLines.size() - 1;
			String currentLine = this.dialogLines.get(lastLine);
			
			// makes a new line of dialog
			String newLine = currentLine + " " + words[i];
			
			// checks if it will fit
			if (this.dialogFont.getWidth(newLine) < this.dialogW - 2 * this.dialogPadding) {
				
				// adds it to the line
				this.dialogLines.set(lastLine, newLine);
				
			} else {
				
				// creates a new line for the next word
				this.dialogLines.add(words[i]);
			}
		}
	}

}
