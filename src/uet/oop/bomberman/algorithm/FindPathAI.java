package uet.oop.bomberman.algorithm;

import static java.lang.Math.abs;
import java.util.LinkedList;
import java.util.Queue;

import javafx.util.Pair;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Entity.Direction;
import static uet.oop.bomberman.entities.Entity.checkWall;
import static uet.oop.bomberman.entities.Entity.getEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.items.Portal;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.graphics.Sprite;




public class FindPathAI {
    private static final int SAFE_DISTANCE = 2;
    private static boolean isPortalRevealed = false;

    public static boolean isSafe(int x, int y) {
        x = x / Sprite.SCALED_SIZE;
        y = y / Sprite.SCALED_SIZE;
        for (Entity e : BombermanGame.entities) {
            if (e instanceof Bomb) {
                Bomb bomb = (Bomb) e;
                int dx = abs(x - bomb.getTileX());
                int dy = abs(y - bomb.getTileY());
                if (dx + dy <= bomb.getFlameLength() && bomb.getTimeToExplode() <= 30) {
                    return false;
                }
            }
        }
        return true;
    }   

    public static int distance(int a, int b, int x, int y) {
        int ans = abs(a - x) + abs(b - y);
        return ans;
    }

    private static Pair<Integer, Integer> findNearestEnemy(int startX, int startY) {
        int minDistance = Integer.MAX_VALUE;
        Pair<Integer, Integer> target = null;
        for (Entity e : BombermanGame.entities) {
            if (e instanceof Enemy) {
                int ex = e.getTileX();
                int ey = e.getTileY();
                int dist = distance(startX, startY, ex, ey);
                if (dist < minDistance) {
                    minDistance = dist;
                    target = new Pair<>(ex, ey);
                }
            }
        }
        return target;
    }

    private static Pair<Integer, Integer> findPortal() {
        for (Entity e : BombermanGame.entities) {
            if (e instanceof Portal) {
                return new Pair<>(e.getTileX(), e.getTileY());
            }
        }
        return null;
    }

    

    public static Direction findSafePath(int startX, int startY) {
        Queue<Pair<Integer, Pair<Integer, Direction>>> q = new LinkedList<>();
        boolean[][] visited = new boolean[BombermanGame.WIDTH][BombermanGame.HEIGHT];
        q.add(new Pair<>(startX, new Pair<>(startY, null)));

        while (!q.isEmpty()) {
           Pair<Integer, Pair<Integer, Direction>> cuurent = q.poll();
           int x = cuurent.getKey();
           int y = cuurent.getValue().getKey();
           Direction dir = cuurent.getValue().getValue(); 

           if (checkWall(x, y)) {
               continue;
           }

           visited[x][y] = true;

           Entity entity = getEntity(x, y);
           if (entity instanceof Wall || entity instanceof Brick || entity instanceof Bomb) {
               continue;
           }

           if (isSafe(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE)) {
               if (dir != null) {
                   return dir;
               }
           }
           q.add(new Pair<>(x + 1, new Pair<>(y, dir == null ? Direction.R : dir)));
           q.add(new Pair<>(x - 1, new Pair<>(y, dir == null ? Direction.L : dir)));
           q.add(new Pair<>(x, new Pair<>(y + 1, dir == null ? Direction.D : dir)));
           q.add(new Pair<>(x, new Pair<>(y - 1, dir == null ? Direction.U : dir)));
        }
        return null;
    }

    public static Direction decideDirection(int currentX, int currentY) {
        // Ưu tiên né bomb
        Direction safeDir = findSafePath(currentX, currentY);
        if (safeDir != null) return safeDir;
    
        // Né quái nếu quá gần
        Pair<Integer, Integer> nearestEnemy = findNearestEnemy(currentX, currentY);
        if (nearestEnemy != null && distance(currentX, currentY, nearestEnemy.getKey(), nearestEnemy.getValue()) <= 2) {
            return avoidEnemy(currentX, currentY, nearestEnemy.getKey(), nearestEnemy.getValue());
        }
    
        // Tiêu diệt quái nếu còn tồn tại
        if (nearestEnemy != null) {
            Direction dirToEnemy = bfsFindPath(currentX, currentY, nearestEnemy.getKey(), nearestEnemy.getValue());
            if (distance(currentX, currentY, nearestEnemy.getKey(), nearestEnemy.getValue()) <= 2) {
                BombermanGame.getBomber().placeBomb();
            }
            return dirToEnemy;
        }
    
        // Chỉ tìm Portal khi đã hết quái
        if (areAllEnemiesDead()) {
            Pair<Integer, Integer> portalPos = findPortal();
            if (portalPos != null) {
                return bfsFindPath(currentX, currentY, portalPos.getKey(), portalPos.getValue());
            }
        }
    
        // Phá Brick nếu chưa hết quái hoặc chưa tìm thấy Portal
        return bfsFindBrick(currentX, currentY);
    }
    
    // Tránh quái
    private static Direction avoidEnemy(int currentX, int currentY, int enemyX, int enemyY) {
        // Sử dụng mảng Object để lưu delta x, delta y và Direction
        Object[][] escapeDirs = {
            {1, 0, Direction.R},
            {-1, 0, Direction.L},
            {0, 1, Direction.D},
            {0, -1, Direction.U}
        };
    
        for (Object[] dir : escapeDirs) {
            int dx = (int) dir[0]; // Ép kiểu phần tử đầu tiên thành int (delta x)
            int dy = (int) dir[1]; // Ép kiểu phần tử thứ hai thành int (delta y)
            Direction direction = (Direction) dir[2]; // Ép kiểu phần tử thứ ba thành Direction
    
            int newX = currentX + dx;
            int newY = currentY + dy;
    
            if (isValidPosition(newX, newY)) {
                Entity entity = getEntity(newX, newY);
                if (!(entity instanceof Enemy) && !isObstacle(newX, newY) && isSafe(newX * Sprite.SCALED_SIZE, newY * Sprite.SCALED_SIZE)) {
                    return direction; // Trả về Direction đúng kiểu
                }
            }
        }
        return null;
    }
    
    // Tìm Brick để phá
    private static Direction bfsFindBrick(int startX, int startY) {
        boolean[][] visited = new boolean[BombermanGame.WIDTH][BombermanGame.HEIGHT];
        Queue<Pair<Pair<Integer, Integer>, Direction>> queue = new LinkedList<>();
    
        Object[][] directions = {
            {1, 0, Direction.R},
            {-1, 0, Direction.L},
            {0, 1, Direction.D},
            {0, -1, Direction.U}
        };
    
        for (Object[] dir : directions) {
            int dx = (int) dir[0];
            int dy = (int) dir[1];
            Direction direction = (Direction) dir[2];
            int newX = startX + dx;
            int newY = startY + dy;
    
            if (isValidPosition(newX, newY) && !isObstacle(newX, newY) && isSafe(newX * Sprite.SCALED_SIZE, newY * Sprite.SCALED_SIZE)) {
                queue.add(new Pair<>(new Pair<>(newX, newY), direction));
                visited[newX][newY] = true;
            }
        }
    
        while (!queue.isEmpty()) {
            Pair<Pair<Integer, Integer>, Direction> current = queue.poll();
            int x = current.getKey().getKey();
            int y = current.getKey().getValue();
            Direction initialDir = current.getValue();
    
            // Kiểm tra Brick
            Entity entity = getEntity(x, y);
            if (entity instanceof Brick) {
                return initialDir;
            }
    
            for (Object[] dir : directions) {
                int dx = (int) dir[0];
                int dy = (int) dir[1];
                Direction direction = (Direction) dir[2];
                int newX = x + dx;
                int newY = y + dy;
    
                if (isValidPosition(newX, newY) && !visited[newX][newY] && !isObstacle(newX, newY) && isSafe(newX * Sprite.SCALED_SIZE, newY * Sprite.SCALED_SIZE)) {
                    visited[newX][newY] = true;
                    queue.add(new Pair<>(new Pair<>(newX, newY), initialDir));
                }
            }
        }
        return null;
    }

    private static Direction bfsFindPath(int startX, int startY, int targetX, int targetY) {
        boolean[][] visited = new boolean[BombermanGame.WIDTH][BombermanGame.HEIGHT];
        Queue<Pair<Pair<Integer, Integer>, Direction>> queue = new LinkedList<>();
    
        // Sử dụng mảng Object chứa delta x, delta y và Direction
        Object[][] directions = {
            {1, 0, Direction.R},
            {-1, 0, Direction.L},
            {0, 1, Direction.D},
            {0, -1, Direction.U}
        };
    
        // Khởi tạo queue với các hướng đi ban đầu
        for (Object[] dir : directions) {
            int dx = (int) dir[0];
            int dy = (int) dir[1];
            Direction direction = (Direction) dir[2];
            int newX = startX + dx;
            int newY = startY + dy;
            if (isValidPosition(newX, newY) && !isObstacle(newX, newY) && isSafe(newX * Sprite.SCALED_SIZE, newY * Sprite.SCALED_SIZE)) {
                queue.add(new Pair<>(new Pair<>(newX, newY), direction));
                visited[newX][newY] = true;
            }
        }
    
        while (!queue.isEmpty()) {
            Pair<Pair<Integer, Integer>, Direction> current = queue.poll();
            int x = current.getKey().getKey();
            int y = current.getKey().getValue();
            Direction initialDir = current.getValue();
    
            // Kiểm tra đích
            if (x == targetX && y == targetY) {
                return initialDir;
            }
    
            // Duyệt các hướng đi
            for (Object[] dir : directions) {
                int dx = (int) dir[0];
                int dy = (int) dir[1];
                Direction direction = (Direction) dir[2];
                int newX = x + dx;
                int newY = y + dy;
                if (isValidPosition(newX, newY) && !visited[newX][newY] && !isObstacle(newX, newY) && isSafe(newX * Sprite.SCALED_SIZE, newY * Sprite.SCALED_SIZE)) {
                    visited[newX][newY] = true;
                    queue.add(new Pair<>(new Pair<>(newX, newY), initialDir));
                }
            }
        }
        return null; // Không tìm thấy đường
    }
    
    // Kiểm tra vị trí hợp lệ
    private static boolean isValidPosition(int x, int y) {
        return x >= 0 && y >= 0 && x < BombermanGame.WIDTH && y < BombermanGame.HEIGHT;
    }
    
    // Kiểm tra vật cản
    private static boolean isObstacle(int x, int y) {
        Entity entity = getEntity(x, y);
        return entity instanceof Wall || entity instanceof Bomb;
    }

    private static boolean areAllEnemiesDead() {
        return (BombermanGame.enemies.isEmpty());
    }
}
