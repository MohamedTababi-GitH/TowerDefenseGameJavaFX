package org.example.javatowerdefensegame;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    Image zombieWalk = new Image("C:\\Users\\Public\\Documents\\JavaTowerDefenseGame\\src\\main\\java\\org\\example\\javatowerdefensegame\\zombie_walk.png");
    Image zombie = new Image("C:\\Users\\Public\\Documents\\JavaTowerDefenseGame\\src\\main\\java\\org\\example\\javatowerdefensegame\\zombie.png");
    private Pane gamePane;
    private List<Enemy> enemies = new ArrayList<>();
    private static final double TILE_SIZE = 40; // Size of each tile in pixels

    // Hardcoded path coordinates (in pixels)
    private static final double[][] PATH = {
            {0, 9 * TILE_SIZE}, {TILE_SIZE, 9 * TILE_SIZE}, {2 * TILE_SIZE, 9 * TILE_SIZE},
            {3 * TILE_SIZE, 9 * TILE_SIZE}, {4 * TILE_SIZE, 9 * TILE_SIZE}, {5 * TILE_SIZE, 9 * TILE_SIZE},
            {6 * TILE_SIZE, 9 * TILE_SIZE}, {7 * TILE_SIZE, 9 * TILE_SIZE}, {8 * TILE_SIZE, 9 * TILE_SIZE},
            {9 * TILE_SIZE, 9 * TILE_SIZE}, {10 * TILE_SIZE, 9 * TILE_SIZE}, {11 * TILE_SIZE, 9 * TILE_SIZE},
            {12 * TILE_SIZE, 9 * TILE_SIZE}, {13 * TILE_SIZE, 9 * TILE_SIZE}, {14 * TILE_SIZE, 9 * TILE_SIZE},
            {15 * TILE_SIZE, 9 * TILE_SIZE}, {16 * TILE_SIZE, 9 * TILE_SIZE}, {17 * TILE_SIZE, 9 * TILE_SIZE},
            {18 * TILE_SIZE, 9 * TILE_SIZE}, {19 * TILE_SIZE, 9 * TILE_SIZE}
    };

    public GameController(Pane gamePane) {
        this.gamePane = gamePane;
        startGame();
    }

    private void startGame() {
        // Initialize enemies and add them to the pane
        Enemy enemy = new Enemy(0, 9 * TILE_SIZE, 2.0, PATH,zombie,zombieWalk);
        enemies.add(enemy);
        gamePane.getChildren().add(enemy);

        // Start the game loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateEnemies();
            }
        }.start();
    }

    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.move();
            // Optionally, check if the enemy has reached the end and handle game over
        }
    }
}

