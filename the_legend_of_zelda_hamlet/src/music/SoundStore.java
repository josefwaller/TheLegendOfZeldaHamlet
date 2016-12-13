package music;

import java.util.HashMap;

import org.newdawn.slick.Music;
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
	
	// musics that have been loaded so far
	private HashMap<String, Music> musics;
	
	// the music currently playing
	private Music music;
	
	/*
	 * Creates a new SoundStore
	 */
	public SoundStore() {
		
		this.sounds = new HashMap<String, Sound>();
		this.musics = new HashMap<String, Music>();
	}
	
	/*
	 * Returns a refernce to the sound in an wav file
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
	 * Loads a music .wav file
	 */
	public Music getMusic(String path) {
		
		if (!this.musics.containsKey(path)) {
			
			try {
				this.musics.put(path, new Music(path));
				
			} catch (SlickException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		return this.musics.get(path);
	}
	
	/*
	 * Sets the music to a .mp3 file
	 */
	public void setMusic (String path) {
		
		if (this.music != null) {
			this.music.stop();
		}
		this.music = this.getMusic(path);
		
		this.music.loop();
		
	}
	
	public void fadeMusic(int duration, float volume, boolean stop) {
		
		if (this.music != null) {

			this.music.fade(duration, volume, stop);
		}
	}
	
	/*
	 * Stops the music
	 */
	public void stopMusic() {
	
		if (this.music != null) {
			this.music.stop();
			
		}
		
	}
	
	/*
	 * Returns the one instance of the sound store
	 */
	public static SoundStore get() {
		return ss;
	}
	
}
