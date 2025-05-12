package uet.oop.bomberman.entities.character;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.BombermanGame;
import static uet.oop.bomberman.BombermanGame.enemies;
import static uet.oop.bomberman.BombermanGame.entities;
import static uet.oop.bomberman.BombermanGame.table;
import uet.oop.bomberman.algorithm.FindPathAI;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.items.BombItem;
import uet.oop.bomberman.entities.items.FlameItem;
import uet.oop.bomberman.entities.items.FlamePassItem;
import uet.oop.bomberman.entities.items.Portal;
import uet.oop.bomberman.entities.items.SpeedItem;
import uet.oop.bomberman.entities.items.WallPassItem;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.KeyListener;
import uet.oop.bomberman.sound.Sound;


public class Bomber extends Entity {
    public boolean AI = false;
    
    public static int bomberLife;
    private int STEP = Sprite.STEP;
    private int blood = 4;
    
    // Movement related variables
    private KeyListener keyListener;
    private boolean moving = false;

    //Bombe related variables
    public int bombQuantity = 1;
    private boolean flamePass = false;
    private boolean wallPass = false;
    private int flameLength = 1;
    private int invincibleTimer = 0;
    private int bombSize = 1;

    public Bomber(int x, int y, Image img, KeyListener keyListener) {
        super(x, y, img);
        this.keyListener = keyListener;
    }

    public void setKeyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
    }

    /**
     * Place a bomb at the current position of the bomber.
     */
    public void placeBomb() {
        if (Bomb.cnt < bombQuantity && !(table[getTileX()][getTileY()] instanceof Bomb) && !(table[getTileX()][getTileY()] instanceof Brick)) {
            Platform.runLater(() -> {
                Entity object = new Bomb(getTileX(), getTileY(), Sprite.bomb.getFxImage(), entities, bombSize);
                entities.add(object);
            });
            Sound.placeBomb.play();
        }
    }
    

    private void chooseSprite() {
        animate += Sprite.STEP / 2;
        if (animate > 7500) {
            animate = 0;
        }
        else if (died) {
            img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, animate, 10).getFxImage();
            img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, animate, 10).getFxImage();
            entities.remove(this);
            return;
        }
        else if (hurt) {
            img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, animate, 10).getFxImage();
            return;
        }
        switch (direction) {
            case U:
                sprite = Sprite.player_up;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, animate, 20);
                }
                break;
            case D:
                sprite = Sprite.player_down;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, animate, 20);
                }
                break;
            case L:
                sprite = Sprite.player_left;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, animate, 20);
                }
                break;
            default:
                sprite = Sprite.player_right;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, animate, 20);
                }
                break;
        }
        img = sprite.getFxImage();
    }
    
    // Getters and setters for properties
    public int getSpeed() {
        return speed;
    }
    
    public int getFlameLength() {
        return flameLength;
    }

    public int getBlood() {
        return blood;   
    }
    
    public Bomber(int x, int y, Image img) {
        super(x, y, img);
    }

    public void getItem() {
        int px = (x + (75 * Sprite.SCALED_SIZE) / (2 * 100)) / Sprite.SCALED_SIZE;
        int py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        if (table[px][py] instanceof FlameItem) {
            if (!((FlameItem) table[px][py]).isPickUp()) {
                bombSize++;
            }
            ((FlameItem) table[px][py]).pick();
        } else if (table[px][py] instanceof SpeedItem) {
            if (!((SpeedItem) table[px][py]).isPickUp()) {
                speed++;
            }
            ((SpeedItem) table[px][py]).pick();
        } else if (table[px][py] instanceof BombItem) {
            if (!((BombItem) table[px][py]).isPickUp()) {
                bombQuantity++;
            }
            ((BombItem) table[px][py]).pick();
        } else if (table[px][py] instanceof Portal) {
            if (enemies.isEmpty()) {
                BombermanGame.gameState = BombermanGame.STATE.NEXT_LEVEL;
            }
        } else if (table[px][py] instanceof FlamePassItem) {
            if (!((FlamePassItem) table[px][py]).isPickUp()) {
                flamePass = true;
            }
            ((FlamePassItem) table[px][py]).pick();
        } else if (table[px][py] instanceof WallPassItem) {
            if (!((WallPassItem) table[px][py]).isPickUp()) {
                wallPass = true;
            }
            ((WallPassItem) table[px][py]).pick();
        }
    }

    @Override
    public void update() {
        bomberLife = blood;
        if (hurt) {
            if (hurt_time == 0) {
                Sound.died.play();
            }
            if (hurt_time == 30) {
                if (life == 0) {
                    BombermanGame.gameState = BombermanGame.STATE.END;
                }
                hurt = false;
                hurt_time = 0;
                invincibleTimer = 60 * 3 / 2;
                return;
            }
            chooseSprite();
            hurt_time++;
            return;
        }

        invincibleTimer = Math.max(0, invincibleTimer - 1);

        if (invincibleTimer == 0) {
            for (Entity entity : BombermanGame.enemies) {
                if (entity instanceof Enemy) {
                    if (checkCollisionEntity(entity)) {
                        blood--;
                        hurt = true;
                        break;
                    }
                }
            }

            for (Entity entity : BombermanGame.entities) {
                if (entity instanceof Flame && !flamePass) {
                    if (checkCollisionEntity(entity)) {
                        blood--;
                        hurt = true;
                        break;
                    }
                }
            }
        }

        moving = false;
        bomberMoving();
        getItem();
        chooseSprite();
        if (keyListener.isKeyPressed(KeyCode.SPACE)) {
            placeBomb();
        }
        if (blood <= 0) {
            BombermanGame.gameState = BombermanGame.STATE.END;
        }
    }

    private boolean checkCollisionEntity(Entity entity) {
        int bomberLeft = x;
        int bomberRight = x + Sprite.SCALED_SIZE;
        int bomberTop = y;
        int bomberBottom = y + Sprite.SCALED_SIZE;

        int entityLeft = entity.getX();
        int entityRight = entity.getX() + Sprite.SCALED_SIZE;
        int entityTop = entity.getY();
        int entityBottom = entity.getY() + Sprite.SCALED_SIZE;

        return bomberRight >= entityLeft
                && bomberLeft <= entityRight
                && bomberBottom >= entityTop
                && bomberTop <= entityBottom;
    }


    public int getPlayerX() {
        if (AI) {
            return (x + (1 * Sprite.SCALED_SIZE) / (2 * 1)) / Sprite.SCALED_SIZE;
        } else {
            return (x + (75 * Sprite.SCALED_SIZE) / (2 * 100)) / Sprite.SCALED_SIZE;
         }
    }

    public int getPlayerY() {
        if (AI) {
            return (y + (1 * Sprite.SCALED_SIZE) / (2 * 1)) / Sprite.SCALED_SIZE;
        } else {
            return (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
         }
    }

    private void AIDirection() {
        if (true) {
            animate += Sprite.STEP / 2;
            if (animate > 7500) {
                animate = 0;
            }
            int px = getTileX(); // Player's x-coordinate in grid
            int py = getTileY(); // Player's y-coordinate in grid
            Direction aiDirection = FindPathAI.decideDirection(px, py);
            if (aiDirection != null) {
                direction = aiDirection;
            }
            moving = true;
        }
    }

    /**
     * Check if the player collides with a wall.
     *
     * @param x the x-coordinate of the wall
     * @param y the y-coordinate of the wall
     */
    private void bomberMoving() {
        int px = getTileX(); // Player's x-coordinate in grid
        int py = getTileY(); // Player's y-coordinate in grid
        sprite = Sprite.player_down;
        // Clear current position
        Entity current = table[px][py];
        table[px][py] = null;        

        // Calculate next position based on input
        
        if (AI) {
            AIDirection();
            if (!wallPass) {
                if (keyListener.isKeyPressed(KeyCode.D) || (AI && direction == Direction.R) || keyListener.isKeyPressed(KeyCode.RIGHT)) {
                    direction = Direction.R;
                    if (checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y) && checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
                        x += STEP;
                        moving = true;
                    }
                }
                if (keyListener.isKeyPressed(KeyCode.A) || (AI && direction == Direction.L) || keyListener.isKeyPressed(KeyCode.LEFT)) {
                    direction = Direction.L;
                    if (checkWall(x - STEP, y) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 1)) {
                        x -= STEP;
                        moving = true;
                    }
                }
                if (keyListener.isKeyPressed(KeyCode.W) || (AI && direction == Direction.U) || keyListener.isKeyPressed(KeyCode.UP)) {
                    direction = Direction.U;
                    if (checkWall(x, y - STEP) && checkWall(x + Sprite.SCALED_SIZE - 1, y - STEP)) {
                        y -= STEP;
                        moving = true;
                    }
                }
                if (keyListener.isKeyPressed(KeyCode.S) || (AI && direction == Direction.D) || keyListener.isKeyPressed(KeyCode.DOWN)) {
                    direction = Direction.D;
                    if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 1) && checkWall(x + Sprite.SCALED_SIZE - 1, y + STEP + Sprite.SCALED_SIZE - 1)) {
                        y += STEP;
                        moving = true;     
                    } 
                }
            } else {
                if (keyListener.isKeyPressed(KeyCode.D) || (AI && direction == Direction.R) || keyListener.isKeyPressed(KeyCode.RIGHT)) {
                    direction = Direction.R;
                    if (checkBrick(x + STEP + Sprite.SCALED_SIZE - 1, y) && checkBrick(x + STEP + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
                        x += STEP;
                        moving = true;
                    }
                }
                if (keyListener.isKeyPressed(KeyCode.A) || (AI && direction == Direction.L) || keyListener.isKeyPressed(KeyCode.LEFT)) {
                    direction = Direction.L;
                    if (checkBrick(x - STEP, y) && checkBrick(x - STEP, y + Sprite.SCALED_SIZE - 1)) {
                        x -= STEP;
                        moving = true; 
                    } 
                }
                if (keyListener.isKeyPressed(KeyCode.W) || (AI && direction == Direction.U) || keyListener.isKeyPressed(KeyCode.UP)) {
                    direction = Direction.U;
                    if (checkBrick(x, y - STEP) && checkBrick(x + Sprite.SCALED_SIZE - 1, y - STEP)) {
                        y -= STEP;
                        moving = true;  
                    } 
                }
                if (keyListener.isKeyPressed(KeyCode.S) || (AI && direction == Direction.D) || keyListener.isKeyPressed(KeyCode.DOWN)) {
                    direction = Direction.D;
                    if (checkBrick(x, y + STEP + Sprite.SCALED_SIZE - 1) && checkBrick(x + Sprite.SCALED_SIZE - 1, y + STEP + Sprite.SCALED_SIZE - 1)) {
                        y += STEP;
                        moving = true;
                    } 
                }
            }
        } else if (!AI) {
            if (!wallPass) {
                if (keyListener.isKeyPressed(KeyCode.D) || (AI && direction == Direction.R) || keyListener.isKeyPressed(KeyCode.RIGHT)) {
                    direction = Direction.R;
                    if (checkWall(x + STEP + Sprite.SCALED_SIZE - 12, y + 3) && checkWall(x + STEP + Sprite.SCALED_SIZE - 12, y + Sprite.SCALED_SIZE - 3)) {
                        x += STEP;
                        moving = true;
                    }
                }
                if (keyListener.isKeyPressed(KeyCode.A) || (AI && direction == Direction.L) || keyListener.isKeyPressed(KeyCode.LEFT)) {
                    direction = Direction.L;
                    if (checkWall(x - STEP, y + 3) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 3)) {
                        x -= STEP;
                        moving = true;
                    }
                }
                if (keyListener.isKeyPressed(KeyCode.W) || (AI && direction == Direction.U) || keyListener.isKeyPressed(KeyCode.UP)) {
                    direction = Direction.U;
                    if (checkWall(x, y - STEP + 3) && checkWall(x + Sprite.SCALED_SIZE - 12, y - STEP + 3)) {
                        y -= STEP;
                        moving = true;
                    }
                }
                if (keyListener.isKeyPressed(KeyCode.S) || (AI && direction == Direction.D) || keyListener.isKeyPressed(KeyCode.DOWN)) {
                    direction = Direction.D;
                    if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 3) && checkWall(x + Sprite.SCALED_SIZE - 12, y + STEP + Sprite.SCALED_SIZE - 3)) {
                        y += STEP;
                        moving = true; 
                    } 
                }
            } else {
                if (keyListener.isKeyPressed(KeyCode.D) || AI || keyListener.isKeyPressed(KeyCode.RIGHT)) {
                    direction = Direction.R;
                    if (checkBrick(x + STEP + Sprite.SCALED_SIZE - 3, y) && checkBrick(x + STEP + Sprite.SCALED_SIZE - 12, y + Sprite.SCALED_SIZE - 3)) {
                        x += STEP;
                        moving = true;
                    }
                }
                if (keyListener.isKeyPressed(KeyCode.A) || AI || keyListener.isKeyPressed(KeyCode.LEFT)) {
                    direction = Direction.L;
                    if (checkBrick(x - STEP, y + 3) && checkBrick(x - STEP, y + Sprite.SCALED_SIZE - 3)) {
                        x -= STEP;
                        moving = true;
                    }
                }
                if (keyListener.isKeyPressed(KeyCode.W) || AI || keyListener.isKeyPressed(KeyCode.UP)) {
                    direction = Direction.U;
                    if (checkBrick(x, y - STEP + 3) && checkBrick(x + Sprite.SCALED_SIZE - 12, y - STEP + 3)) {
                        y -= STEP;
                        moving = true; 
                    }
                }
                if (keyListener.isKeyPressed(KeyCode.S) || AI || keyListener.isKeyPressed(KeyCode.DOWN)) {
                    direction = Direction.D;
                    if (checkBrick(x, y + STEP + Sprite.SCALED_SIZE - 3) && checkBrick(x + Sprite.SCALED_SIZE - 12, y + STEP + Sprite.SCALED_SIZE - 3)) {
                        y += STEP;
                        moving = true; 
                    } 
                }
            }
        }
        if (moving && animate % 15 == 0) {
            Sound.move.play();
        }
        table[px][py] = current;
    }

    public boolean isFlamePass() {
        return flamePass;
    }

    public boolean isProtectded() {
        return invincibleTimer > 0;
    }

    public void setTileX(int tileX) {
        this.x = tileX *  Sprite.SCALED_SIZE;
    }

    public void setTileY(int tileY) {
        this.y = tileY *  Sprite.SCALED_SIZE;
    }
}