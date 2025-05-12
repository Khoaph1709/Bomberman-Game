package uet.oop.bomberman.entities.bomb;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import static uet.oop.bomberman.BombermanGame.enemies;
import static uet.oop.bomberman.BombermanGame.table;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.items.Item;
import uet.oop.bomberman.entities.items.Portal;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;

public class Bomb extends Entity {
    private int timeToExplode = 120; // 2 seconds (60 frames/second)
    public static int cnt = 0;
    private final int size;
    private final List<Entity> entities;
    private boolean exploded = false;
    private Entity portalPos = null;

    public int getFlameLength() {
        return size;
    }

    public int getTimeToExplode() {
        return timeToExplode;
    }

    public Bomb(int x, int y, Image img, List<Entity> entities, int size) {
        super(x, y, img);
        this.size = size;
        this.entities = entities;
        cnt++;
        if (table[x][y] instanceof Portal) {
            portalPos = table[x][y];
        }
        table[x][y] = this;
    }

    public void getImg() {
        if (exploded) {
            sprite = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1, Sprite.bomb_exploded2, animate, 20);
        } else {
            sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, animate, 60);
        }
        img = sprite.getFxImage();
    }

    private boolean checkBreak(int i, int j) {
        Entity cur = table[i][j];
        if (cur instanceof Wall) {
            return true;
        }
        if (cur instanceof Brick) {
            ((Brick) cur).setExploaded();
            return true;
        }
        return false;
    }

    public boolean isExploded() {
        return exploded;
    }

    public void setExpload() {
        this.animate = 70;
    }

    private void setDied(int i, int j) {
        Entity cur = table[i][j];
        if (cur instanceof Enemy) {
            for (Entity e : enemies) {
                if (e.getTileX() == i && e.getTileY() == j) {
                    e.setHurt();
                }
            }
        }
        
        if (cur instanceof Item) {
            cur.setDied();
        }
        if (cur instanceof Bomb && !((Bomb) cur).isExploded()) {
            ((Bomb) cur).setExpload();
        }
        if (BombermanGame.getBomber().getTileX() == i && BombermanGame.getBomber().getTileY() == j && !BombermanGame.getBomber().isFlamePass() && !BombermanGame.getBomber().isProtectded()) {
            BombermanGame.getBomber().setHurt();
        }
    }

    @Override
    public void update() {
        animate++;
        int px = getTileX();
        int py = getTileY();
        if (table[px][py] instanceof Portal) {
            portalPos = table[px][py];
        }
        table[px][py] = this;

        if (animate == 70) {
            exploded = true;
            Sound.explosion.play();
            Platform.runLater(
                () -> {
                    for (int c = 1; c <= size; c++) {
                        int i = x / Sprite.SCALED_SIZE - c, j = y / Sprite.SCALED_SIZE;
                        if (checkBreak(i, j)) {
                            break;
                        }
                        if (c < size) {
                            entities.add(new Flame(i, j, null, Direction.OH, entities));
                        } else {
                            entities.add(new Flame(i, j, null, Direction.L, entities));
                        }
                    }
                    for (int c = 1; c <= size; c++) {
                        int i = x / Sprite.SCALED_SIZE + c, j = y / Sprite.SCALED_SIZE;
                        if (checkBreak(i, j)) break;
                        if (c < size) {
                            entities.add(new Flame(i, j, Sprite.explosion_horizontal.getFxImage(), Direction.OH, entities));
                        } else {
                            entities.add(new Flame(i, j, Sprite.explosion_horizontal_right_last.getFxImage(), Direction.R, entities));
                        }
                    }
                    for (int c = 1; c <= size; c++) {
                        int i = x / Sprite.SCALED_SIZE, j = y / Sprite.SCALED_SIZE - c;
                            if (checkBreak(i, j)) break;
                            if (c < size) {
                                entities.add(new Flame(i, j, Sprite.explosion_vertical.getFxImage(), Direction.OV, entities));
                            } else {
                                entities.add(new Flame(i, j, Sprite.explosion_vertical_top_last.getFxImage(), Direction.U, entities));
                            }
                    }
                    for (int c = 1; c <= size; c++) {
                        int i = x / Sprite.SCALED_SIZE, j = y / Sprite.SCALED_SIZE + c;
                        if (checkBreak(i, j)) break;
                        if (c < size) {
                            entities.add(new Flame(i, j, Sprite.explosion_vertical.getFxImage(), Direction.OV, entities));
                        } else {
                            entities.add(new Flame(i, j, Sprite.explosion_vertical_down_last.getFxImage(), Direction.D, entities));
                        }
                    }
                    Timer bombTimer = new Timer();
                    bombTimer.schedule(new TimerTask() {
                        public void run() {
                            for (int c = 0; c <= size; c++) {
                                int i = x / Sprite.SCALED_SIZE - c, j = y / Sprite.SCALED_SIZE;
                                if (checkBreak(i, j)) break;
                                setDied(i, j);
                            }
                            for (int c = 1; c <= size; c++) {
                                int i = x / Sprite.SCALED_SIZE + c, j = y / Sprite.SCALED_SIZE;
                                if (checkBreak(i, j)) break;
                                setDied(i, j);
                            }
                            for (int c = 1; c <= size; c++) {
                                int i = x / Sprite.SCALED_SIZE, j = y / Sprite.SCALED_SIZE - c;
                                if (checkBreak(i, j)) break;
                                setDied(i, j);
                            }
                            for (int c = 1; c <= size; c++) {
                                int i = x / Sprite.SCALED_SIZE, j = y / Sprite.SCALED_SIZE + c;
                                if (checkBreak(i, j)) break;
                                setDied(i, j);
                            }
                        }
                    }, timeToExplode);
                }
            );
        }
        if (animate == 80) {
            Platform.runLater(
                () -> {
                    table[px][py] = portalPos;
                    cnt--;
                    entities.remove(this);
                }
            );
        }
        if (animate > 7500) {
            animate = 0;
        }
        getImg();
    }
}