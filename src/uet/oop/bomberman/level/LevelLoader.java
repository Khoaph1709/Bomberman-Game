package uet.oop.bomberman.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import uet.oop.bomberman.BombermanGame;
import static uet.oop.bomberman.BombermanGame.hiddenTable;
import static uet.oop.bomberman.BombermanGame.table;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Balloom;
import uet.oop.bomberman.entities.character.enemy.Doll;
import uet.oop.bomberman.entities.character.enemy.Doria;
import uet.oop.bomberman.entities.character.enemy.Minvo;
import uet.oop.bomberman.entities.character.enemy.Oneal;
import uet.oop.bomberman.entities.items.*;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.graphics.Sprite;



public class LevelLoader {
    private int width;
    private int height;
    private List<Entity> entities;
    private List<Entity> stillObjects;
    private List<Entity> enemies;
    private List<Entity> flames;

    private Bomber bomber;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bomber getBomber() {
        return bomber; 
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getStillObjects() {
        return stillObjects;
    }

    public List<Entity> getEnemies() {
        return enemies; 
    }

    public List<Entity> getFlames() {
       return flames; 
    }

    public LevelLoader() {
        entities = new ArrayList<>();
        stillObjects = new ArrayList<>();
    }

    public LevelLoader(String levelPath) {
        stillObjects = new ArrayList<>();
        entities = new ArrayList<>();
        enemies = new ArrayList<>();
        flames = new ArrayList<>();
        try {
            File file = new File(levelPath);
            Scanner scanner = new Scanner(file);

            int level = scanner.nextInt();
            int height = scanner.nextInt();
            int width = scanner.nextInt();
            scanner.nextLine();

            for (int i = 0; i < height; i++) {
                String line = scanner.nextLine().trim();
                processLine(line, i);
            }
            BombermanGame.MAXSCORE = enemies.size();
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found! " + e.getMessage());
        }
    }

    private void processLine(String line, int row) {
        for (int col = 0; col < line.length(); col++) {
            char c = line.charAt(col);
            createEntity(col, row, c);
        }
    }

    private void createEntity(int x, int y, char c) {
        switch (c) {
            case '#': // Tường
                Entity wall = new Wall(x, y, Sprite.wall.getFxImage());
                stillObjects.add(wall);
                table[x][y] = wall;
                break;
            case '*': // Gạch
                Entity brick = new Brick(x, y, Sprite.brick.getFxImage());
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage())); 
                entities.add(brick);
                table[x][y] = brick;
                break;
            case 'p': // Bomber
                if (bomber == null) {
                    bomber = BombermanGame.getBomber();
                    entities.add(bomber);
                    table[x][y] = bomber;
                } else {
                    System.out.println("Warning: Multiple Bomber positions detected in level file. Using first occurrence.");
                }
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                break;
            case '1': // Balloom
                Entity balloom = new Balloom(x, y, Sprite.balloom_right1.getFxImage());
                enemies.add(balloom);
                table[x][y] = balloom;
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                break;
            case '2': // Oneal
                Entity oneal = new Oneal(x, y, Sprite.oneal_right1.getFxImage());
                enemies.add(oneal);
                table[x][y] = oneal;
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                break;
            case '3': // Doll
                Entity doll = new Doll(x, y, Sprite.doll_right1.getFxImage());
                enemies.add(doll);
                table[x][y] = doll;
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                break;
            case '4': // Minvo
                Entity minvo = new Minvo(x, y, Sprite.minvo_right1.getFxImage());
                enemies.add(minvo);
                table[x][y] = minvo;
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                break;
            case '5': 
                Entity doria = new Doria(x, y, Sprite.kondoria_right1.getFxImage());
                enemies.add(doria);
                table[x][y] = doria;
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                break;
            case 'b': // BombItem
                Entity bombItem = new BombItem(x, y, Sprite.powerup_bombs.getFxImage());
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                entities.add(new Brick(x, y, Sprite.brick.getFxImage()));
                table[x][y] = new Brick(x, y, Sprite.brick.getFxImage());
                hiddenTable[x][y] = bombItem;
                break;
            case 'f': // FlameItem
                Entity flameItem = new FlameItem(x, y, Sprite.powerup_flames.getFxImage());
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                entities.add(new Brick(x, y, Sprite.brick.getFxImage()));
                table[x][y] = new Brick(x, y, Sprite.brick.getFxImage());
                hiddenTable[x][y] = flameItem;
                break;
            case 's': // SpeedItem
                Entity speedItem = new SpeedItem(x, y, Sprite.powerup_speed.getFxImage());
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                entities.add(new Brick(x, y, Sprite.brick.getFxImage()));
                table[x][y] = new Brick(x, y, Sprite.brick.getFxImage());
                hiddenTable[x][y] = speedItem;
                break;
            case 'x': // Portal
                Entity portal = new Portal(x, y, Sprite.portal.getFxImage());
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                entities.add(new Brick(x, y, Sprite.brick.getFxImage()));
                table[x][y] = new Brick(x, y, Sprite.brick.getFxImage());
                hiddenTable[x][y] = portal;
                break;
            case 'a':
                Entity flamePass = new FlamePassItem(x, y, Sprite.powerup_flamepass.getFxImage());
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                entities.add(new Brick(x, y, Sprite.brick.getFxImage()));
                table[x][y] = new Brick(x, y, Sprite.brick.getFxImage());
                hiddenTable[x][y] = flamePass;
                break;
            case 'c':
                Entity wallPass = new WallPassItem(x, y, Sprite.powerup_wallpass.getFxImage());
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                entities.add(new Brick(x, y, Sprite.brick.getFxImage()));
                table[x][y] = new Brick(x, y, Sprite.brick.getFxImage());
                hiddenTable[x][y] = wallPass;
                break;
            default: // Grass
                stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                break;
        }
    }
}
