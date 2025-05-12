package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;


public abstract class Entity {

    protected int x;

    protected int y;

    protected Image img;

    protected Sprite sprite;

    protected int animate = 0;

    protected int life = 1;
    
    protected boolean died = false;

    protected boolean hurt = false;
    protected int hurt_time = 0;

    protected int hurtAnimationFramse = 0;
    protected static final int HURT_ANIMATION_DURATION = 20;

    protected int speed = 1; // pixel / frame

    protected Direction direction = Direction.R;

    //Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    public Entity(int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
    }

    public void setImage(Image img) {
        this.img = img;
    }

    public static Entity getEntity(int x, int y) {
        return BombermanGame.table[x][y];
    }

    /**
     * Kiểm tra xem có thể di chuyển tới vị trí x, y hay không
     */
    public static boolean checkWall(int x, int y) {
        if (x < 0 || y < 0 || x > Sprite.SCALED_SIZE * BombermanGame.WIDTH || y > Sprite.SCALED_SIZE * BombermanGame.HEIGHT)
            return false;

        x /= Sprite.SCALED_SIZE;
        y /= Sprite.SCALED_SIZE;
        Entity entity = getEntity(x, y);
        
        return !(entity instanceof Wall) && !(entity instanceof Brick) && !(entity instanceof Bomb);
    }

    /**
     * Kiểm tra xem có thể di chuyển tới vị trí x, y hay không
     * x là tọa độ pixel của entity
     * y là tọa độ pixel của entity
     */
    public static boolean checkBrick(int x, int y) {
        if (x < 0 || y < 0 || x > Sprite.SCALED_SIZE * BombermanGame.WIDTH || y > Sprite.SCALED_SIZE * BombermanGame.HEIGHT)
            return false;

        x /= Sprite.SCALED_SIZE;
        y /= Sprite.SCALED_SIZE;
        Entity entity = getEntity(x, y);
        return !(entity instanceof Wall) && !(entity instanceof Bomb);
    }


    public int getTileX() {
        return Coordinates.pixelToTile(x);
    }

    public int getTileY() {
        return Coordinates.pixelToTile(y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setDied() {
        this.died = true;
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public void reduceLife() {
        this.life--;
    }

    public void setHurt() {
        if (!hurt) {
            reduceLife();
        }
        hurt = true;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(img, x, y);
    }

    public abstract void update();

    public enum Direction {
        L, R, U, D, OH, OV
    }

}
