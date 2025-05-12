package uet.oop.bomberman.algorithm;

import java.util.LinkedList;
import java.util.Queue;

import javafx.util.Pair;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Entity.Direction;
import uet.oop.bomberman.graphics.Sprite;

public class FindPath {
    public static Direction bfs(int i, int j) {
        boolean[][] check = new boolean[BombermanGame.WIDTH][BombermanGame.HEIGHT];
        Queue<Pair<Integer, Pair<Integer, Direction>>> q = new LinkedList<>();
        if (Entity.checkWall((i + 1) * Sprite.SCALED_SIZE, j * Sprite.SCALED_SIZE)) {
            check[i + 1][j] = true;
            q.add(new Pair<>(i + 1, new Pair<>(j, Direction.R)));
        }
        if (Entity.checkWall(i * Sprite.SCALED_SIZE, (j + 1) * Sprite.SCALED_SIZE)) {
            check[i][j + 1] = true;
            q.add(new Pair<>(i, new Pair<>(j + 1, Direction.D)));
        }
        if (Entity.checkWall((i - 1) * Sprite.SCALED_SIZE, j * Sprite.SCALED_SIZE)) {
            check[i - 1][j] = true;
            q.add(new Pair<>(i - 1, new Pair<>(j, Direction.L)));
        }
        if (Entity.checkWall(i * Sprite.SCALED_SIZE, (j - 1) * Sprite.SCALED_SIZE)) {
            check[i][j - 1] = true;
            q.add(new Pair<>(i, new Pair<>(j - 1, Direction.U)));
        }
        check[i][j] = true;
        while (!q.isEmpty()) {
            i = q.peek().getKey();
            assert q.peek() != null;
            j = q.peek().getValue().getKey();
            assert q.peek() != null;
            Direction direction = q.peek().getValue().getValue();
            q.remove();
            if (Entity.checkWall((i + 1) * Sprite.SCALED_SIZE, j * Sprite.SCALED_SIZE) && !check[i + 1][j]) {
                q.add(new Pair<>(i + 1, new Pair<>(j, direction)));
                check[i + 1][j] = true;
            }
            if (Entity.checkWall(i * Sprite.SCALED_SIZE, (j + 1) * Sprite.SCALED_SIZE) && !check[i][j + 1]) {
                q.add(new Pair<>(i, new Pair<>(j + 1, direction)));
                check[i][j + 1] = true;
            }
            if (Entity.checkWall((i - 1) * Sprite.SCALED_SIZE, j * Sprite.SCALED_SIZE) && !check[i - 1][j]) {
                q.add(new Pair<>(i - 1, new Pair<>(j, direction)));
                check[i - 1][j] = true;
            }
            if (Entity.checkWall(i * Sprite.SCALED_SIZE, (j - 1) * Sprite.SCALED_SIZE) && !check[i][j - 1]) {
                q.add(new Pair<>(i, new Pair<>(j - 1, direction)));
                check[i][j - 1] = true;
            }
            if (BombermanGame.getBomber().getTileX() == i && BombermanGame.getBomber().getTileY() == j) {
                return direction;
            }
        }
        return null;
    }
}