package uet.oop.bomberman.entities.character.enemy;
import javafx.application.Platform;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import static uet.oop.bomberman.BombermanGame.enemies;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;

public abstract class Enemy extends Entity {
    // protected Entity old_cur = null;
    protected final int MAX_ANIMATE = 7500;

    public Enemy(int x, int y, Image img) {
        super(x, y, img);
    }
}
