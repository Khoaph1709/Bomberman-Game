package uet.oop.bomberman.entities.tile;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;


public class Tile extends Entity {
    @Override
    public int getTileX() {
        return x / Sprite.SCALED_SIZE;
    }

    @Override
    public int getTileY() {
        return y / Sprite.SCALED_SIZE;
    }  

    public Tile(int x, int y, Image img) {
        super(x, y, img); 
    }

    @Override
    public void update() {};
}
