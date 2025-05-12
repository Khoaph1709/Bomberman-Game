package uet.oop.bomberman.entities.tile;

import javafx.scene.image.Image;
import static uet.oop.bomberman.BombermanGame.table;

public class Wall extends Tile {

    public Wall(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        table[getTileX()][getTileY()] = this; 
    }
}
