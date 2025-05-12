package uet.oop.bomberman.entities.character.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

public class Doria extends Enemy {
    private static final int STEP = Sprite.STEP / 2;
    private boolean moving = false;

    public Doria(int x, int y, Image img) {
        super(x, y, img);
    }

    private void doriaMoving() {
        int px = getTileX();
        int py = getTileY();

        BombermanGame.table[px][py] = null;
        sprite = Sprite.kondoria_right1;

        if (hurt) {
            img = Sprite.movingSprite(Sprite.kondoria_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, 20).getFxImage();
            return; 
        }
}
