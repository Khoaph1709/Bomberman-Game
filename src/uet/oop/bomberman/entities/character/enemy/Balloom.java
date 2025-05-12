package uet.oop.bomberman.entities.character.enemy;

import javafx.scene.image.Image;
import static uet.oop.bomberman.BombermanGame.table;
import uet.oop.bomberman.graphics.Sprite;
public class Balloom extends Enemy {
    private static final int STEP = Sprite.STEP;
    private boolean moving;


    public Balloom(int x, int y, Image img) {
        super(x, y, img);
    }

    private void balloomMoving() {
        int px = getTileX();
        int py = getTileY();

        table[px][py] = null;
        sprite = Sprite.balloom_right1;

        if (hurt) {
            img = Sprite.movingSprite(Sprite.balloom_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, 20).getFxImage();
            return;
        }
}
