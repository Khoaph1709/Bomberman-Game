package uet.oop.bomberman.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.KeyListener;

public class GameGUI {
    private Group root;
    private Canvas canvas;
    private GraphicsContext gc;
    private Text textLife;
    private Text textScore;
    private Text textLevel;

    public void setupStage(Stage stage, int width, int height) {
        root = new Group();
        canvas = new Canvas(Sprite.SCALED_SIZE * width, Sprite.SCALED_SIZE * height + 30);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        if (stage.getScene() == null) {
            Scene scene = new Scene(root, Color.BLACK);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(root);
        }
        KeyListener keyListener = new KeyListener(stage.getScene());
        BombermanGame.getBomber().setKeyListener(keyListener);
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public void renderGame(List<Entity> stillObjects, List<Entity> entities,
                           List<Entity> enemies, Bomber bomber) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        enemies.forEach(g -> g.render(gc));
        if (bomber != null) {
            bomber.render(gc);
        }
    }


    public void updateGameStats(int life, int score, int level) {
        if (textLife != null) root.getChildren().removeAll(textLife, textScore, textLevel);

        Font font = new Font("pixels", 20);
        textLife = createText(10, "LIFE: " + life, font);
        textScore = createText(100, "SCORE: " + score, font);
        textLevel = createText(canvas.getWidth()/2 - 40, "LEVEL: " + level, font);

        root.getChildren().addAll(textLife, textScore, textLevel);
    }

    private Text createText(double x, String content, Font font) {
        Text text = new Text(x, canvas.getHeight() - 8, content);
        text.setFont(font);
        text.setFill(Color.WHITE);
        return text;
    }


    public void createMainMenu(Stage stage, Runnable startAction, Runnable exitAction) {
        try {
            // Code tạo menu từ lớp Game cũ
            Image menuBG = new Image(new FileInputStream("res/button_menu/menu.jpeg"));
            ImageView bgView = new ImageView(menuBG);
            bgView.setFitWidth(960);
            bgView.setFitHeight(720);

            Button startBtn = createButton("res/button_menu/start.png", 400, 300, startAction);
            Button exitBtn = createButton("res/button_menu/exit.png", 400, 400, exitAction);

            root.getChildren().addAll(bgView, startBtn, exitBtn);
            stage.getScene().setRoot(root);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Button createButton(String imagePath, double x, double y, Runnable action) {
        try {
            Image img = new Image(new FileInputStream(imagePath));
            ImageView view = new ImageView(img);
            Button btn = new Button();
            btn.setGraphic(view);
            btn.setStyle("-fx-background-color: transparent;");
            btn.setTranslateX(x);
            btn.setTranslateY(y);
            btn.setOnAction(e -> action.run());
            return btn;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }
}