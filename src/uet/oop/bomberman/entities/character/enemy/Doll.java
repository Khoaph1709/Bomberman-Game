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

        switch (direction) {
            case D:
                if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 1) && checkWall(x + Sprite.SCALED_SIZE - 1, y + STEP + Sprite.SCALED_SIZE - 1)) {
                    y += STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, animate, 20);
                }
                break;
            case U:
                sprite = Sprite.doll_left2;
                if (checkWall(x, y - STEP) && checkWall(x + Sprite.SCALED_SIZE - 1, y - STEP)) {
                    y -= STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, animate, 20);
                }
                break;
            case L:
                sprite = Sprite.doll_left1;
                if (checkWall(x - STEP, y) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 1)) {
                    x -= STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, animate, 20);
                }
                break;
            case R:
                if (checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y) && checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
                    x += STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, animate, 20);
                }
                break;
            default:
                break;
        }
        img = sprite.getFxImage();
        int newPx = getTileX();
        int newPy = getTileY();
        table[newPx][newPy] = this;
    }

    
    public void setEnemyDie() {
        this.life = 0;
        gotHurt(Sprite.oneal_dead);
        this.died = true;
    }


    @Override
    public void update() {
        if (hurt) {
            gotHurt(Sprite.doll_dead);
            return;
        }
        animate++;
        moving = false;
        findDirection();
        dollMoving();
        checkCollideWithBomber();
    }
}
