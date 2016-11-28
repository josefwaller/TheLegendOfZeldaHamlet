package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;


// different entities
import entities.Button;
import entities.CutsceneAnimation;
import entities.Door;
import entities.NPC;

// The player Entity
import entities.Player;

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

	// all objects on the current map
	private ArrayList<StaticEntity> objects;
	
	// all entities (Except the player) on the screen
	private ArrayList<Entity> entities;
	
	// all animations that will be played
	private ArrayList<CutsceneAnimation> animations;
	
	// the lines of dialog currently being drawn on the screen
	
	// whether or not dialog is currently being shown on screen
	private boolean isShowingDialog;	
	
	// the Heads Up Display
	private HeadsUpDisplay hud;
	
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
		
		// loads the map
		loadMap("assets/maps/test.tmx");
		
		// the width one tile should be
		int finalTileWidth = (this.windowW / this.widthInTiles);
		
		// the conversion factor between the tiledmap tile width (16) and the final width
		double conversion = this.map.getTileWidth() / (double)finalTileWidth;
		
		// the width and height used for positioning
		this.w = (int) (this.windowW * conversion);
		this.h = (int) (this.windowH * conversion);
		
		// the hud
		this.hud = new HeadsUpDisplay(this.windowW, this.windowH);
	
		// loads the objects
		loadObjects();
	}
	
	/*
	 * Updates everything
	 * Position, game logic, etc
	 */
	public void update(GameContainer container, int delta) {

		// gets input
		Input input = container.getInput();
		if (!this.isPlayingTransition && !this.isShowingDialog) {
			
			// updates the player
			this.player.update(input, delta, this.blocked);
			
			// updates objects
			for (int o = 0; o < this.objects.size(); o++) {
				this.objects.get(o).update();
			}
			
			// updates animations
			for (int i = 0; i < this.animations.size(); i++) {
				this.animations.get(i).update();
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
		
		this.hud.update(input);
		
	}

	/*
	Renders components on the window
	*/
	public void render (GameContainer container, Graphics g) {
		
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
		
		// draws the map
		this.map.render(0, 0);
		
		if (!this.isPlayingTransition) {

			// draws any objects
			for (int i = 0; i < this.objects.size(); i++) {
				this.objects.get(i).render(g);
			}

			// renders the player
			this.player.render(g);
		}
		
		// draws the hud
		this.hud.render(g);

	}

	/*
	 * Loads all the objects in the map
	 * 
	 */
	private void loadObjects() {

		// creates a new player at 0, 0
		this.player = new Player(50, 50, this);
		
		// initializes sections
		this.sections = new ArrayList<int[]>();
		
		// initializes objects
		this.objects = new ArrayList<StaticEntity>();
		
		// initialsizes animations
		this.animations = new ArrayList<CutsceneAnimation>();
		
		for (int gi = 0; gi < this.map.getObjectGroupCount(); gi++) {
			
			for (int oi = 0; oi < this.map.getObjectCount(gi); oi++) {
				
				// gets the x and y for the object
				int objX = this.map.getObjectX(gi, oi);
				int objY = this.map.getObjectY(gi, oi);
				
				// creates a new object
				switch (this.map.getObjectType(gi, oi)) {
					case "button": this.objects.add(new Button(objX, objY, this)); break;
					case "door": 
						this.objects.add(
							new Door(
									objX, 
									objY, 
									Integer.parseInt(this.map.getObjectProperty(gi, oi, "pathId", null)), 
									Integer.parseInt(this.map.getObjectProperty(gi, oi, "direction", null)), 
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
						this.animations.add(new CutsceneAnimation(
							this.map.getObjectProperty(gi,  oi, "src", "test"),
							this)
						);
						break;
					case "section": addSection(gi, oi); break;
					
					default : System.out.println(this.map.getObjectType(gi, oi)); break;
				
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
			System.err.println("Couild not load test map");
			System.exit(0);
		}
		
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
				this.camEndX = (int) (d.getX() - this.w);
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
	 * Returns the contents of a file
	 */
	public static String readFile(String filename) {
		String toReturn = "";
		Scanner s;
		try {
			s = new Scanner(new File(
				String.format(filename)
			));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
			return "";
		}
		
		while (s.hasNextLine()) {
			toReturn += s.nextLine();
		}
		
		s.close();
		
		return toReturn;
	}
	
	
	/*
	 * Checks whether a position is blocked based on a 2D array
	 * 
	 */
	public boolean isBlocked(float x, float y, int w, int h)
	{
		// converts to integers, because pixel-specific is good enough
		int intX = Math.round(x);
		int intY = Math.round(y);
		
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
			if (this.blocked[indexX][indexY]) {
				return true;
			}
			
		}
		return false;
	}

	/*
	 * Returns whether to draw hitboxes and such when in debug mode
	 */
	public boolean isDebug() {
		return true;
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
	/*
	Starts the game
	*/
	public static void main(String[] args)
	{


        try
        {
        	// sets width and height
        	int size = 800;
        	
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
}