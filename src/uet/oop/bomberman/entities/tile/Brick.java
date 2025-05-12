package uet.oop.bomberman.entities.tile;

import javafx.application.Platform;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import static uet.oop.bomberman.BombermanGame.entities;
import static uet.oop.bomberman.BombermanGame.table;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;


public class Brick extends Tile {
    private boolean exploded = false;

    // Constructor for the Brick class
    public Brick(int x, int y, Image img) {
        super(x, y, img);
    }

    public void brickExploded() {
        sprite = Sprite.movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1, Sprite.brick_exploded2, animate++, 20);
        img = sprite.getFxImage();

        if (animate == 20) {
            Platform.runLater(() -> {
                entities.remove(this);
                x = getTileX();
                y = getTileY();
                BombermanGame.table[x][y] = null;
                Entity hiddenItem = BombermanGame.hiddenTable[x][y];
                if (hiddenItem != null) {
                    BombermanGame.table[x][y] = hiddenItem;
                    entities.add(hiddenItem);
                }
            });
        }
    }

    public void setExploaded() {
        this.exploded = true;
    }
    

    @Override
    public void update() {
        table[getTileX()][getTileY()] = this;
        if (exploded) {
            brickExploded();
        }
    }
}
