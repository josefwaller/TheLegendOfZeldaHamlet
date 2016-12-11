package music;

import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/*
 * Stores all music and sound and plays them when needed
 */
public class SoundStore {

	// The one instance of the SoundStore
	private static SoundStore ss = new SoundStore();
	
	// the sounds that have been loaded so far
	private HashMap<String, Sound> sounds;
	
	// the music currently playing
	private Sound music;
	
	/*
	 * Creates a new SoundStore
	 */
	public SoundStore() {
		
		this.sounds = new HashMap<String, Sound>();
		
	}
	
	/*
	 * Returns a refernce to the sound in an mp3 file
	 * Loads if only if not yet loaded
	 */
	public Sound getSound (String path) {
		
		if (!this.sounds.containsKey(path)) {

			
			try {
				this.sounds.put(path, new Sound(path));
				
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		
		return this.sounds.get(path);
	}
	
	/*
	 * Sets the music to a .mp3 file
	 */
	public void setMusic (String path) {
		
		this.music = this.getSound(path);
		
		this.music.loop();
		
	}
	
	/*
	 * Stops the music
	 */
	public void stopMusic() {
	
		this.music.stop();
		
	}
	
	/*
	 * Returns the one instance of the sound store
	 */
	public static SoundStore get() {
		return ss;
	}
	
}
