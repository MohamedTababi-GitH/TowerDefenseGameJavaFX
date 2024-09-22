package org.example.javatowerdefensegame;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBoard extends Pane {
    private int gold = 5;
    private int health = 5;
    Label goldLabel;
    Label healthLabel;
    private char[][] tilemap;
    private double tileSize;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private Timeline spawnTimeline;
    private List<Tower> towers = new ArrayList<>();
    private ImageView heartIcon;
    private ImageView coinIcon;
    Image coinImage = new Image("C:\\Users\\Public\\Documents\\JavaTowerDefenseGame\\src\\main\\resources\\Assets\\gold_coin.png");

    Image heartImage = new Image("C:\\Users\\Public\\Documents\\JavaTowerDefenseGame\\src\\main\\resources\\Assets\\heart_ic.png");

    Image zombie = new Image("C:\\Users\\Public\\Documents\\JavaTowerDefenseGame\\src\\main\\java\\org\\example\\javatowerdefensegame\\zombie.png");
    Image veggiesCart = new Image("C:\\Users\\Public\\Documents\\JavaTowerDefenseGame\\src\\main\\resources\\Assets\\veggies_cart.png");
    Image zombieWalk = new Image("C:\\Users\\Public\\Documents\\JavaTowerDefenseGame\\src\\main\\java\\org\\example\\javatowerdefensegame\\zombie_walk.png");
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

        // Load the heart and coin icons
        heartIcon = new ImageView(heartImage);
        heartIcon.setFitWidth(64);
        heartIcon.setFitHeight(64);

        coinIcon = new ImageView(coinImage);
        coinIcon.setFitWidth(20);
        coinIcon.setFitHeight(20);

        // Create labels for gold and health
        goldLabel = new Label(String.valueOf(gold));
        healthLabel = new Label(String.valueOf(health));

        // Manually position the heart icon and health label
        heartIcon.setLayoutX(10);   // Set X position of the heart icon
        heartIcon.setLayoutY(10);   // Set Y position of the heart icon

        healthLabel.setLayoutX(60); // Set X position of the health label, to the right of the heart icon
        healthLabel.setLayoutY(20); // Adjust Y position to vertically align with the heart icon
        healthLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        // Manually position the coin icon and gold label
        coinIcon.setLayoutX(34);    // Set X position of the coin icon
        coinIcon.setLayoutY(60);    // Set Y position of the coin icon

        goldLabel.setLayoutX(62);   // Set X position of the gold label, to the right of the coin icon
        goldLabel.setLayoutY(52);   // Align it with the coin icon
        goldLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        // Add the heart, health, coin, and gold elements directly to the game board
        getChildren().addAll(heartIcon, healthLabel, coinIcon, goldLabel);
    }


    private void startGame() {
        spawnTimeline = new Timeline(new KeyFrame(Duration.seconds(1.5), event -> spawnEnemy()));
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
                    gold++;
                    goldLabel.setText(String.valueOf(gold));
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
                health--;
                healthLabel.setText(String.valueOf(health));
                toRemove.add(enemy); // Mark enemy for removal if it has reached the end
            }
        }
        enemies.removeAll(toRemove); // Remove enemies that have reached the end
        getChildren().removeAll(toRemove); // Remove from the Pane

        // Check if health has reached 0, and trigger game over
        if (health <= 0) {
            endGame(); // Call the method to display the game-over screen
        }
    }

    private void endGame() {
        // Stop the game (stop enemy spawning and animations)
        spawnTimeline.stop();

        // Create a new layout for the game-over screen
        VBox gameOverScreen = new VBox(30); // Increased spacing for more dramatic separation
        gameOverScreen.setStyle("-fx-alignment: center; -fx-background-color: #3C0D0D;"); // Dark red background
        gameOverScreen.setPrefSize(this.getWidth(), this.getHeight()); // Fill entire game board
        gameOverScreen.setAlignment(Pos.CENTER); // Ensure everything in VBox is centered

        // Create a border frame for the "You're Doomed" text
        HBox frame = new HBox();
        frame.setStyle("-fx-padding: 30; -fx-border-color: #FF0000; -fx-border-width: 8px; -fx-background-color: #4B0000;");
        frame.setAlignment(Pos.CENTER); // Center the content of the HBox

        // Create the "You're Doomed" label with a post-apocalyptic, urgent style
        Label gameOverLabel = new Label("You're Doomed");
        gameOverLabel.setStyle("-fx-font-size: 72px; -fx-text-fill: #FFD700; -fx-font-family: 'Impact';");

        // Add a fiery glow for a burning city effect
        DropShadow fireGlow = new DropShadow();
        fireGlow.setColor(Color.ORANGERED);
        fireGlow.setOffsetX(5);
        fireGlow.setOffsetY(5);
        fireGlow.setRadius(30);
        gameOverLabel.setEffect(fireGlow);

        // Add the "You're Doomed" label to the framed HBox
        frame.getChildren().add(gameOverLabel);

        // Create additional post-apocalyptic elements, such as a flickering message
        Label apocalypseMessage = new Label("The apocalypse is upon you...");
        apocalypseMessage.setStyle("-fx-font-size: 24px; -fx-text-fill: #D3D3D3; -fx-font-family: 'Courier New';");
        apocalypseMessage.setEffect(new DropShadow(10, Color.DARKRED));

        // Create a subtle flickering effect using a timeline
        Timeline flicker = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> apocalypseMessage.setVisible(false)),
                new KeyFrame(Duration.seconds(0.8), e -> apocalypseMessage.setVisible(true))
        );
        flicker.setCycleCount(Timeline.INDEFINITE);
        flicker.play();

        // Add the frame and apocalyptic message to the game-over screen layout
        gameOverScreen.getChildren().addAll(frame, apocalypseMessage);

        // Clear the game board and show the game-over screen
        getChildren().clear();
        getChildren().add(gameOverScreen);
    }










    private void spawnEnemy() {
        // Define a hardcoded path
        double[][] path = {
                // Start from (4, 3)
                {0 * tileSize, 11 * tileSize}, {4 * tileSize, 11 * tileSize},
                {4 * tileSize, 3 * tileSize}, {7 * tileSize, 3 * tileSize},// Move down to (7, 4)
                {7 * tileSize, 13 * tileSize}, {12 * tileSize, 13 * tileSize},
                {12 * tileSize, 13 * tileSize},
                {12 * tileSize, 10 * tileSize}, {19 * tileSize, 10 * tileSize}
        };

    Enemy enemy = new Enemy(0, 11 * tileSize, 1, path,zombie,zombieWalk);
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
    // Map to store the towers based on their row and column positions
    private Map<String, ImageView> towerMap = new HashMap<>();

    // Modify the handleSlotClick method to allow changing the tower
    private void handleSlotClick(int row, int col) {
        // Create and show the tower selection window
        Stage towerSelectionStage = new Stage();
        towerSelectionStage.initModality(Modality.APPLICATION_MODAL);
        towerSelectionStage.setTitle("Select Tower");
        towerSelectionStage.setWidth(500);
        towerSelectionStage.setHeight(500);
        VBox vbox = new VBox(10); // VBox with 10 pixels spacing between elements
        vbox.setStyle("-fx-padding: 10; -fx-background-color: #739900; -fx-alignment: center;"); // Add padding and background style

        // Define tower options (you can add more towers with their respective images and properties)
        Image tower1Image = veggiesCart; // Assuming you've already loaded this image
        //Image tower2Image = new Image("\"C:\\\\Users\\\\Public\\\\Documents\\\\JavaTowerDefenseGame\\\\src\\\\main\\\\resources\\\\Assets\\\\veggies_cart.png\""); // Replace with actual image path

        // Tower 1: Veggies Cart
        VBox tower1Box = new VBox(5);
        tower1Box.setLayoutX(15);
        tower1Box.setLayoutY(50);

        ImageView tower1ImageView = new ImageView(tower1Image);
        tower1ImageView.setFitWidth(100); // Adjust image size as needed
        tower1ImageView.setFitHeight(100);
        tower1ImageView.setPreserveRatio(true);

        Label tower1NameLabel = new Label("Veggies Cart");
        tower1NameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Label tower1PriceLabel = new Label("Price: 50 Gold");
        tower1PriceLabel.setStyle("-fx-text-fill: white;");

        // Tower 2: Example Tower
        VBox tower2Box = new VBox(5);
        tower2Box.setStyle("-fx-alignment: center;");

        ImageView tower2ImageView = new ImageView(veggiesCart);
        tower2ImageView.setFitWidth(100);
        tower2ImageView.setFitHeight(100);
        tower2ImageView.setPreserveRatio(true);

        Label tower2NameLabel = new Label("Tower 2 (Yellow)");
        tower2NameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Label tower2PriceLabel = new Label("Price: 75 Gold");
        tower2PriceLabel.setStyle("-fx-text-fill: white;");

        // Handle selection and placement for Tower 1
        tower1ImageView.setOnMouseClicked(e -> {
            if (gold >= 5) { // Check if player has enough gold
                placeTower(row, col, "Veggies Cart", tower1Image, 2, 10); // Customize fireRate, damage
                gold -= 5; // Deduct the gold for this tower
                goldLabel.setText(String.valueOf(gold)); // Update the gold display
                towerSelectionStage.close(); // Close the selection window
            } else {
                System.out.println("Not enough gold!");
            }
        });

        // Handle selection and placement for Tower 2
        tower2ImageView.setOnMouseClicked(e -> {
            if (gold >= 75) { // Check if player has enough gold
                placeTower(row, col, "Tower 2 (Yellow)", veggiesCart, 1.5, 15); // Customize fireRate, damage
                gold -= 75; // Deduct the gold for this tower
                goldLabel.setText(String.valueOf(gold)); // Update the gold display
                towerSelectionStage.close();
            } else {
                System.out.println("Not enough gold!");
            }
        });

        // Add the towers to the VBox
        tower1Box.getChildren().addAll(tower1NameLabel, tower1ImageView, tower1PriceLabel);
        tower2Box.getChildren().addAll(tower2NameLabel, tower2ImageView, tower2PriceLabel);

        vbox.getChildren().addAll(tower1Box, tower2Box); // Add both towers to the main VBox

        // Create the scene and show the selection window
        Scene scene = new Scene(vbox, 300, 400); // Adjust scene size as necessary
        towerSelectionStage.setScene(scene);
        towerSelectionStage.showAndWait();
    }



    private void placeTower(int row, int col, String type, Image img, double fireRate, double damage) {
        // Remove the existing tower (if any) from the same slot
        String key = row + "," + col;
        if (towerMap.containsKey(key)) {
            ImageView existingTower = towerMap.get(key);
            getChildren().remove(existingTower);  // Remove the old tower ImageView from the Pane
            towers.removeIf(tower -> tower.getX() == col * tileSize && tower.getY() == row * tileSize);  // Remove the corresponding Tower object from the list
        }

        // Define the size of the tower image (you can adjust this multiplier)
        double towerSizeMultiplier = 2; // Increase this value to make the tower larger
        double towerWidth = tileSize * towerSizeMultiplier;
        double towerHeight = tileSize * towerSizeMultiplier;

        // Replace the slot tile with a grass tile
        ImageView grassTile = new ImageView(grassImage);
        grassTile.setFitWidth(tileSize);
        grassTile.setFitHeight(tileSize);
        grassTile.setPreserveRatio(true);
        grassTile.setX(col * tileSize);
        grassTile.setY(row * tileSize);

        // Add the grass tile to the game board (below the tower)
        getChildren().add(grassTile);

        // Create the new Tower object
        Tower tower = new Tower(col * tileSize, row * tileSize, tileSize, type, img, fireRate, damage);

        // Center the tower image within the tile by adjusting its position
        ImageView towerImageView = tower.getImageView();
        towerImageView.setFitWidth(towerWidth);
        towerImageView.setFitHeight(towerHeight);
        towerImageView.setPreserveRatio(true);
        towerImageView.setX(col * tileSize - (towerWidth - tileSize) / 2);
        towerImageView.setY(row * tileSize - (towerHeight - tileSize) / 2);

        // Add a click event to the tower image to allow changing the tower
        towerImageView.setOnMouseClicked(e -> {
            handleSlotClick(row, col);  // Open the tower selection window again when the tower is clicked
        });

        // Add the tower to the list of towers
        towers.add(tower);

        // Add the ImageView of the tower to the game board
        getChildren().add(towerImageView);

        // Store the new tower in the map
        towerMap.put(key, towerImageView);
    }


}
