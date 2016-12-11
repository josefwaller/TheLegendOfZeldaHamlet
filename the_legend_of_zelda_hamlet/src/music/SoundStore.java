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
	
	/*
	 * Creates a new SoundStore
	 */
	public SoundStore() {
		
		
		
	}
	
	/*
	 * Returns a refernce to the sound in an mp3 file
	 * Loads if only if not yet loaded
	 */
	public Sound getMP3 (String path) {
		
		try {
			return new Sound(path);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/*
	 * Sets the music to a .mp3 file
	 */
	public void setMusic (String path) {
		
		Sound music = this.getMP3(path);
		
		music.loop();
		
	}
	
	/*
	 * Stops the music
	 */
	public void stopMusic() {
		
	}
	
	/*
	 * Returns the one instance of the sound store
	 */
	public static SoundStore get() {
		return ss;
	}
	
}
