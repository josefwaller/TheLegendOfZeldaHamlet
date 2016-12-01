package entities.abstracts;

import game.Game;

public abstract class EnemyEntity extends AnimatedEntity {

	public EnemyEntity(int x, int y, int w, int h, Game g) {
		super(x, y, w, h, g);
	}

	public EnemyEntity(int x, int y, int s, Game g) {
		super(x, y, s, g);
	}

	// enemies must be able to update
	public abstract void update(int delta);
	
}
