package music;

import java.util.HashMap;

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
	 * Loads a .mp3 file and returns a reference to it
	 */
	public Sound loadMP3 (String path) {
		
		return null;
	}
	
	/*
	 * Sets the music to a .mp3 file
	 */
	public void setMusic (String path) {
		
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
