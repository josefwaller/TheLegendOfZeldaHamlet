package entities;

import java.util.ArrayList;

import music.SoundStore;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;

import entities.abstracts.AnimatedEntity;
import entities.abstracts.EnemyEntity;
import entities.abstracts.Entity;
import entities.abstracts.InteractiveEntity;
import entities.abstracts.ThrowableEntity;
import sprites.AnimationStore;
import sprites.Animation;
import game.Game;

/*
The main player class.
*/
public class Player extends AnimatedEntity {

	// the different things the player can do
	public static final int STATE_IDLE = 0;
	public static final int STATE_ATTACKING = 1;
	public static final int STATE_CARRYING = 2;
	public static final int STATE_FLINCHING = 3;
	public static final int STATE_DYING = 4;
	
	// the current thing the player is doing
	private int state;
	
	// the speed at which the player runs
	private int speed = 90;
	
	// the health the player has
	private int health = 3;
	
	// the max number of hearts the player can have
	private int maxHealth = 3;
	
	// how long the attack takes
	private int attackDuration = 300;
	
	// when the attack started
	private long attackTime;
	
	// the range at which the player can attack enemies
	private int attackRange = 8;
	
	// the range at which the player can interact with objects
	private int interactRange = 10;
	
	// the standing animations
	private Animation standUp;
	private Animation standDown;
	private Animation standSide;
	private Animation standUpShield;
	private Animation standDownShield;
	private Animation standSideShield;
	
	// the running animations
	private Animation runUp;
	private Animation runDown;
	private Animation runSide;
	private Animation runUpShield;
	private Animation runDownShield;
	private Animation runSideShield;
	
	// the attacking animations
	private Animation attackUp;
	private Animation attackDown;
	private Animation attackSide;
	
	// the picking up animations
	private Animation pickUpUp;
	private Animation pickUpSide;
	private Animation pickUpDown;
	
	// the flinching animations
	private Animation flinchUp;
	private Animation flinchSide;
	private Animation flinchDown;
	
	private Animation win;
	
	// the death animations
	private Animation spin;
	private Animation death;
	
	// the different sounds
	private Sound hurtSound;
	private Sound lowHealthSound;
	private Sound deathSound;
	private Sound swordSound;
	
	// the object the player is picking up
	private ThrowableEntity carriedObject;
	
	// the time inbetween sprite changes while running
	private int runDuration = 100;
	
	// the direction the player was facing when hit
	private int flinchDirection;
	
	// the time the player flinches after being hit
	private int flinchDuration = 200;
	
	// the speed at which the player moves when flinching
	private int flinchSpeed = 150;
	
	// the time the player was hit
	private long hitTime;
	
	// whether the player is showing
	// used to create the blink effect after being hit
	private boolean isShowing = true;
	
	// whether the play is blinking after being hit by an enemy
	private boolean isBlinking = false;
	
	// the time inbetween each blink when the player is invincible after being hit
	private int blinkFrequency = 200;
	
	// the duration the player is blinking after being hit
	private int blinkDuration = 1000;
	
	// the time the player spins before falling over
	private int deathDuration = 1000;
	
	/*
	Creates a new player
	*/
	public Player(int x, int y, Game g)
	{
		// sets position and game
		super(x, y, 18, 24, g);
		
		this.state = Player.STATE_IDLE;
		
		String sheet = "assets/images/linkspritesheet";
		
		AnimationStore a = AnimationStore.get();;
		
		// loads standing sprite
		this.standDown = a.getAnimation(sheet, "standdown");
		this.standSide = a.getAnimation(sheet, "standside");
		this.standUp = a.getAnimation(sheet, "standup");
		this.standDownShield = a.getAnimation(sheet, "standdownshield");
		this.standSideShield = a.getAnimation(sheet, "standsideshield");
		this.standUpShield = a.getAnimation(sheet, "standupshield");
		
		// creates animations
		this.runUp = a.getAnimation(sheet, "runup");
		this.runDown = a.getAnimation(sheet, "rundown");
		this.runSide = a.getAnimation(sheet, "runside");
		this.runUpShield = a.getAnimation(sheet, "runupshield");
		this.runDownShield = a.getAnimation(sheet, "rundownshield");
		this.runSideShield = a.getAnimation(sheet, "runsideshield");
		
		// these durations are set after, bacuse they depend on the animation's length
		this.attackUp = a.getAnimation(sheet, "attackup");
		this.attackSide = a.getAnimation(sheet, "attackside");
		this.attackDown = a.getAnimation(sheet, "attackdown");
		
		// sets the attack animation's duration
		this.attackUp.setDuration(this.attackDuration / this.attackUp.getAnimLength());
		this.attackSide.setDuration(this.attackDuration / this.attackSide.getAnimLength());
		this.attackDown.setDuration(this.attackDuration / this.attackDown.getAnimLength());
		
		// creates animation for picking up
		this.pickUpUp = a.getAnimation(sheet, "pickupup");
		this.pickUpSide = a.getAnimation(sheet, "pickupside");
		this.pickUpDown = a.getAnimation(sheet, "pickupdown");
		
		// gets animations for flinching and dying
		this.flinchUp = a.getAnimation(sheet, "damageup");
		this.flinchSide = a.getAnimation(sheet, "damageside");
		this.flinchDown = a.getAnimation(sheet, "damagedown");
		this.spin = a.getAnimation(sheet, "spin");
		this.death = a.getAnimation(sheet, "death");
		
		this.win = a.getAnimation(sheet, "win");
		
		// loads sounds
		this.hurtSound = SoundStore.get().getSound("assets/sfx/playerhit.wav");
		this.deathSound = SoundStore.get().getSound("assets/sfx/playerdeath.wav");
		this.lowHealthSound = SoundStore.get().getSound("assets/sfx/lowhealth.wav");
		this.swordSound = SoundStore.get().getSound("assets/sfx/playerattack.wav");
		
		// adds hitbox
		this.addHitbox(2, 2, 14, 20);
		
		// sets to go down for default
		this.setAnim(this.runDown, this.runDuration);
	}
	
	/*
	 * Updates the player position
	 * Moves, takes/deals damage, etc
	 */
	public void update(Input input, int delta)
	{
		
		if (this.health == 1 && !this.lowHealthSound.playing()) {
			this.lowHealthSound.play();
		}
		
		// updates hitboxes
		this.updateHitboxes();
		
		switch (this.state) {
			case Player.STATE_CARRYING:
				
				// sets animation
				switch (this.direction) {
					case Entity.DIR_DOWN:
						this.setAnim(this.pickUpDown, 100);
						break;

					case Entity.DIR_LEFT:
					case Entity.DIR_RIGHT:
						this.setAnim(this.pickUpSide, 100);
						break;
		
					case Entity.DIR_UP:
						this.setAnim(this.pickUpUp, 100);
						break;
				}
				this.animUpdate();
				
				if (input.isKeyPressed(Input.KEY_SPACE)) {
					this.carriedObject.startThrow(this.direction);
					this.state = Player.STATE_IDLE;
				}
				break;
			
			case Player.STATE_ATTACKING:
				
				// checks if the player is done attacking
				if (System.currentTimeMillis() - this.attackTime >= this.attackDuration) {
					this.state = Player.STATE_IDLE;
				}
				// checks if it hits anything
				this.checkForAttack();
				
				this.animUpdate();
				break;
			
			case Player.STATE_IDLE:
				
				boolean isRunning = false;

				// checks if the player needs to move
				if (input.isKeyDown(Input.KEY_LEFT))
				{
					this.tryToMove(this.x - (this.speed * delta / 1000f), this.y);
					this.direction = Entity.DIR_LEFT;
					
					this.setAnim(this.runSideShield, this.runDuration);
					isRunning = true;
				}
				else if (input.isKeyDown(Input.KEY_RIGHT))
				{
					this.tryToMove(this.x + (this.speed * delta / 1000f), this.y);

					this.direction = Entity.DIR_RIGHT;
					this.setAnim(this.runSideShield, this.runDuration);
					isRunning = true;
				}
				if (input.isKeyDown(Input.KEY_UP))
				{
					this.tryToMove(this.x, this.y - (this.speed * delta / 1000f));
					
					// checks it is not just overwriting an animation that was already set
					// earlier this frame.
					if (!(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_RIGHT))) {
						
						this.direction = Entity.DIR_UP;
						this.setAnim(this.runUpShield, this.runDuration);
					}
					isRunning = true;
				}
				else if (input.isKeyDown(Input.KEY_DOWN))
				{
					this.tryToMove(this.x, this.y + (this.speed * delta / 1000f));
					
					if (!(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_RIGHT))) {
						
						this.direction = Entity.DIR_DOWN;
						this.setAnim(this.runDownShield, this.runDuration);
					}
					isRunning = true;
				}
				
				if (input.isKeyPressed(Input.KEY_SPACE)) {
					
					// interacts with anything if it can, otherwise swings it's sword
					if (!this.tryToInteract()) {
						
						this.swingSword();
						
					}
				} else if (isRunning) {
					
					this.loop = true;
					this.animUpdate();
					
				} else {
					
					// sets standing animation
					switch (this.direction) {
						case Entity.DIR_DOWN:
							this.setAnim(this.standDownShield, this.runDuration);
							break;
						case Entity.DIR_UP:
							this.setAnim(this.standUpShield, this.runDuration);
							break;
						case Entity.DIR_LEFT:
						case Entity.DIR_RIGHT:
							this.setAnim(this.standSideShield, this.runDuration);
							break;
					}
				}
				break;
				
			case Player.STATE_FLINCHING:
				
				this.flinch(delta);
				break;
				
			case Player.STATE_DYING:
				
				this.die();
				break;
		}
		
		// checks if the player is still invincible from being hit by an enemy
		if (this.isBlinking) {

			if (System.currentTimeMillis() - this.hitTime >= this.blinkDuration) {
				
				this.isShowing = true;
				this.isBlinking = false;
			} else {
				
				this.isShowing = Math.floor(System.currentTimeMillis() / this.blinkFrequency) % 2 == 0;
			}
		}
	}

	/*
	 * Renders the player
	 */
	public void render(Graphics g) {
		
		if (this.isShowing) {

			super.render(g);
			
			if (this.game.isDebug()) {

				g.fillRect(
					this.getCoordsInFront(this.attackRange)[0],
					this.getCoordsInFront(this.attackRange)[1],
					2,
					2
				);
			}
		}
	}
	
	/*
	 * Plays the winning animation
	 */
	public void onWin() {
		
		// sets animation
		this.setAnim(this.win, 0);
		this.loop = false;
		this.animUpdate();
	}
	
	/*
	 * Plays the death animation
	 */
	private void die() {
		
		// checks if the player is done spinning
		if (System.currentTimeMillis() - this.hitTime > this.deathDuration) {
			
			/// plays death animation
			this.loop = false;
			this.setAnim(this.death, 100);
			
			// notifies game
			this.game.onPlayerDeath();
			
		} else {
			
			// sets spin animation
			this.setAnim(this.spin, 100);
		}
		
		this.animUpdate();
	}
	
	/*
	 * Plays the flinching animation
	 */
	private void flinch(long delta) {
	
		// checks if the player is done flinching
		if (System.currentTimeMillis() - this.hitTime > this.flinchDuration) {
			this.state = Player.STATE_IDLE;
			
		} else {
			
			super.loop = false;
			
			// gets the distacne to move this frame
			float distance = this.flinchSpeed * delta / 1000f;
			
			// sets animation and moves
			switch(this.flinchDirection) {
			
				case Entity.DIR_UP:
					
					this.setAnim(this.flinchUp, 0);
					this.tryToMove(this.x, this.y + distance);
					break;

				case Entity.DIR_LEFT:
					
					this.tryToMove(this.x + distance, this.y);
					this.setAnim(this.flinchSide, 0);
					break;
					
				case Entity.DIR_RIGHT:
					
					this.tryToMove(this.x - distance, this.y);
					this.setAnim(this.flinchSide, 0);
					break;
					
				case Entity.DIR_DOWN:
					
					this.setAnim(this.flinchDown, 0);
					this.tryToMove(this.x, this.y - distance);
					break;
			}
			
		}
		
	}
	
	/*
	 * Damages the player
	 */
	public void onHit() {
		
		// checks that the player isn't still invincible
		if (!this.isBlinking) {
			
			if (this.state != Player.STATE_FLINCHING && this.state != Player.STATE_DYING) {
				
				this.health -= 1;
				this.hitTime = System.currentTimeMillis();
				
				if (this.health > 0) {
					
					this.hurtSound.play();
					this.state = Player.STATE_FLINCHING;
					this.flinchDirection = this.direction;
					this.isBlinking = true;
				
				} else {

					this.deathSound.play();
					this.state = Player.STATE_DYING;
					this.game.startPlayerDeath();
				}
			}
		}
		
	}
	
	/*
	 * When the player picks up a heart
	 */
	public void onHeart() {
		
		if (this.health < this.maxHealth) {
			this.health++;
		}
		
	}
	
	/*
	 * Checks if the player can attack an enemy, and 
	 * does so if it can. Should be called only if 
	 * the player is attacking
	 */
	private void checkForAttack() {
		
		// gets the coordinates it attacks
		int[] attackCoords = this.getCoordsInFront(this.attackRange);
		
		// gets all the enemies
		EnemyEntity[] enemies = this.game.getEnemies();
		
		// checks if it hits any
		for (int i = 0; i < enemies.length; i++) {
			
			if (enemies[i].collidesWithPoint(attackCoords[0], attackCoords[1])) {
				
				enemies[i].onHit();
			}
		}
		
	}
	
	/*
	 * Sets up the animation to pick up an entity
	 */
	public void pickUpObject(ThrowableEntity obj) {
		
		this.state = Player.STATE_CARRYING;
		this.loop = false;
		this.carriedObject = obj;
	}
	
	/*
	 * Sets up to play the sword attack animation 
	 * for whichever direction the player is facing and deals damage to entities
	 */
	private void swingSword() {
		
		// sets up to attack
		this.state = Player.STATE_ATTACKING;
		this.attackTime = System.currentTimeMillis();
		this.loop = false;
		this.swordSound.play();
		
		// sets animation to attack
		switch (this.direction) {
			case Entity.DIR_UP: 
				this.setAnim(
					this.attackUp, 
					this.attackDuration / this.attackUp.getAnimLength());
				break;
			case Entity.DIR_LEFT:
			case Entity.DIR_RIGHT:
				this.setAnim(
					this.attackSide, 
					this.attackDuration / this.attackSide.getAnimLength());
				break;
			case Entity.DIR_DOWN:
				this.setAnim(
					this.attackDown, 
					this.attackDuration / this.attackDown.getAnimLength());
				break;
		}
	}

	
	/*
	 * Called when the player hits space. Returns
	 * true if there is an object the player interacts with,
	 * and false if there is not
	 */
	private boolean tryToInteract() {
		
		// the position it should check for the interactive
		int[] point = this.getCoordsInFront(this.interactRange);
		int x = point[0];
		int y = point[1];
		
		// gets all the interactive objects
		ArrayList<InteractiveEntity> objs = this.game.getInteractiveObjects();
		
		for (int i = 0; i < objs.size(); i++) {
			InteractiveEntity obj = objs.get(i);
			
			// checks if they are within range to interact with
			if (obj.getX() < x && obj.getX() + obj.getW() > x) {
				if (obj.getY() < y && obj.getY() + obj.getH() > y) {
					
					// interacts with it
					obj.onPlayerInteract();
					
					return true;
					
				}
			}
			
		}
		
		return false;
		
	}

	/*
	 * Get/Set
	 */
	public int getHealth() {
		return this.health;
	}
}