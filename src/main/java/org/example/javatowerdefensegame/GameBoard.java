package org.example.javatowerdefensegame;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends Pane {
    private int score = 0;
    Label scoreLabel = new Label("Score: " + score);
    private char[][] tilemap;
    private double tileSize;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private Timeline spawnTimeline;
    private List<Tower> towers = new ArrayList<>();


    Image grassImage = new Image("C:\\Users\\Public\\Documents\\JavaTowerDefenseGame\\src\\main\\java\\org\\example\\javatowerdefensegame\\newGrass2.png");
    Image slotImage = new Image("C:\\Users\\Public\\Documents\\JavaTowerDefenseGame\\src\\main\\java\\org\\example\\javatowerdefensegame\\slot1.png");
    Image pathImage = new Image("C:\\Users\\Public\\Documents\\JavaTowerDefenseGame\\src\\main\\java\\org\\example\\javatowerdefensegame\\path1.png");
    public GameBoard(char[][] tilemap, double tileSize) {
        this.tilemap = tilemap;
        this.tileSize = tileSize;
        initialize();
    }

    private void initialize() {
        drawTilemap();
        startGame();
        scoreLabel.setLayoutX(10);
        scoreLabel.setLayoutY(10);
        scoreLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        getChildren().add(scoreLabel);
    }

    private void startGame() {
        spawnTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> spawnEnemy()));
        spawnTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        spawnTimeline.play(); // Start spawning enemies

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateEnemies();
                updateProjectiles();
                updateTowers();
            }
        }.start();
    }

    private void updateTowers() {
        double elapsedTime = 0.016; // Assuming 60 FPS, so ~16ms per frame
        for (Tower tower : towers) {
            tower.update(elapsedTime, enemies, projectiles, this.getWidth(), this.getHeight());
            // Add newly created projectiles to the game board
            for (Projectile projectile : projectiles) {
                if (!getChildren().contains(projectile)) {
                    getChildren().add(projectile);
                }
            }
        }
    }

    private void updateProjectiles() {
        List<Projectile> toRemove = new ArrayList<>();
        List<Enemy> hitEnemies = new ArrayList<>();

        for (Projectile projectile : projectiles) {
            projectile.move();

            // Check collision with enemies
            for (Enemy enemy : enemies) {
                if (projectile.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    score++;
                    scoreLabel.setText("Score: " + score);
                    toRemove.add(projectile);
                    hitEnemies.add(enemy);
                    break; // Break after the first collision to avoid multiple hits
                }
            }
        }

        // Remove projectiles and hit enemies
        projectiles.removeAll(toRemove);
        getChildren().removeAll(toRemove);

        enemies.removeAll(hitEnemies);
        getChildren().removeAll(hitEnemies);
    }

    private void updateEnemies() {
        List<Enemy> toRemove = new ArrayList<>();
        for (Enemy enemy : enemies) {
            enemy.move();
            if (enemy.hasReachedEnd()) {
                toRemove.add(enemy); // Mark enemy for removal if it has reached the end
            }
        }
        enemies.removeAll(toRemove); // Remove enemies that have reached the end
        getChildren().removeAll(toRemove); // Remove from the Pane
    }

    private void spawnEnemy() {
        // Define a hardcoded path
        double[][] path = {
                // Start from (4, 3)
                {0 * tileSize, 11 * tileSize}, {4 * tileSize, 11 * tileSize},
                {4 * tileSize, 3 * tileSize}, {7 * tileSize, 3 * tileSize},// Move down to (7, 4)
                {7 * tileSize, 13 * tileSize}, {12 * tileSize, 13 * tileSize},
                {12 * tileSize, 13 * tileSize},
                {12 * tileSize, 10 * tileSize}, {21 * tileSize, 10 * tileSize}
        };

    Enemy enemy = new Enemy(0, 11 * tileSize, 1, path);
        enemies.add(enemy);
        getChildren().add(enemy);
    }

    private void drawTilemap() {


        for (int row = 0; row < tilemap.length; row++) {
            for (int col = 0; col < tilemap[row].length; col++) {
                ImageView tileImageView = new ImageView();
                tileImageView.setFitWidth(tileSize);
                tileImageView.setFitHeight(tileSize);
                tileImageView.setPreserveRatio(true);

                switch (tilemap[row][col]) {
                    case 'G':
                        tileImageView.setImage(grassImage);
                        break;
                    case 'S':
                        tileImageView.setImage(slotImage);
                        int finalRow = row;
                        int finalCol = col;
                        tileImageView.setOnMouseClicked(e -> handleSlotClick(finalRow, finalCol));
                        break;
                    case 'P':
                        tileImageView.setImage(pathImage);
                        break;
                }

                tileImageView.setX(col * tileSize);
                tileImageView.setY(row * tileSize);
                getChildren().add(tileImageView);
            }
        }
    }

    private void handleSlotClick(int row, int col) {
        // Handle slot click and add tower
        System.out.println("clicked");
        // Create and show the tower selection window
        Stage towerSelectionStage = new Stage();
        towerSelectionStage.initModality(Modality.APPLICATION_MODAL);
        towerSelectionStage.setTitle("Select Tower");
        towerSelectionStage.setWidth(500);
        towerSelectionStage.setHeight(500);
        VBox vbox = new VBox(10); // VBox with 10 pixels spacing between buttons
        vbox.setStyle("-fx-padding: 10;"); // Add padding around the VBox

        Button tower1Button = new Button("Tower 1 (Blue)");
        Button tower2Button = new Button("Tower 2 (Yellow)");

        tower1Button.setOnAction(e -> {
            placeTower(row, col, "fast", Color.BLUE, 0.5, 10);
            towerSelectionStage.close();
        });

        tower2Button.setOnAction(e -> {
            placeTower(row, col, "heavy", Color.YELLOW, 1.5, 20);
            towerSelectionStage.close();
        });
        vbox.getChildren().addAll(tower1Button, tower2Button);

        Scene scene = new Scene(vbox, 200, 150); // Set the scene size
        towerSelectionStage.setScene(scene);
        towerSelectionStage.showAndWait();
    }

    private void placeTower(int row, int col, String type, Color color, double fireRate, double damage) {
        // Create and place the new tower
        Tower tower = new Tower(col * tileSize, row * tileSize, tileSize, type, color, fireRate, damage);
        towers.add(tower);
        getChildren().add(tower);
    }
}
