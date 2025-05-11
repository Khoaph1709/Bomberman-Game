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

    protected void gotHurt(Sprite sprite) {
        hurt_time++;
        if (hurt_time == 1) {
            Sound.died.play();
        }
        img = sprite.getFxImage();
        if (hurt_time == 20) {
            hurt_time = 0;
            hurt = false;
            if (life == 0) {
                Platform.runLater(() -> {
                    enemies.remove(this);
                    x = getTileX();
                    y = getTileY();
                    BombermanGame.table[getTileX()][getTileY()] = null;
                });
            }
        }
        this.sprite = Sprite.movingSprite(sprite, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, 20);
        img = sprite.getFxImage();
        
        // if (animate == 60) {
        //     Platform.runLater(() -> {
        //         enemies.remove(this);
        //         x = getTileX();
        //         y = getTileY();
        //         BombermanGame.table[x][y] = null;
        //     });
        // }
    }
}
