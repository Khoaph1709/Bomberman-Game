package uet.oop.bomberman.entities.items;

import javafx.application.Platform;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import static uet.oop.bomberman.BombermanGame.table;
import uet.oop.bomberman.graphics.Sprite;


public class BombItem extends Item {
    public BombItem(int x, int y, Image img) {
        super(x, y, img);
    }
    

    @Override
    public void update() {
        img = Sprite.powerup_bombs.getFxImage();
        table[x / Sprite.SCALED_SIZE][y / Sprite.SCALED_SIZE] = this;
        if (pickUp || died) {
            Platform.runLater(() -> {
                table[x / Sprite.SCALED_SIZE][y / Sprite.SCALED_SIZE] = null;
                BombermanGame.entities.remove(this);
            });
        }
    }
    
}
