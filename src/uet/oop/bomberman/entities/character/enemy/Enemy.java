package uet.oop.bomberman.entities.character.enemy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Platform;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import static uet.oop.bomberman.BombermanGame.enemies;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;



public abstract class Enemy extends Entity {
    // protected Entity old_cur = null;
    protected final int MAX_ANIMATE = 7500;

    public Enemy(int x, int y, Image img) {
        super(x, y, img);
    }

    protected void findDirection() {
        if (animate > MAX_ANIMATE) {
            animate = 0;
        }
        if (animate % 70 == 0) {
            List<Direction> validDirections = new ArrayList<>();

            for (Direction dir : Direction.values()) {
                boolean canMove = false;
                switch (dir) {
                    case D:
                        canMove = checkWall(x, y + Sprite.SCALED_SIZE) 
                                && checkWall(x + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE)
                                && checkWall(x + Sprite.SCALED_SIZE / 2, y + Sprite.SCALED_SIZE);
                        break;
                    case U:
                        canMove = checkWall(x, y - 1) 
                                && checkWall(x + Sprite.SCALED_SIZE - 1, y - 1)
                                && checkWall(x + Sprite.SCALED_SIZE / 2, y - 1);
                        break;
                    case L:
                        canMove = checkWall(x - 1, y) 
                                && checkWall(x - 1, y + Sprite.SCALED_SIZE - 1)
                                && checkWall(x - 1, y + Sprite.SCALED_SIZE / 2);
                        break;
                    case R:
                        canMove = checkWall(x + Sprite.SCALED_SIZE, y) 
                                && checkWall(x + Sprite.SCALED_SIZE, y + Sprite.SCALED_SIZE - 1)
                                && checkWall(x + Sprite.SCALED_SIZE, y + Sprite.SCALED_SIZE / 2);
                        break;
                    default:
                        break;
                }

                if (canMove) {
                    validDirections.add(dir);
                }
            }

            if (!validDirections.isEmpty()) {
                Random random = new Random();
                
                // Lấy vị trí Bomber
                int bomberX = BombermanGame.getBomber().getTileX();
                int bomberY = BombermanGame.getBomber().getTileY();
                int enemyX = getTileX();
                int enemyY = getTileY();
                
                // Tính toán hướng ưu tiên
                Direction preferredDirection;
                int dx = bomberX - enemyX;
                int dy = bomberY - enemyY;
                
                if (Math.abs(dx) > Math.abs(dy)) {
                    preferredDirection = (dx > 0) ? Direction.R : Direction.L;
                } else {
                    preferredDirection = (dy > 0) ? Direction.D : Direction.U;
                }
                
                // Xác suất 70% đuổi theo Bomber nếu hướng hợp lệ
                if (validDirections.contains(preferredDirection) && random.nextDouble() < 0.7) {
                    direction = preferredDirection;
                } else {
                    direction = validDirections.get(random.nextInt(validDirections.size()));
                }
            }
        }
    }

    protected void gotHurt(Sprite sprite) {
        hurt_time++;
        if (hurt_time == 1) {
            Sound.mobDied.play();
        }
        img = sprite.getFxImage();
        if (hurt_time == 20) {
            hurt_time = 0;
            hurt = false;
            if (life == 0) {
                Platform.runLater(() -> {
                    enemies.remove(this);
                    x = getTileX();
                    y = getTileY();
                    BombermanGame.table[getTileX()][getTileY()] = null;
                });
            }
        }
        this.sprite = Sprite.movingSprite(sprite, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, 20);
        img = sprite.getFxImage();
        
        // if (animate == 60) {
        //     Platform.runLater(() -> {
        //         enemies.remove(this);
        //         x = getTileX();
        //         y = getTileY();
        //         BombermanGame.table[x][y] = null;
        //     });
        // }
    }

    public static boolean checkWall(int x, int y) {
        if (x < 0 || y < 0 || x > Sprite.SCALED_SIZE * BombermanGame.WIDTH || y > Sprite.SCALED_SIZE * BombermanGame.HEIGHT)
            return false;

        x /= Sprite.SCALED_SIZE;
        y /= Sprite.SCALED_SIZE;
        Entity entity = getEntity(x, y);
        return !(entity instanceof Wall) && !(entity instanceof Brick) && !(entity instanceof Bomb) && !(entity instanceof Doria);
    }
    

    protected void checkCollideWithBomber() {
        if (BombermanGame.getBomber().getPlayerX() == getTileX() && BombermanGame.getBomber().getPlayerY() == getTileY() && !BombermanGame.getBomber().isProtectded()) {
            BombermanGame.getBomber().setHurt();
        }
    }
}
