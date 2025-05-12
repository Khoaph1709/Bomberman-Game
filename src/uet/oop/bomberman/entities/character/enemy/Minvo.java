package uet.oop.bomberman.entities.character.enemy;

import java.util.Random;

import javafx.scene.image.Image;
import static uet.oop.bomberman.BombermanGame.table;
import uet.oop.bomberman.algorithm.FindPath;
import uet.oop.bomberman.graphics.Sprite;

public class Minvo extends Enemy {
    public Minvo(int x, int y, Image img) {
        super(x, y, img);
    }
}