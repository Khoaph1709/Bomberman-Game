package uet.oop.bomberman.entities.items;

import javafx.scene.image.Image;
import static uet.oop.bomberman.BombermanGame.table;
import uet.oop.bomberman.graphics.Sprite;

public class Portal extends Item {
    public Portal(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        table[x / Sprite.SCALED_SIZE][y / Sprite.SCALED_SIZE] = this;
        img = Sprite.portal.getFxImage();
    }
}
