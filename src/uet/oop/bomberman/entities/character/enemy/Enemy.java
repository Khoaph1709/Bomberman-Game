package uet.oop.bomberman.entities.character.enemy;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public abstract class Enemy extends Entity {
    // protected Entity old_cur = null;
    protected final int MAX_ANIMATE = 7500;

    public Enemy(int x, int y, Image img) {
        super(x, y, img);
    }
}
