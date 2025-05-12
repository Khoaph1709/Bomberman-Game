package uet.oop.bomberman.entities.character.enemy;

import javafx.scene.image.Image;
import static uet.oop.bomberman.BombermanGame.table;
import uet.oop.bomberman.graphics.Sprite;

public class Doll extends Enemy {
    private static final int STEP = Sprite.STEP / 2;
    private boolean moving = false;

    public Doll(int x, int y, Image img) {
        super(x, y, img);
    }

    private void dollMoving() {
        int px = getTileX();
        int py = getTileY();
        
        table[px][py] = null;
        sprite = Sprite.doll_right1;

        if (hurt) {
            img = Sprite.movingSprite(Sprite.doll_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, 20).getFxImage();
            // Sound.playSoundEffect("cut_ra_ngoai");
            return; 
        }

    public void setEnemyDie() {
        this.life = 0;
        gotHurt(Sprite.oneal_dead);
        this.died = true;
    }
}
