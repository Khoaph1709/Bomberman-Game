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

    private static final int STEP = Sprite.STEP;
    private boolean moving = false;
    private boolean canReach = false;

    /**
     * Kiểm tra xem có thể di chuyển tới vị trí x, y hay không
     * @param x
     * @param y
     * @return true nếu có thể di chuyển tới vị trí x, y, ngược lại trả về false
     */
    @Override
    protected void findDirection() {
        animate++;
        if (animate > MAX_ANIMATE) {
            animate = 0;
        }
        if (x % Sprite.SCALED_SIZE == 0 && y % Sprite.SCALED_SIZE == 0) {
            Direction nDirection = FindPath.bfs(getTileX(), getTileY());
            if (nDirection != null) {
                moving = true;
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

    /**
     * Di chuyển enemy theo hướng direction
     */
    private void minvoMoving() {
        int px = getTileX();
        int py = getTileY();

        table[px][py] = null;
        sprite = Sprite.minvo_right1;
        if (hurt) {
            img = Sprite.movingSprite(Sprite.minvo_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, 20).getFxImage();
            return;
        }
        switch (direction) {
            case D:
                if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 1) && checkWall(x + Sprite.SCALED_SIZE - 1, y + STEP + Sprite.SCALED_SIZE - 1)) {
                    y += STEP;
                    moving = true;
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.minvo_right1, Sprite.minvo_right2, Sprite.minvo_right3, animate, 20);
                    }
                }
                break;
            case U:
                if (checkWall(x, y - STEP) && checkWall(x + Sprite.SCALED_SIZE - 1, y - STEP)) {
                    y -= STEP;
                    moving = true;
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.minvo_left1, Sprite.minvo_left2, Sprite.minvo_left3, animate, 20);
                    }
                }
                break;
            case L:
                if (checkWall(x - STEP, y) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 1)) {
                    x -= STEP;
                    moving = true; 
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.minvo_left1, Sprite.minvo_left2, Sprite.minvo_left3, animate, 20); 
                    }
                }
                break;
            case R:
                if (checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y) && checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
                    x += STEP;
                    moving = true;
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.minvo_right1, Sprite.minvo_right2, Sprite.minvo_right3, animate, 20); 
                    } 

                }
                break;
            default:
                break;
        }
        img = sprite.getFxImage();
        table[getTileX()][getTileY()] = this;
    }

    /**
     * Cập nhật trạng thái của enemy
     */
    @Override
    public void update() {
        if (hurt) {
            gotHurt(Sprite.minvo_dead);
        }
        animate++;
        findDirection();
        minvoMoving();
        checkCollideWithBomber();
    }
}
