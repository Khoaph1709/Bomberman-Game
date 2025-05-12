package uet.oop.bomberman.entities.character.enemy;

import java.util.Random;

import javafx.scene.image.Image;
import static uet.oop.bomberman.BombermanGame.table;
import uet.oop.bomberman.algorithm.FindPath;
import uet.oop.bomberman.graphics.Sprite;

public class Oneal extends Enemy {
    public Oneal(int x, int y, Image img) {
        super(x, y, img);
    }

    private static final int STEP = Sprite.STEP / 2;
    private static final int MIN_SPEED = 1;
    private static final int MAX_SPEED = 3;
    private boolean moving = false;
    private boolean canReach = false;
    private int currentSpeed = MIN_SPEED;

    @Override
    protected void findDirection() {
        animate++;
        if (animate > MAX_ANIMATE) {
            animate = 0;
        }
        if (x % Sprite.SCALED_SIZE == 0 && y % Sprite.SCALED_SIZE == 0) {
           Direction nDirection = FindPath.bfs(getTileX(), getTileY());
           if (nDirection != null) {
                canReach = true;
                direction = nDirection;
           } 
           moving = false;
        }
        if (animate % 70 == 0 && x % Sprite.SCALED_SIZE == 0 && y % Sprite.SCALED_SIZE == 0 && !canReach) {
            direction = Direction.values()[new Random().nextInt(Direction.values().length)];
            moving = false;
        }
    }

    private void onealMoving() {
        int px = getTileX();
        int py = getTileY();
        table[px][py] = null;
        sprite = Sprite.oneal_right1;
        
        if (hurt) {
            img = Sprite.movingSprite(Sprite.oneal_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, 20).getFxImage();
            return;
        }
        switch (direction) {
            case D:
                if (checkWall(x, y + STEP * currentSpeed + Sprite.SCALED_SIZE - 1) && checkWall(x + Sprite.SCALED_SIZE - 1, y + STEP * currentSpeed + Sprite.SCALED_SIZE - 1)) {
                    y += STEP * currentSpeed;
                    moving = true;
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, animate, 20);
                    }
                }
                break;
            case U:
                if (checkWall(x, y - STEP * currentSpeed) && checkWall(x + Sprite.SCALED_SIZE - 1, y - STEP * currentSpeed)) {
                    y -= STEP * currentSpeed;
                    moving = true;
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, animate, 20);
                    }
                }
                break;
            case L:
                if (checkWall(x - STEP * currentSpeed, y) && checkWall(x - STEP * currentSpeed, y + Sprite.SCALED_SIZE - 1)) {
                    x -= STEP * currentSpeed;
                    moving = true;
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, animate, 20);
                    }
                }
                break;
            case R:
                if (checkWall(x + STEP * currentSpeed + Sprite.SCALED_SIZE - 1, y) && checkWall(x + STEP * currentSpeed + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
                    x += STEP * currentSpeed;
                    moving = true;
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, animate, 20);
                    }
                }
                break;
            default:
                break;
        }
        img = sprite.getFxImage();
        table[getTileX()][getTileY()] = this;
    }

    @Override
    public void update() { 
        if (hurt) {
            gotHurt(Sprite.oneal_dead);
            return;
        }
        
        // Randomly change speed every 100 frames
        if (animate % 100 == 0) {
            currentSpeed = MIN_SPEED + new Random().nextInt(MAX_SPEED - MIN_SPEED + 1);
        }
        animate++;
        findDirection();
        onealMoving();
        checkCollideWithBomber();
    }
}

