package uet.oop.bomberman.entities.items;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import static uet.oop.bomberman.BombermanGame.table;
import uet.oop.bomberman.graphics.Sprite;
import javafx.application.Platform;


public class FlamePassItem extends Item {
    public FlamePassItem(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        img = Sprite.powerup_flamepass.getFxImage();
        table[x / Sprite.SCALED_SIZE][y / Sprite.SCALED_SIZE] = this;
        if (pickUp || died) {
            Platform.runLater(() -> {
                table[x / Sprite.SCALED_SIZE][y / Sprite.SCALED_SIZE] = null;
                BombermanGame.entities.remove(this);
            });
        }
    }
}
