package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

import music.SoundStore;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.tiled.TiledMap;













// different entities
import entities.Button;
import entities.AutomatedEntity;
import entities.Door;
import entities.MaceSoldier;
import entities.NPC;

// The player Entity
import entities.Player;
import entities.Pot;
import entities.Soldier;
import entities.SpriteChanger;
import entities.abstracts.CollisionEntity;
import entities.abstracts.EnemyEntity;
// superclasses
import entities.abstracts.Entity;
import entities.abstracts.InteractiveEntity;
import entities.abstracts.StaticEntity;

// the heads up display
import hud.HeadsUpDisplay;

/*
The main Game class.
This is the main class which runs the program
*/
public class Game extends BasicGame {
	
	/*    Static variables    */
	
	// the different values in the map array
	static final int MAP_EMPTY = 0;
	static final int MAP_BLOCKED = 1;

	
	/*    Non-static variables    */
	
	// The dimensions of the window
	private int windowW;
	private int windowH;
	
	// the final width and height of the screen
	// used for positioning elements
	private int w;
	private int h;
	
	// how many TiledMap tiles in a row are on one screen at once
	// used to tell how wide the TiledMap should be
	private int widthInTiles = 10;
	
	// The map
	private TiledMap map;
	
	// the x and y offset to draw everything with
	// so that the player is centered
	private int cameraX;
	private int cameraY;
	
	// A 2D array of whether an entity can walk on these tiles
	private boolean[][] blocked;
	
	// An array of the different sections of the map
	// which the camera does a special transition inbetween
	private ArrayList<int[]> sections;
	
	// whether the player has walked off screen or through a door and the screen is panning towards the new area
	private boolean isPlayingTransition = false;
	
	// the time the transition started and how long it takes
	private long transStartTime;
	private long transDuration = 1000;
	
	// the starting position of the camera during the transition
	private int camStartX = 0;
	private int camStartY = 0;
	
	// the final position of the camera during the transition
	private int camEndX = 0;
	private int camEndY = 0;
	
	// the section that the transition is moving towards
	private int newSection;
	
	// the index of the section that the player is currently in
	private int currentSection = 0;
	
	// the player entity
	private Player player;
	
	// whether the player is currently dying
	private boolean playerIsDying = false;
	
	// whether the player is currently dead
	private boolean playerIsDead = false;
	
	// the font to use when rendering the death screen
	private UnicodeFont deathFont;
	private int deathFontSize = 24;

	// all objects on the current map
	private ArrayList<StaticEntity> objects;
	
	// all entities (Except the player) on the screen
	private ArrayList<EnemyEntity> enemies;
	
	// all animations that will be played
	private ArrayList<AutomatedEntity> animations;
	
	// all consumables on the map
	private ArrayList<CollisionEntity> consumables;
	
	// whether or not dialog is currently being shown on screen
	private boolean isShowingDialog;	
	
	// the Heads Up Display
	private HeadsUpDisplay hud;
	
	// the map the player is in
	private String currentMap;
	
	// the id of the door in the new map the player is going to
	// if there was no new player spawned, it should spawn the player at this door
	private int newPathId;
	
	// whether the game is paused or not
	private boolean isPaused = false;
	
	// whether the player has won
	private boolean hasWon = false;
	
	// the time that the player won
	private long winTime;
	
	// the time it takes before fading to credits after winning
	private int winDuration = 10000;
	
	// whether the game is starting to the credits
	private boolean startingCredits = false;
	
	// whether the game is currently laying the credits
	private boolean playingCredits = false;
	/*
	Returns a new Game
	Sets up window and game
	*/
	public Game(int w, int h) {

		// sets the title
		super("The Legend of Zelda: Hamlet");
		
		// records width and height
		this.windowW = w;
		this.windowH = h;
	}

	/*
	 * Initializes all components of the game
	 */
	public void init(GameContainer container) {
		
		// the width one tile should be
		int finalTileWidth = (this.windowW / this.widthInTiles);
		
		// the conversion factor between the tiledmap tile width (16) and the final width
		double conversion = 16.0 / (double)finalTileWidth;
		
		// the width and height used for positioning
		this.w = (int) (this.windowW * conversion);
		this.h = (int) (this.windowH * conversion);
		
		// the hud
		this.hud = new HeadsUpDisplay(this.windowW, this.windowH, this);
		
		// loads the font
		this.deathFont = HeadsUpDisplay.loadFont("RetGanon.ttf");
		this.currentMap = "castleone.tmx";
		this.hud.showMainMenu();
	}
	
	/*
	 * Updates everything
	 * Position, game logic, etc
	 */
	public void update(GameContainer container, int delta) {

		// gets input
		Input input = container.getInput();
		
		if (!this.isPaused && !this.hud.showingMainMenu() && !this.hasWon) {

			if (this.playerIsDead) {
				
				// checks for input
				if (input.isKeyPressed(Input.KEY_SPACE)) {
					
					this.playerIsDead = false;
					this.playerIsDying = false;
					this.hud.hideGameOver();
					
					this.player = null;
					
					this.loadMap("assets/maps/" + this.currentMap);
					this.loadObjects();
					
					if (this.player == null) {
						this.spawnPlayerAtDoor(this.newPathId);
					}
					
					this.currentSection = this.getNewSection();
				}
			}
			
			if (!this.isPlayingTransition && !this.isShowingDialog) {
				
				// updates the player
				this.player.update(input, delta);
				
				if (!this.playerIsDying) {

					// updates objects
					for (int o = 0; o < this.objects.size(); o++) {
						
						if (this.objects.get(o).getSection() == this.currentSection) {
							this.objects.get(o).update();
						}
					}
					
					// updates animations
					for (int i = 0; i < this.animations.size(); i++) {
						this.animations.get(i).update(delta);
					}
					
					// updates enemies
					for (int i = 0; i < this.enemies.size(); i++) {
						if (this.enemies.get(i).getSection() == this.currentSection) {
							this.enemies.get(i).update(delta);
						}
					}
					
					for (int i = 0; i < this.consumables.size(); i++) {
						if (this.consumables.get(i).getSection() == this.currentSection) {
							this.consumables.get(i).update();
						}
					}
				}
				
			} else if (this.isPlayingTransition){
				
				if (System.currentTimeMillis() - this.transStartTime > this.transDuration) {
					this.isPlayingTransition = false;
					this.currentSection = this.newSection;
				}
				
			} else if (this.isShowingDialog) {
				if (this.hud.getDialog().isDone()) {
					this.isShowingDialog = false;
					this.hud.stopDialog();
				}
			}
		} else if (this.hasWon) {
			
			if (System.currentTimeMillis() - this.winTime >= this.winDuration) {
				this.hud.fadeOut();
				this.hasWon = false;
				this.startingCredits = true;
			}
			
		}
		
		this.hud.update(input);
		
	}

	/*
	Renders components on the window
	*/
	public void render (GameContainer container, Graphics g) {
		
		if (!this.hud.showingMainMenu()) {

			// the factor to scale the map to
			int factor = (int) (this.windowW / (double)this.w);
			g.scale(factor, factor);
			
			if (!isPlayingTransition) {
				
				// the current section
				int[] section = this.sections.get(this.currentSection);
				
				// the x, y, w, and h or the section
				int sectionX = section[0];
				int sectionY = section[1];
				int sectionW = section[2];
				int sectionH = section[3];
				
				// gets the amount to move the screen over to center on the player
				this.cameraX = (int)((this.player.getX() + this.player.getW() / 2) - (this.w / 2));
				this.cameraY = (int)((this.player.getY() + this.player.getH() / 2) - (this.w / 2));
				
				// checks that the camera has not gone too far left/right
				if (this.cameraX < sectionX) {
					this.cameraX = sectionX;
					
				} else if (this.cameraX - sectionX + this.w > sectionW) {
					this.cameraX = sectionX + (sectionW - this.w);
				}
				
				// checks the camera has not gone too far up/down
				if (this.cameraY < sectionY) {
					this.cameraY = sectionY;
				} else if (this.cameraY + this.h - sectionY > sectionH) {
					this.cameraY = sectionY + sectionH - this.h;
				}
				
				// moves the camera over to center the player
				g.translate(
					- this.cameraX, 
					- this.cameraY
				);
				
			} else {
				
				// the percentage of the transition that is done
				double percent = (System.currentTimeMillis() - this.transStartTime) / (double)this.transDuration;
				
				// gets the camera position
				int camX = (int) (this.camStartX + (this.camEndX - this.camStartX) * percent);
				int camY = (int) (this.camStartY + (this.camEndY - this.camStartY) * percent);
				
				// moves the camera
				g.translate(-camX, -camY);
				
			}
			
			if (!this.playerIsDying) {

				
				// draws the map
				this.map.render(0, 0);
				
				if (!this.isPlayingTransition) {

					
					// draws any objects
					for (int i = 0; i < this.objects.size(); i++) {
						this.objects.get(i).render(g);
					}
					
					// renders animations
					
					for (int i = 0; i < this.animations.size(); i++) {
						
						this.animations.get(i).render(g);
					}
					
					// renders consumables
					for (int i = 0; i < this.consumables.size(); i++) {
						
						this.consumables.get(i).render(g);
					
					}

					// renders the player
					this.player.render(g);
					
					// renders enemies
					for (int i = 0; i < this.enemies.size(); i++){
						this.enemies.get(i).render(g);;
					}
				}
				
				// draws the hud
				this.hud.render(g);
				
			} else {

				g.setColor(Color.red);
				
				g.fillRect(this.cameraX, this.cameraY, this.w, this.h);
				
				this.player.render(g);
				
				this.hud.render(g);
				
			}
		} else {
			
			this.hud.render(g);
		}

	}

	/*
	 * Loads all the objects in the map
	 * 
	 */
	private void loadObjects() {
		
		// initializes sections
		this.sections = new ArrayList<int[]>();
		
		// initializes objects
		this.objects = new ArrayList<StaticEntity>();
		
		// initializes enemies
		this.enemies = new ArrayList<EnemyEntity>();
		
		// initializes animations
		this.animations = new ArrayList<AutomatedEntity>();
		
		// initializes
		this.consumables = new ArrayList<CollisionEntity>();
		
		for (int gi = 0; gi < this.map.getObjectGroupCount(); gi++) {
			
			for (int oi = 0; oi < this.map.getObjectCount(gi); oi++) {
				
				// gets the x and y for the object
				int objX = this.map.getObjectX(gi, oi);
				int objY = this.map.getObjectY(gi, oi);
				
				// creates a new object
				switch (this.map.getObjectType(gi, oi)) {
				
					case "button": 
						this.objects.add(new Button(objX, objY, this)); 
						break;
						
					case "door": 
						
						boolean isNewMap = Boolean.parseBoolean(this.map.getObjectProperty(gi, oi, "toNewMap", "false"));
						String mapName = "";
						
						if (isNewMap) {
							mapName = this.map.getObjectProperty(gi, oi, "newMap", null) + ".tmx";
						}
						
						this.objects.add(
							new Door(
									objX, 
									objY, 
									this.map.getObjectWidth(gi, oi), 
									this.map.getObjectHeight(gi, oi), 
									Integer.parseInt(this.map.getObjectProperty(gi, oi, "pathId", null)), 
									Integer.parseInt(this.map.getObjectProperty(gi, oi, "direction", "0")),
									isNewMap,
									mapName,
									this
							)
						); 
						break;
						
					case "npc": 
						this.objects.add(
							new NPC(
								objX, 
								objY,
								this.map.getObjectProperty(gi, oi, "spritesheet", "test"),
								this.map.getObjectProperty(gi,  oi, "dialog", "test"),
								this));
						break;
						
					case "cutsceneanimation": 
						this.animations.add(new AutomatedEntity(
							this.map.getObjectProperty(gi,  oi, "src", "test"),
							this)
						);
						break;
						
					case "section": 
						addSection(gi, oi); 
						break;
						
					case "soldier":
						
						// gets the patrol routes
						int patrolX = Integer.parseInt(
								this.map.getObjectProperty(gi,  oi, "patrolx", "0")
						);
						int patrolY = Integer.parseInt(
								this.map.getObjectProperty(gi,  oi, "patroly", "0")
						);
						
						this.enemies.add(new Soldier(
							objX,
							objY,
							patrolX,
							patrolY,
							Integer.parseInt(
								this.map.getObjectProperty(gi,  oi, "difficulty", "0")),
							this
						));
						break;
					
					case "pot":
						this.objects.add(new Pot(objX, objY, this));
						break;
						
					case "player":
						this.player = new Player(objX, objY, this);
						break;
						
					case "macesoldier":
						this.enemies.add(new MaceSoldier(objX, objY, this));
						break;
						
					case "spritechanger":
						
						String spriteOne = this.map.getObjectProperty(gi, oi, "spriteOne", null);
						String spriteTwo = this.map.getObjectProperty(gi, oi, "spriteTwo", null);
						
						this.objects.add(new SpriteChanger(
							objX,
							objY, 
							spriteOne,
							spriteTwo,
							this));
						
						break;
						
					default : 
						System.out.println(this.map.getObjectType(gi, oi)); 
						break;
				
				}
			}
			
		}
	}

	/*
	 * Loads the .tmx file into a slick.TiledMap Object
	 * and sets up Game's blocked value, which is used
	 * for collision detection
	 */
	private void loadMap(String mapUrl) {
		
		// creates the TiledMap
		try {
			// loads the test map
			this.map = new TiledMap(mapUrl);
			
		} catch (SlickException e) {
			// prints error message
			e.printStackTrace();
			System.err.println("Couild not load map");
			System.exit(0);
		}
		
		// loads the music
		String musicPath = this.map.getMapProperty("music", "overworld");
		SoundStore.get().setMusic("assets/music/wav/" + musicPath + ".wav");
		
		blocked = new boolean[this.map.getWidth()][this.map.getHeight()];
		
		for (int x = 0; x < blocked.length; x++) {
			for (int y = 0; y < blocked[x].length; y++) {
				
				int tileID = this.map.getTileId(x, y, this.map.getLayerIndex("Tile Layer"));
				
				String value = this.map.getTileProperty(tileID, "blocked", "false");
				
				if (value.equals("true")) {
					
					blocked[x][y] = true;
				}
				
			}
		}
		
	}

	/*
	 * Starts a new game
	 */
	public void startGame() {

		// prevents anything from happening while the game is loading
		this.isPaused = true;
		
		// sets the current map to the first one
		this.currentMap = "castleone.tmx";
		
		// loads the map
		this.loadMap("assets/maps/" + this.currentMap);
	
		// loads the objects
		this.loadObjects();
		
		// gets the current section
		this.currentSection = this.getNewSection();
		
		// unpauses
		this.isPaused = false;
	}
	
	/*
	 * Shows the end credits
	 */
	public void onWin() {
		
		this.isPaused = true;
		SoundStore.get().stopMusic();
		SoundStore.get().getSound("assets/music/wav/victory.wav").play();
		this.player.onWin();
		
		this.winTime = System.currentTimeMillis();
		this.hasWon = true;
		
	}
	/*
	 * Starts a transition to the next section
	 */
	public void startTransition(int pathId, Door startingDoor) {
		
		// searches through objects and find the doors
		for (int i = 0; i < this.objects.size(); i++) {
			Entity o = this.objects.get(i);
			
			if (o instanceof Door) {
				
				// casts to door
				Door door = (Door)o;
				
				// checks if it is the other door
				if (door.getPathID() == pathId && door != startingDoor) {
					
					// moves the player to the door's position
					int[] pos = door.getExitPos(this.player.getW(), this.player.getH());
					this.player.setX(pos[0]);
					this.player.setY(pos[1]);
					
					// changes the section to match the player's new coordinates
					this.newSection = getNewSection();
					
					// saves the new coordinates
					this.camStartX = this.cameraX;
					this.camStartY = this.cameraY;
					
					// finds the coordinates needed to focus the Camera on the door
					setCameraPositionForDoor(door);
					
					// sets to play the transition
					this.isPlayingTransition = true;
					this.transStartTime = System.currentTimeMillis();
					break;
					
				}
				
			}
		}
		
	}
	
	/*
	 * Loads a new map after the player walks through a door leading to a new area
	 */
	public void moveToMap(String mapName, int pathId) {
	
		this.currentMap = mapName;
		this.newPathId = pathId;
		
		this.hud.fadeOut();
		this.isPaused = true;
	}
	
	/*
	 * Fades the screen in or out
	 */
	public void onFinishedFadeIn() {
		if (!this.startingCredits) {
			this.isPaused = false;
		}
	}
	public void onFinishedFadeOut() {
		
		if (this.playingCredits) {
			
			this.playingCredits = false;
			this.startingCredits = false;

			this.startGame();
			this.hud.fadeIn();
			
		} else if (this.startingCredits) {
			
			this.isPaused = true;
			this.playingCredits = true;
			this.hud.startCredits();
			this.hud.fadeIn();
			
		} else {

			// loads the map
			this.loadMap("assets/maps/" + this.currentMap);
			this.loadObjects();
			
			this.spawnPlayerAtDoor(this.newPathId);
			
			this.hud.fadeIn();
		}
	}
	
	/*
	 * Spawns the player at a door with a certain path ID
	 */
	public void spawnPlayerAtDoor(int pathId) {

		
		// creates player with 0 position to start
		this.player = new Player(0, 0, this);
		
		// sets player position
		for (int i = 0; i < this.objects.size(); i++) {
			
			if (this.objects.get(i) instanceof Door) {
				
				Door d = (Door) this.objects.get(i);
				
				if (d.getPathID() == pathId) {
					
					int[] exitPos = d.getExitPos(this.player.getW(), this.player.getH());
					
					this.player.setX(exitPos[0]);
					this.player.setY(exitPos[1]);
					this.player.setDirection(d.getDirection());
					this.player.update(new Input(0), 0);
					
					this.currentSection = this.getNewSection();
				}
				
			}
			
		}
	}
	
	/*
	 * Starts a dialog with the dialog lines provided
	 */
	public void startDialog(String dialog) {
		this.hud.startDialog(dialog);
		this.isShowingDialog = true;
	}
	/*
	 * Used in the transition animation
	 * Sets camEndX and camEndY (the ending camera position) to where the player will come out of the door
	 */
	private void setCameraPositionForDoor(Door d) {
		
		// Basically sets up the camera so that the door is exactly on the edge facing in
		
		// initially sets the camera to end centered on the door
		this.camEndX = (int) ((d.getX() + d.getW() / 2) - this.w / 2);
		this.camEndY = (int) ((d.getY() + d.getH() / 2) - this.h / 2);
		
		switch (d.getDirection()) {
			
			case Entity.DIR_UP:
				
				// the door is facing up, so it is at the bottom of the screen
				this.camEndY = (int) (d.getY() + d.getH() - this.h);
				break;
				
			case Entity.DIR_DOWN:
				
				// the door is facing down, so it is at the top of the screen
				this.camEndY = (int) d.getY();
				break;
				
			case Entity.DIR_LEFT:
				
				// the door is facing left, so it needs to be at the right
				this.camEndX = (int) (d.getX() + d.getW() - this.w);
				break;
				
			case Entity.DIR_RIGHT:
				
				// the door is facing right, so it needs to be on the left
				this.camEndX = (int) d.getX();
				break;
		}
		
	}
	
	/*
	 * Changes the active section to match the player's coordinates
	 */
	private int getNewSection() {
		
		// finds the section the player is in
		for (int i = 0; i < this.sections.size(); i++) {
			int[] s = sections.get(i);
			if (player.getX() < s[0] + s[2]) {
				if (player.getX() + player.getW() > s[0]) {
					if (player.getY() < s[1] + s[3]) {
						if (player.getY() + player.getH() > s[1]) {
							return i;
						}
					}
				}
			}
		}
		
		return -1;
		
	}
	
	/*
	 * Adds a section to the sections ArrayList
	 */
	private void addSection(int gi, int oi)
	{
		
		int[] section = {
			this.map.getObjectX(gi, oi),
			this.map.getObjectY(gi, oi),
			this.map.getObjectWidth(gi, oi),
			this.map.getObjectHeight(gi, oi)
		};
		
		this.sections.add(section);
		
	}
	
	/*
	 * Removes an enemy when it dies
	 */
	public void removeEnemy(EnemyEntity e) {
		
		this.enemies.remove(e);
		
	}
	
	/*
	 * Returns the contents of a file
	 */
	public static String readFile(String filename) {
		
		// the string to return
		String toReturn = "";
		
		// the scanner used to read the file
		Scanner s;
		try {
			// creates a new scanner from the file
			s = new Scanner(new File(
				String.format(filename)
			));
			
		} catch (FileNotFoundException e) {
			
			// prints error message and exits
			e.printStackTrace();
			System.exit(0);
			return "";
		}
		
		// adds lines to toReturn
		while (s.hasNextLine()) {
			toReturn += s.nextLine() + "\n";
		}
		
		// closes the scanner
		s.close();
		
		return toReturn;
	}
	
	
	/*
	 * Checks whether a position is blocked based on a 2D array
	 * 
	 */
	public boolean isBlocked(float x, float y, int w, int h) {
		
		// checks the coordinate is not off the map
		if (x <= 0 || y <= 0) {
			return true;
		}
		
		// converts to integers, because pixel-specific is good enough
		int intX = (int) Math.ceil(x);
		int intY = (int) Math.ceil(y);
		
		// the x, y coordinates of the corners of the entity
		int[][] corners = {
			{
				intX,
				intY
			},
			{
				intX + w,
				intY
			},
			{
				intX + w,
				intY + h
			},
			{
				intX,
				intY + h
			}
		};
		
		// checks if each corner is not hitting anything
		for (int i = 0; i < corners.length; i++) {
			
			// gets the index in the 2D array where the point would be
			int indexX = (int) Math.floor(corners[i][0] / (double)(this.map.getTileWidth()));
			int indexY = (int) Math.floor(corners[i][1] / (double)(this.map.getTileHeight()));
			
			// checks if it hits anything
			try {

				if (this.blocked[indexX][indexY]) {
					return true;
				}
			} catch (Exception e) {
				
				System.out.println("Oh no!");
				System.out.format("Index: %d \n", i);
				System.out.format("indexX: %d, indexY: %d \n", indexX, indexY);
				System.out.format("intX: %d, intY: %d \n", intX, intY);
				System.out.format("X: %f, Y: %f, W: %d, h: %d \n", x, y, w, h);
				System.exit(0);
			}
			
		}
		return false;
	}

	/*
	 * Returns whether to draw hitboxes and such when in debug mode
	 */
	public boolean isDebug() {
		return false;
	}
	
	/*
	 * Called when the player dies
	 * Sets up to play the player's death animation
	 * Ex: Sets up the background
	 * 
	 */
	public void startPlayerDeath() {
		
		this.playerIsDying = true;
		SoundStore.get().stopMusic();
	}
	
	/*
	 * Called when the player's death animation has finished
	 * and the user can press space to respawn
	 */
	public void onPlayerDeath() {
		
		this.playerIsDead = true;
		this.hud.showGameOver();
		
	}
	
	/*
	 * Figures out which section a point is in
	 */
	public int getSectionForPoint(int x, int y, int w, int h) {
		
		for (int i = 0; i < this.sections.size(); i++) {
		
			int[] s = sections.get(i);
			
			if (s[0] <= x) {
				if (s[0] + s[2] >= x + w) { 
					if (s[1] <= y) {
						if (s[1] + s[3] >= y + h) {
							return i;
						}
					}
				}
			}
			
		}
		
		System.out.println("Entity did not belong to a section");
		System.out.format("X: %d, Y: %d \n", x, y);
		System.exit(0);
		
		return 0;
		
	}
	
	/*
	 * Adds a consumable object
	 */
	public void addConsumable(CollisionEntity e) {
		
		this.consumables.add(e);
	}
	
	/*
	 * Removes a consumable object from the game
	 */
	public void removeConsumable(CollisionEntity e) {
		
		for (int i = 0; i < this.consumables.size(); i++) {
			
			if (consumables.get(i) == e) {
				consumables.remove(i);
				break;
			}
			
		}
		
	}
	
	/*
	 * Get methods
	 */
	public Player getPlayer() {
		return this.player;
	}
	public ArrayList<InteractiveEntity> getInteractiveObjects() {
		
		ArrayList<InteractiveEntity> toReturn = new ArrayList<InteractiveEntity>();
		
		for (int i = 0; i < this.objects.size(); i++) {
			StaticEntity obj = this.objects.get(i);
			
			if (obj instanceof InteractiveEntity) {
				toReturn.add((InteractiveEntity)obj);
			}
		}
		
		return toReturn;
		
	}
	public StaticEntity[] getObjects() {
		
		return this.objects.toArray(new StaticEntity[this.objects.size()]);
		
	}
	public EnemyEntity[] getEnemies() {
		
		return this.enemies.toArray(new EnemyEntity[this.enemies.size()]);
	}
	
	/*
	Starts the game
	*/
	public static void main(String[] args)
	{


        try
        {
        	// sets width and height
        	int size = 800;
        	
        	Game.fixMaps();
        	
        	// creates the window
            AppGameContainer app = new AppGameContainer(new Game(size, size));
            
            // sets the window dimensions
            app.setDisplayMode(size, size, false);
            
            // hides FPS
            app.setShowFPS(false);
            
            // starts the game
            app.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }
	}
	
	public static void fixMaps() {
		
		File folder = new File("assets/maps");
		
		File[] maps = folder.listFiles();
		
		for (int i = 0; i < maps.length; i++) {
			if (maps[i].isFile()) {
				
				String contents = Game.readFile("assets/maps/" + maps[i].getName());

				
				contents = contents.replaceAll("<objectgroup (.*?) (width=.+ height=.+)+>", "<objectgroup $1>");
				contents = contents.replaceAll("<objectgroup (.*?)>", "<objectgroup $1 width=\"1\" height=\"1\">");
				
				Writer w = null;
				try {
					w = new PrintWriter(maps[i]);					
					w.write(contents);
					w.flush();
					
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
		
	}
}