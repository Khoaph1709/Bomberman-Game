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
        
        switch (direction) {
            case D:
                if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 1) && checkWall(x + Sprite.SCALED_SIZE - 1, y + STEP + Sprite.SCALED_SIZE - 1)) {
                    y += STEP;
                    moving = true;
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, animate, 20);
                    }
                }
                break;
            case U:
                if (checkWall(x, y - STEP) && checkWall(x + Sprite.SCALED_SIZE - 1, y - STEP)) {
                    y -= STEP;
                    moving = true;
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, animate, 20);
                    }
                }
                break;
            case L:
                if (checkWall(x - STEP, y) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 1)) {
                    x -= STEP;
                    moving = true;
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, animate, 20);
                    }
                }
                break;
            case R:
                if (checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y) && checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
                    x += STEP;
                    moving = true;
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, animate, 20);
                    }
                }
                break;
            default:
                break;
        }
        
        img = sprite.getFxImage();
        int new_px = getTileX();
        int new_py = getTileY();
        table[new_px][new_py] = this;
    }

    @Override
    public void update() {
        if (hurt) {
            gotHurt(Sprite.balloom_dead);
            return;
        }
        animate++;  
        moving = false;
        findDirection();
        balloomMoving();
        checkCollideWithBomber();
    }
}
