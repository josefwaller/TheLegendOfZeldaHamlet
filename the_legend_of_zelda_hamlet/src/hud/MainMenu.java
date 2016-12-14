package hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;

import music.SoundStore;
import sprites.SpriteSheet;
import sprites.SpriteStore;

public class MainMenu {
	
	public static final int STAGE_TRI = 0;
	public static final int STAGE_LOGO = 1;
	public static final int STAGE_SWORD = 2;
	public static final int STAGE_FLASH = 3;
	public static final int STAGE_END = 4;
	public static final int STAGE_FADE = 5;
	
	private int stage = 0;
	
	private int w;
	private int h;
	
	// the spritesheet of triforces
	private SpriteSheet triforces;
	
	// the time the triforce animation started
	private long stageTime;
	
	// the time it takes to play the triforce animation
	private int triforceDuration = 5000;
	
	// the number of triforce sprites
	private int numberOfTriforces = 170;
	
	// the width and height of the final triforce sprite
	private int triforceW;
	private int triforceH;
	
	// the sound to play 
	private Sound s;
	  
	private int logoDelay = 1600;
	private int logoDuration = 500;
	
	private int swordDelay = 1000;
	private int swordDuration = 100;
	
	private int flashDuration = 300;
	
	private int endDuration = 10000;
	
	private int fadeDuration = 2000;
	
	// the logo
	private Image logo;
	private Image logoWithSword;
	
	private Sound swordSound;
	
	// the sword
	private Image sword;
	  
	public MainMenu(int w, int h) {
		
		this.w = w;
		this.h = h;
		
		// loads the triforces
		this.triforces = SpriteStore.get()
				.loadSpriteSheet("assets/images/mainmenu", "assets/images/triforce");

		this.triforceH = this.triforces.getSprite("/170").getHeight() * 3;
		this.triforceW = this.triforces.getSprite("/170").getWidth() * 3;
		
		// records time
		this.stageTime = System.currentTimeMillis();

		this.logo = SpriteStore.get().loadSprite("assets/images/mainmenulogo.png")
				.getScaledCopy(3f);
		
		this.logoWithSword = SpriteStore.get().loadSprite("assets/images/mainmenulogosword.png")
				.getScaledCopy(3f);

		this.sword = SpriteStore.get().loadSprite("assets/images/mainmenusword.png")
				.getScaledCopy(3f);
		
		this.swordSound = SoundStore.get().getSound("assets/sfx/menusword.wav");
		this.s = SoundStore.get().getSound("assets/music/wav/intro.wav");
		
		this.start();
	}
	
	public void start() {
		
		this.stage = MainMenu.STAGE_TRI;
		this.stageTime = System.currentTimeMillis();
		s.play();
	}
	
	
	public void render(Graphics g) {

		long since = System.currentTimeMillis() - this.stageTime;
		
		switch (this.stage) {
			case MainMenu.STAGE_TRI:
				
				double percent = (since / (double)this.triforceDuration);
				
				if (percent >= 1.0) {
					percent = 1.0;
					this.stage = MainMenu.STAGE_LOGO;
					this.stageTime = System.currentTimeMillis();
				}

				Image triforce = this.triforces.getSprite("/" + (int)Math.floor(numberOfTriforces * percent))
						.getScaledCopy(3f);
								
				if (percent < 0.4) {
					triforce.setAlpha((float)(10/4 * percent));
					
				}
				// top
				triforce.drawCentered(
						(float)((this.w / 2)), 
						(float)((this.h / 2 - this.triforceH / 2) * percent));

				// right
				triforce.drawCentered(
						(float)(this.w - (this.w / 2 - this.triforceW / 2) * percent), 
						(float)(this.h - (this.h / 2 - this.triforceH / 2) * percent));
				
				// left
				triforce.drawCentered(
						(float)((this.w / 2 - this.triforceW / 2) * percent), 
						(float)(this.h - (this.h / 2 - this.triforceH / 2) * percent));
				break;
				
			case MainMenu.STAGE_LOGO:
				
				this.drawTriforce();
				
				if (since >= this.logoDelay) {
					
					percent = (since - this.logoDelay)
						/ (double)this.logoDuration;
					
					if (percent > 1.0) {
						percent = 1.0;
					}
					
					this.logo.setAlpha((float)percent);
					
					this.logo.drawCentered(this.w / 2, this.h / 2);
					
					if (percent == 1.0) {
						this.stageTime = System.currentTimeMillis();
						this.stage = MainMenu.STAGE_SWORD;
						
					}
				}
				
				break;
				
			case MainMenu.STAGE_SWORD:
				
				this.drawTriforce();
				this.logo.drawCentered(this.w / 2, this.h / 2);
				
				if (since >= this.swordDelay) {
					
					percent = (since - this.swordDelay) / (double)this.swordDuration;
					
					this.sword.drawCentered(
						this.w * 7 / 22f - 20,
						(this.h / 2 - this.sword.getHeight() / 2) * (float)percent);
					
					if (percent >= 1) {
						this.stageTime = System.currentTimeMillis();
						this.stage = MainMenu.STAGE_FLASH;
						this.swordSound.play();
					}
				}
				
				break;
				
			case MainMenu.STAGE_FLASH:
				
				Color[] c = {
					Color.white,
					Color.red,
					Color.blue,
					Color.green
				};
				
				g.setColor(c[(int)Math.floor(since / 50.0) % 4]);
				g.fillRect(0, 0, this.w, this.h);
				
				if (since >= this.flashDuration) {
					this.stage = MainMenu.STAGE_END;
					this.stageTime = System.currentTimeMillis();
				}
				break;
				
			case MainMenu.STAGE_END:
				
				this.drawTriforce();
				this.logoWithSword.drawCentered(this.w / 2, this.h / 2);

				
				if (since >= this.endDuration) {
				
					this.stage = MainMenu.STAGE_FADE;
					this.stageTime = System.currentTimeMillis();
				}
				break;
				
			case MainMenu.STAGE_FADE:
				
				this.drawTriforce();
				this.logoWithSword.drawCentered(this.w / 2, this.h / 2);
				
				g.setColor(Color.black);
				percent = (since) / (double)this.fadeDuration;
				
				g.setColor(new Color(0f, 0f, 0f, (float)percent));
				
				g.fillRect(0, 0, this.w, this.h);
				
				if (since > this.fadeDuration) {

					//restarts
					this.start();
				}
				
			break;
		}
		
	}
	
	public void stop() {
		
		this.s.stop();
		
	}
	
	private void drawTriforce() {
		
		Image tri = this.triforces.getSprite("/170").getScaledCopy(3f);
		
		tri.drawCentered(
			(float)(this.w / 2),
			(float)((this.h - this.triforceH) / 2));
		
		tri.drawCentered(
			(float)((this.w - this.triforceW) / 2),
			(float)((this.h + this.triforceH) / 2));

		tri.drawCentered(
			(float)((this.w + this.triforceW) / 2),
			(float)((this.h + this.triforceH) / 2));
		
	}
}
