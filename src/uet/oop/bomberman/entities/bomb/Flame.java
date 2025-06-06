package uet.oop.bomberman.entities.bomb;

import java.util.List;

import javafx.application.Platform;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class Flame extends Entity {
    private final Direction direction;
    private final List<Entity> entities;
    private int animate = 0;

    public Flame(int x, int y, Image img, Direction direction, List<Entity> entities) {
        super(x, y, img);
        this.entities = entities;
        this.direction = direction;
    }

    public void getImg() {
        Sprite sprite = null;
        switch (direction) {
            case U:
                sprite = Sprite.movingSprite(Sprite.explosion_vertical_top_last, Sprite.explosion_vertical_top_last1, Sprite.explosion_vertical_top_last2, animate, 20);
                break;
            case D:
                sprite = Sprite.movingSprite(Sprite.explosion_vertical_down_last, Sprite.explosion_vertical_down_last1, Sprite.explosion_vertical_down_last2, animate, 20);
                break;
            case L:
                sprite = Sprite.movingSprite(Sprite.explosion_horizontal_left_last, Sprite.explosion_horizontal_left_last1, Sprite.explosion_horizontal_left_last2, animate, 20);
                break;
            case R:
                sprite = Sprite.movingSprite(Sprite.explosion_horizontal_right_last, Sprite.explosion_horizontal_right_last1, Sprite.explosion_horizontal_right_last2, animate, 20);
                break;
            case OH:
                sprite = Sprite.movingSprite(Sprite.explosion_horizontal, Sprite.explosion_horizontal1, Sprite.explosion_horizontal2, animate, 20);
                break;
            case OV:
                sprite = Sprite.movingSprite(Sprite.explosion_vertical, Sprite.explosion_vertical1, Sprite.explosion_vertical2, animate, 20);
                break;
        }
        img = sprite.getFxImage();
    }

    @Override
    public void update() {
        animate++;
        if (animate == 10) {
            Platform.runLater(() -> entities.remove(this));
        }
        getImg();
    }
}
