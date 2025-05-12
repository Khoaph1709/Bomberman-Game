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
        
        switch (direction) {
            case D:
                if (checkBrick(x, y + STEP + Sprite.SCALED_SIZE - 1) && (checkBrick(x + Sprite.SCALED_SIZE - 1, y + STEP + Sprite.SCALED_SIZE - 1))) {
                    y += STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, animate, 20);
                }
                break;
            case U:
                sprite = Sprite.kondoria_left2;
                if (checkBrick(x, y - STEP) && checkBrick(x + Sprite.SCALED_SIZE - 1, y - STEP)) {
                    y -= STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, animate, 20);
                }
                break;
            case L:
                sprite = Sprite.kondoria_left1;
                if (checkBrick(x - STEP, y) && checkBrick(x - STEP, y + Sprite.SCALED_SIZE - 1)) {
                    x -= STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, animate, 20);
                }
                break;
            case R:
                if (checkBrick(x + STEP + Sprite.SCALED_SIZE - 1, y) && checkBrick(x + STEP + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
                    x += STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, animate, 20);
                }
                break;
            default:
                break;
        }
        img = sprite.getFxImage();
        int new_px = getTileX();
        int new_py = getTileY();
        BombermanGame.table[new_px][new_py] = this;
    }

    @Override
    protected void findDirection() {
        if (animate > 1000) {
            animate = 0;
        }
        if (animate % 50 == 0) {
            List<Direction> validDirections = new ArrayList<>();

            for (Direction dir : Direction.values()) {
                boolean canMove = false;
                switch (dir) {
                    case D:
                        canMove = checkBrick(x, y + Sprite.SCALED_SIZE) 
                                && checkBrick(x + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE)
                                && checkBrick(x + Sprite.SCALED_SIZE / 2, y + Sprite.SCALED_SIZE);
                        break;
                    case U:
                        canMove = checkBrick(x, y - 1) 
                                && checkBrick(x + Sprite.SCALED_SIZE - 1, y - 1)
                                && checkBrick(x + Sprite.SCALED_SIZE / 2, y - 1);
                        break;
                    case L:
                        canMove = checkBrick(x - 1, y) 
                                && checkBrick(x - 1, y + Sprite.SCALED_SIZE - 1)
                                && checkBrick(x - 1, y + Sprite.SCALED_SIZE / 2);
                        break;
                    case R:
                        canMove = checkBrick(x + Sprite.SCALED_SIZE, y) 
                                && checkBrick(x + Sprite.SCALED_SIZE, y + Sprite.SCALED_SIZE - 1)
                                && checkBrick(x + Sprite.SCALED_SIZE, y + Sprite.SCALED_SIZE / 2);
                        break;
                    default:
                        break;
                }

                if (canMove) {
                    validDirections.add(dir);
                }
            }

            if (!validDirections.isEmpty()) {
                direction = validDirections.get(new Random().nextInt(validDirections.size()));
            }
        }
    }

    @Override
    public void update() {
        if (hurt) {
            gotHurt(Sprite.kondoria_dead);
            return;
        }
        animate++;
        moving = false;
        doriaMoving();
        findDirection();
        checkCollideWithBomber();
    }
}
