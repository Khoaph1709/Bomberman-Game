package uet.oop.bomberman;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameGUI;
import uet.oop.bomberman.input.KeyListener;
import uet.oop.bomberman.level.LevelLoader;
import uet.oop.bomberman.sound.Sound;

public class BombermanGame extends Application {
    private static Bomber gameBomber = new Bomber(1, 1, Sprite.player_down.getFxImage(), null);

    public static Bomber getBomber() {
        return gameBomber;
    }
    
    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;
    private static final int INIT_LEVEL = 0;
    private static final int MAX_LEVEL = 5;
    public static long FPS_GAME = 1000 / 60;
    public static int level = INIT_LEVEL;

    private GraphicsContext gc;
    private GameGUI gameGUI = new GameGUI();
    public static List<Entity> entities = new ArrayList<>();
    public static List<Entity> stillObjects = new ArrayList<>();
    public static List<Entity> enemies = new ArrayList<>();
    public List<Entity> flames = new ArrayList<>();
    public static boolean isEnd = false;
    Group root = null;
    private Canvas canvas;
    private Text textLife = null;
    private Text textLevel = null;
    public static int MAXSCORE = 0;
    private boolean newGame = true;
    private AnimationTimer gameTimer;
    private AnimationTimer mainTimer;

    public static Sound music;
    

    private final Effect shadow = new DropShadow();

    public static Entity[][] table;
    public static Entity[][] hiddenTable;
    public static STATE gameState = STATE.MENU;

    @Override
    public void start(Stage stage) {
        if (mainTimer != null) {
            mainTimer.stop();
            mainTimer = null;
        }

        gameBomber = new Bomber(1, 1, Sprite.player_down.getFxImage(), null);
        gameState = STATE.MENU;
        newGame = true;
        isEnd = false;
        mainTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (newGame) {
                    gameLoop(stage);
                    newGame = false;
                }
                if (gameState == STATE.EXIT) {
                    System.exit(0);
                }
            }
        };
        mainTimer.start();

        createMap();
    }

    public void createMap() {
        try {
            table = new Entity[WIDTH][HEIGHT];
            hiddenTable = new Entity[WIDTH][HEIGHT];
            LevelLoader levelLoader = new LevelLoader("res/levels/Level" + level + ".txt");
            stillObjects = levelLoader.getStillObjects();
            entities = levelLoader.getEntities();
            enemies = levelLoader.getEnemies();
            System.out.println("Loaded " + stillObjects.size() + " static objects");
            System.out.println("Loaded " + entities.size() + " entities");
            System.out.println("Loaded " + enemies.size() + " enemies");

            gameBomber.setTileX(1);
            gameBomber.setTileY(1);
        } catch (Exception e) {
            System.err.println("Error loading level:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void update() {
        entities.forEach(Entity::update);
        enemies.forEach(Entity::update);    
    }

    public void render(Stage stage) {
        switch (gameState) {
            case SINGLEPLAYER:
                Bomber bomber = new  Bomber(1, 1, Sprite.player_down.getFxImage(), null);
                for (Entity entity : entities) {
                    if (entity instanceof Bomber) {
                        bomber = (Bomber) entity;
                        break;
                    }
                }
                gameBomber = bomber;
                gameGUI.renderGame(stillObjects, entities, enemies, bomber);
                gameGUI.updateGameStats(gameBomber.getBlood(), ((MAXSCORE - enemies.size()) * 100), level);
                break;
            case PAUSE:
                break;
            case NEXT_LEVEL:
                if (level < MAX_LEVEL) {
                    music.stop();
                    Sound.win.play();
                    gameBomber.resetStats();
                    gameState = STATE.SINGLEPLAYER;
                    level++;
                    gameGUI = new GameGUI(); //TODO: FIX BUG
                    gameGUI.setupStage(stage, WIDTH, HEIGHT);
                    canvas = gameGUI.getCanvas();
                    gc = gameGUI.getGraphicsContext();
                    stage.show();
                    createMap();
                } else if (!isEnd) {
                    music.stop();
                    Sound.youWin.play();
                    end(stage);
                    isEnd = true;
                }
                break;
            case END:
                if (!isEnd) {
                    //TODO: end game
                    end(stage);
                    isEnd = true;
                }
                break;
            default:
                throw new IllegalStateException("Invalid game state");
        } 
    }

    public void single(Stage stage) {
        if (music != null) {
            music.stop();
        }
        gameGUI = new GameGUI();
        gameGUI.setupStage(stage, WIDTH, HEIGHT);
        KeyListener keyListener = new KeyListener(stage.getScene());
        gameBomber.setKeyListener(keyListener);
        gc = gameGUI.getGraphicsContext();
        stage.show();

        if (gameTimer != null) {
            gameTimer.stop();
        }

        gameTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (gameState == STATE.MENU || gameState == STATE.END) {
                    newGame = true;
                    stop(); 
                    return;
                } else {
                    update();
                    render(stage);
                }
                long frameTime = (now - lastUpdate) / 1000000;
                if (frameTime < FPS_GAME) {
                    try {
                        Thread.sleep(FPS_GAME - frameTime);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                lastUpdate = System.nanoTime();
            }
        };
        gameTimer.start();
    }

    public void gameLoop(Stage stage) {
        switch (gameState) {
            case MENU:
                menu(stage);
                break;
            case SINGLEPLAYER:
                single(stage);
                break;
            case MULTIPLAYER:
                break;
            case PAUSE:
                break;
            case EXIT:
                System.exit(0);
                break;
            case END:
                if (!isEnd) {
                    end(stage);
                    isEnd = true;
                }
                break;
            default:
                throw new IllegalStateException("Invalid game state");
        }
    }

    public void end(Stage stage) {
        if (music!= null) {
            music.stop();
        }
        InputStream stream = null;
        Button button = new Button();
        button.setStyle("-fx-background-color: transparent; ");
        button.setPrefSize(166, 66);
        if (gameState == STATE.NEXT_LEVEL) {
            music = Sound.youWin;
            music.play();
            try {
                stream = new FileInputStream("res/button_menu/youwin.png");
            } catch (Exception e) {
                e.getMessage();
            }
        } else {
            music = Sound.ending;
            music.play();
            try {
                stream = new FileInputStream("res/button_menu/replay.png");
            } catch (Exception e) {
                e.getMessage();
            }
        }
        Image img = new Image(stream);
        ImageView view = new ImageView();
        view.setFitHeight(60);
        view.setFitWidth(170);
        view.setImage(img);
        button.setGraphic(view);
        button.setTranslateX(Sprite.MENU_SIZE * 15 - 170 / 2);
        button.setTranslateY(Sprite.SCALED_SIZE * 10 - 10);
        button.setOnMouseEntered(e -> button.setEffect(shadow));
        button.setOnMouseExited(e -> button.setEffect(null));
        button.setOnAction(event -> {
            if (gameTimer != null) {
                gameTimer.stop();
            }
            if (mainTimer != null) {
                mainTimer.stop();
            }
            if (gameState == STATE.NEXT_LEVEL) {
                gameState = STATE.SINGLEPLAYER;
                newGame = true;
            } else if (gameState == STATE.END) {
                resetGame();
                gameState = STATE.MENU;
                newGame = true;
                createMap();
            }
            isEnd = false;
            stage.close();
            Stage newStage = new Stage();
            start(newStage);
        });
        Group newRoot = new Group();
        try {
            stream = new FileInputStream("res/button_menu/endgame.jpeg");
        } catch (Exception e) {
            e.getMessage();
        }
        Image image = new Image(stream);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitWidth(30 * Sprite.MENU_SIZE);
        imageView.setFitHeight(15 * Sprite.MENU_SIZE);
        newRoot.getChildren().add(imageView);
        newRoot.getChildren().add(button);
        Scene scene = new Scene(newRoot, Sprite.MENU_SIZE * 30, Sprite.MENU_SIZE * 15, Color.BLACK);
        stage.setTitle("BOMBERMAN");
        stage.setScene(scene);
        stage.show();
    }

    private void resetGame() {
        entities.clear();
        stillObjects.clear();
        enemies.clear();
        gameBomber = new Bomber(1, 1, Sprite.player_down.getFxImage(), null);
        entities.add(gameBomber);
        MAXSCORE = 0;
        isEnd = false;
        level = INIT_LEVEL;
        gameBomber.bombQuantity = 1;
        Bomb.cnt = 0;
        gameBomber.setKeyListener(null);
        newGame = true;
    }

    public void menu(Stage stage) {
        // resetGame();
        if (music != null) {
            music.stop();
        }
        music = Sound.titleScreen;
        music.loop();
        Group menuRoot = new Group();
        Button startButton = new Button();
        Button exitButton = new Button();
        Button AIButton = new Button();
        startButton.setStyle("-fx-background-color: transparent; ");
        startButton.setPrefSize(166, 66);
        startButton.setTranslateX(Sprite.MENU_SIZE * 30 / 2 - 166 / 2);
        startButton.setTranslateY(Sprite.MENU_SIZE * 15 / 2 - 25);
        InputStream stream = null;
        try {
            stream = new FileInputStream("res/button_menu/start.png");
        } catch (Exception e) {
            e.getMessage();
        }
        Image img = new Image(stream);
        ImageView view = new ImageView();
        view.setFitHeight(66);
        view.setFitWidth(166);
        view.setImage(img);
        startButton.setGraphic(view);
        startButton.setOnMouseEntered(e -> startButton.setEffect(shadow));
        startButton.setOnMouseExited(e -> startButton.setEffect(null));
        startButton.setOnAction(event -> {
            gameState = STATE.SINGLEPLAYER;
            gameBomber.AI = false;
            single(stage);
        });

        AIButton.setStyle("-fx-background-color: transparent; ");
        AIButton.setPrefSize(166, 66);
        AIButton.setTranslateX(Sprite.MENU_SIZE * 30 / 2 - 166 / 2);
        AIButton.setTranslateY(Sprite.MENU_SIZE * 15 / 2 + 66 / 2 + 20);
        try {
            stream = new FileInputStream("res/button_menu/AI_button.png");
        } catch (Exception e) {
            e.getMessage();
        }
        img = new Image(stream);
        view = new ImageView();
        view.setFitHeight(66);
        view.setFitWidth(166);
        view.setImage(img);
        AIButton.setGraphic(view);
        AIButton.setOnMouseEntered(e -> AIButton.setEffect(shadow));
        AIButton.setOnMouseExited(e -> AIButton.setEffect(null));
        AIButton.setOnAction(event -> {
            gameState = STATE.SINGLEPLAYER;
            gameBomber.AI = true;
            single(stage);
        });

        exitButton.setStyle("-fx-background-color: transparent; ");
        exitButton.setPrefSize(166, 66);
        exitButton.setTranslateX(Sprite.MENU_SIZE * 30 / 2 - 166 / 2);
        exitButton.setTranslateY(Sprite.MENU_SIZE * 15 / 2 + 66 / 2 + 20 + 60 + 20);
        try {
            stream = new FileInputStream("res/button_menu/exit.png");
        } catch (Exception e) {
            e.getMessage();
        }
        img = new Image(stream);
        view = new ImageView();
        view.setFitHeight(66);
        view.setFitWidth(166);
        view.setImage(img);
        exitButton.setGraphic(view);
        exitButton.setOnMouseEntered(e -> exitButton.setEffect(shadow));
        exitButton.setOnMouseExited(e -> exitButton.setEffect(null));
        exitButton.setOnAction(event -> {
            System.exit(0);
        });
        
        try {
            stream = new FileInputStream("res/button_menu/menu.jpeg");
        } catch (Exception e) {
            e.getMessage();
        }
        Image image = new Image(stream);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitWidth(30 * Sprite.MENU_SIZE);
        imageView.setFitHeight(15 * Sprite.MENU_SIZE);
        menuRoot.getChildren().add(imageView);
        menuRoot.getChildren().addAll(startButton, AIButton, exitButton);
        Scene scene = new Scene(menuRoot, Sprite.MENU_SIZE * 30, Sprite.MENU_SIZE * 15, Color.BLACK);
        stage.setTitle("BOMBERMAN");
        stage.setScene(scene);
        stage.show();
    }

    public enum STATE {
        MENU,
        SINGLEPLAYER,
        MULTIPLAYER,
        PAUSE,
        END,
        NEXT_LEVEL,
        EXIT;
    }

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
        System.out.println(BombermanGame.gameBomber.getTileX() + " " + BombermanGame.gameBomber.getTileY());
    }
}
