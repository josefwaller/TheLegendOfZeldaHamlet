package sprites;

import java.util.HashMap;

public class AnimationStore {

	private HashMap<String, HashMap<String, SpriteAnimation>> animations;
	
	private static AnimationStore animStore;
	
	public AnimationStore() {
		
		this.animations = new HashMap<String, HashMap<String, SpriteAnimation>>();
		
	}
	
	public void loadAnimationsForSheet() {
		
	}
	
	public SpriteAnimation getAnimation(String sheetName, String animName) {
		
		return null;
	}
	
	public AnimationStore get() {
		return AnimationStore.animStore;
	}
	
}
