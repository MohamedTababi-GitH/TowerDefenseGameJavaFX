package org.example.javatowerdefensegame;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBoard extends Pane {
    private int gold = 5;
    private int health = 10;
    Label goldLabel;
    Label healthLabel;
    private Label waveLabel;
    private boolean gameOver = false;
    private char[][] tilemap;
    private double tileSize;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();

    private List<Tower> towers = new ArrayList<>();
    private ImageView heartIcon;
    private ImageView coinIcon;
    Image zombieIMG = new Image("Assets/zombie.png");
    Image coinImage = new Image("Assets/gold_coin.png");

    Image heartImage = new Image("Assets/heart_ic.png");

    Image veggiesCart = new Image("Assets/veggies_cart.png");
    Image redMoon = new Image("Assets/RedMoonTower.png");
    Image archerTower = new Image("Assets/Archer.png");
    Image woodTower = new Image("Assets/woodTower.png");
    Image zombieWalk = new Image("Assets/zombie_walk.png");
    Image grassImage = new Image("Assets/grass.png");
    Image slotImage = new Image("Assets/Circular_Brick.png");
    Image citySignImage = new Image("Assets/city_sign.png");
    Image pathImage = new Image("Assets/path.png");
    Image bomb = new Image("Assets/bomb.png");
    Image tomato = new Image("Assets/tomato.png");
    Image rocks = new Image("Assets/rocks.png");
    Image spark = new Image("Assets/spark.png");
    Image worm = new Image("Assets/fireWorm.png");
    Image wormWalk = new Image("Assets/fireWormWalk.png");
    Image bringer = new Image("Assets/Bringer-of-Death_Walk_1.png");
    Image bringerWalk = new Image("Assets/Bringer-of-Death_Walk_2.png");
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

        // set position the heart icon and health label
        heartIcon.setLayoutX(10);
        heartIcon.setLayoutY(10);

        healthLabel.setLayoutX(60);
        healthLabel.setLayoutY(20);
        healthLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

      // Set coin icon position
        coinIcon.setLayoutX(34);
        coinIcon.setLayoutY(60);

        goldLabel.setLayoutX(62);   // Set X position of the gold label, to the right of the coin icon
        goldLabel.setLayoutY(52);   // Align it with the coin icon
        goldLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");


        waveLabel = new Label("Wave 1");
        waveLabel.setLayoutX(380); // Center horizontally
        waveLabel.setLayoutY(10); // Position it at the top

// Font settings
        waveLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));

// Text color and shadow for a distressed look
        waveLabel.setTextFill(Color.DARKGREEN);
        waveLabel.setEffect(new DropShadow(5, Color.BLACK)); // Adds a shadow effect for a rugged appearance

// Background to enhance the apocalyptic theme
        waveLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 10px; -fx-background-radius: 5px;");


        // Add the heart, health, coin, and gold elements directly to the game board
        getChildren().addAll(heartIcon, healthLabel, coinIcon, goldLabel, waveLabel);
    }
    private Timeline startTimeline;
    private Timeline secondWaveTimeline;
    private Timeline lastTimeLine;

    private void startGame() {
        // Add 3-second pause before starting the first wave
        PauseTransition initialPause = new PauseTransition(Duration.seconds(0));
        initialPause.setOnFinished(event -> {
            // Start the first wave after the initial pause
            startTimeline = new Timeline(new KeyFrame(Duration.seconds(1.5), event1 -> spawnFirstWave()));
            startTimeline.setCycleCount(14);
            startTimeline.play();

            // Move to the next wave after the first wave ends
            startTimeline.setOnFinished(event2 -> {
                // Add 3-second pause before starting Wave 2
                PauseTransition pause1 = new PauseTransition(Duration.seconds(10));
                pause1.setOnFinished(event3 -> {
                    waveLabel.setText("Wave 2");
                    waveLabel.setTextFill(Color.ORANGE);

                    // Start second wave after the pause
                    secondWaveTimeline = new Timeline(new KeyFrame(Duration.seconds(1.5), event4 -> spawnSecondWave()));
                    secondWaveTimeline.setCycleCount(12);
                    secondWaveTimeline.play();

                    // Move to the next wave after the second wave ends
                    secondWaveTimeline.setOnFinished(event5 -> {
                        // Add 3-second pause before starting Wave 3
                        PauseTransition pause2 = new PauseTransition(Duration.seconds(10));
                        pause2.setOnFinished(event6 -> {
                            waveLabel.setText("Wave 3");
                            waveLabel.setTextFill(Color.RED);

                            // Start third wave after the pause
                            lastTimeLine = new Timeline(new KeyFrame(Duration.seconds(1.5), event7 -> spawnThirdWave()));
                            lastTimeLine.setCycleCount(20);
                            lastTimeLine.play();
                        });
                        pause2.play();
                    });
                });
                pause1.play();
            });
        });
        initialPause.play();

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
        List<Projectile> toRemove = new ArrayList<>(); // List to track projectiles to remove

        for (Tower tower : towers) {
            tower.update(elapsedTime, enemies, projectiles, this.getWidth(), this.getHeight());

            // Check each projectile and add it to the game board if it's visible
            for (Projectile projectile : new ArrayList<>(projectiles)) { // Use a copy to avoid concurrent modification
                // Check if the projectile is visible and not already added to the scene
                if (projectile.getImageView().isVisible() && !getChildren().contains(projectile.getImageView())) {
                    getChildren().add(projectile.getImageView());
                }

                // Check if the projectile is out of bounds or has hit a target
                if (projectile.hasHitTarget() || !projectile.getImageView().isVisible()) {
                    toRemove.add(projectile);
                }
            }
        }

        // Remove projectiles that are no longer needed
        for (Projectile projectile : toRemove) {
            getChildren().remove(projectile.getImageView());
            projectiles.remove(projectile); // Remove from the projectiles list
        }
    }


    private void updateProjectiles() {
        // If the game is over, remove all projectiles and return
        if (gameOver) {
            for (Projectile projectile : projectiles) {
                getChildren().remove(projectile.getImageView());
            }
            projectiles.clear();
            return;
        }

        List<Projectile> toRemoveProjectiles = new ArrayList<>();
        List<Enemy> hitEnemies = new ArrayList<>();

        for (Projectile projectile : new ArrayList<>(projectiles)) { // Use a copy to avoid concurrent modification
            if (!gameOver) {
                projectile.move();
            }

            // Check collision with enemies
            for (Enemy enemy : new ArrayList<>(enemies)) { // Use a copy to avoid concurrent modification
                if (projectile.getBounds().intersects(enemy.getBoundsInParent())) {
                    int damage = projectile.getDamage(); // Get the damage value from the projectile
                    enemy.setHp(enemy.getHp() - damage); // Apply the damage to the enemy

                    // If the enemy's HP is 0 or less, prepare it for removal
                    if (enemy.getHp() <= 0) {
                        gold += enemy.getGoldReward(); // Add gold based on enemy type
                        goldLabel.setText(String.valueOf(gold));
                        hitEnemies.add(enemy); // Mark enemy for removal
                    }

                    // Set the projectile's ImageView to invisible and mark for removal
                    projectile.getImageView().setVisible(false);
                    toRemoveProjectiles.add(projectile); // Mark projectile for removal
                    break; // Break after the first collision to avoid multiple hits
                }
            }
        }

        // Remove hit enemies and projectiles after the loop
        for (Enemy enemy : hitEnemies) {
            getChildren().remove(enemy);
            enemies.remove(enemy);
        }
        projectiles.removeAll(toRemoveProjectiles);
    }



    private void updateEnemies() {
        List<Enemy> toRemove = new ArrayList<>();
        for (Enemy enemy : enemies) {
            enemy.move();
            if (enemy.hasReachedEnd()) {
                health--;
                healthLabel.setText(String.valueOf(health));
                flickerHealthLabel();
                toRemove.add(enemy); // Mark enemy for removal if it has reached the end
            }
        }
        enemies.removeAll(toRemove); // Remove enemies that have reached the end
        getChildren().removeAll(toRemove); // Remove from the Pane

        // Check if health has reached 0, and trigger game over
        if (health <= 0) {
            gameOver = true;
            endGame(); // Call the method to display the game-over screen
        }
    }
    private void flickerHealthLabel() {
        Color originalColor = (Color) healthLabel.getTextFill();
        Color redColor = Color.RED;

        Timeline flickerTimeline = new Timeline(
                new KeyFrame(Duration.millis(0), e -> healthLabel.setTextFill(redColor)),
                new KeyFrame(Duration.millis(100), e -> healthLabel.setTextFill(originalColor))
        );
        flickerTimeline.setCycleCount(2); // Flicker twice
        flickerTimeline.play();
    }


    private void endGame() {
        // Stop the game (stop enemy spawning and animations)
        if (startTimeline != null && startTimeline.getStatus() == Timeline.Status.RUNNING) {
            startTimeline.stop();
        }
        if (secondWaveTimeline != null && secondWaveTimeline.getStatus() == Timeline.Status.RUNNING) {
            secondWaveTimeline.stop();
        }
        if (lastTimeLine != null && lastTimeLine.getStatus() == Timeline.Status.RUNNING) {
            lastTimeLine.stop();
        }

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


    private void spawnFirstWave() {


            // Define the spawn position or path for the Zombies
            double[][] path = {
                    {0 * tileSize, 8 * tileSize}, {3.7 * tileSize, 8 * tileSize},
                    {3.7 * tileSize, 2 * tileSize}, {12.7 * tileSize, 2* tileSize},
                    {12.7 * tileSize, 9 * tileSize}, {6.7 * tileSize, 9 * tileSize},
                    {6.7 * tileSize, 13 * tileSize}, {15.7 * tileSize, 13 * tileSize},
                    {15.7 * tileSize, 8 * tileSize},{21 * tileSize, 8 * tileSize}
            };

            Enemy zombie = new Enemy(0, 8 * tileSize, 0.6, path, zombieIMG, zombieWalk, 30,4);
            enemies.add(zombie);
            getChildren().add(zombie);

    }


    private void spawnSecondWave() {

        // Define the spawn position or path for the Zombies
        double[][] path = {
                {0 * tileSize, 8 * tileSize}, {2.7 * tileSize, 8 * tileSize},
                {2.7 * tileSize, 2 * tileSize}, {11.7 * tileSize, 2* tileSize},
                {11.7 * tileSize, 9 * tileSize}, {5.7 * tileSize, 9 * tileSize},
                {5.7 * tileSize, 13 * tileSize}, {14.7 * tileSize, 13 * tileSize},
                {14.7 * tileSize, 8 * tileSize},{21 * tileSize, 8 * tileSize}
        };

        Enemy zombie = new Enemy(0, 8 * tileSize, 1.4, path,worm, wormWalk,80,10);
        zombie.resize(120,120);
        enemies.add(zombie);
        getChildren().add(zombie);

    }


    private void spawnThirdWave() {
        // Define the spawn position or path for the Zombies
        double[][] path = {
                {0 * tileSize, 7.5 * tileSize}, {3.2 * tileSize, 7.5 * tileSize},
                {3.2 * tileSize, 1.5 * tileSize}, {12.2 * tileSize, 1.5* tileSize},
                {12.2 * tileSize, 8.5 * tileSize}, {6.2 * tileSize, 8.5 * tileSize},
                {6.2 * tileSize, 12.5 * tileSize}, {15.2 * tileSize, 12.5 * tileSize},
                {15.2 * tileSize, 7.5 * tileSize},{21 * tileSize, 7.5 * tileSize}
        };

        // Create the enemy
        Enemy zombie = new Enemy(0, 7.5 * tileSize, 1.1, path,bringer, bringerWalk,300,40);

        // Resize the enemy to make it bigger
        zombie.resize(96, 96); // Adjust the size as needed

        // Add the enemy to the list and the game board
        enemies.add(zombie);
        getChildren().add(zombie);
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
                    case 'X':
                        tileImageView.setImage(citySignImage);
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
        towerSelectionStage.getIcons().add(woodTower);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(50); // Increased horizontal gap between cells
        gridPane.setVgap(50); // Increased vertical gap between cells
        gridPane.setStyle("-fx-padding: 20; -fx-background-color: #739900; -fx-alignment: center;"); // Adjusted padding


        // Tower 1: Veggies Cart
        VBox tower1Box = new VBox(10);
        tower1Box.setStyle("-fx-alignment: center; -fx-background-color: #004d00; -fx-border-color: #fff; -fx-border-width: 2px; -fx-padding: 10;");

        ImageView tower1ImageView = new ImageView(veggiesCart);
        tower1ImageView.setFitWidth(100); // Adjust image size as needed
        tower1ImageView.setFitHeight(100);
        tower1ImageView.setPreserveRatio(true);

        Label tower1NameLabel = new Label("Vegetables");
        tower1NameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Label tower1PriceLabel = new Label("Price: 5 Gold");
        tower1PriceLabel.setStyle("-fx-text-fill: white;");

        tower1ImageView.setOnMouseClicked(e -> {
            if (gold >= 5) { // Check if player has enough gold
                placeTower(row, col, "Veggies Cart", veggiesCart, 2, 10,tomato); // Customize fireRate, damage
                gold -= 5; // Deduct the gold for this tower
                goldLabel.setText(String.valueOf(gold)); // Update the gold display
                towerSelectionStage.close(); // Close the selection window
            } else {
                System.out.println("Not enough gold!");
            }
        });

        tower1Box.getChildren().addAll(tower1NameLabel, tower1ImageView, tower1PriceLabel);
        gridPane.add(tower1Box, 0, 0); // Add to cell (0, 0)

        // Tower 2: Tower 2 (Red)
        VBox tower2Box = new VBox(10);
        tower2Box.setStyle("-fx-alignment: center; -fx-background-color: #004d00; -fx-border-color: #fff; -fx-border-width: 2px; -fx-padding: 10;");

        ImageView tower2ImageView = new ImageView(archerTower);
        tower2ImageView.setFitWidth(100);
        tower2ImageView.setFitHeight(100);
        tower2ImageView.setPreserveRatio(true);

        Label tower2NameLabel = new Label("Bomb Tower");
        tower2NameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Label tower2PriceLabel = new Label("Price: 50 Gold");
        tower2PriceLabel.setStyle("-fx-text-fill: white;");

        tower2ImageView.setOnMouseClicked(e -> {
            if (gold >= 50) { // Check if player has enough gold
                placeTower(row, col, "Tower 2 (Red)", archerTower, 2, 30, bomb);
                gold -= 50;
                goldLabel.setText(String.valueOf(gold));
                towerSelectionStage.close();
            } else {
                System.out.println("Not enough gold!");
            }
        });

        tower2Box.getChildren().addAll(tower2NameLabel, tower2ImageView, tower2PriceLabel);
        gridPane.add(tower2Box, 0, 1); // Add to cell (0, 1)

        // Tower 3: Tower 3 (Green)
        VBox tower3Box = new VBox(10);
        tower3Box.setStyle("-fx-alignment: center; -fx-background-color: #004d00; -fx-border-color: #fff; -fx-border-width: 2px; -fx-padding: 10;");

        ImageView tower3ImageView = new ImageView(woodTower);
        tower3ImageView.setFitWidth(100);
        tower3ImageView.setFitHeight(100);
        tower3ImageView.setPreserveRatio(true);

        Label tower3NameLabel = new Label("Wooden Tower");
        tower3NameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Label tower3PriceLabel = new Label("Price: 20 Gold");
        tower3PriceLabel.setStyle("-fx-text-fill: white;");

        tower3ImageView.setOnMouseClicked(e -> {
            if (gold >= 20) { // Check if player has enough gold
                placeTower(row, col, "Tower 3 (Green)", woodTower, 2, 20,rocks);
                gold -= 20; // Deduct the gold for this tower
                goldLabel.setText(String.valueOf(gold)); // Update the gold display
                towerSelectionStage.close(); // Close the selection window
            } else {
                System.out.println("Not enough gold!");
            }
        });

        tower3Box.getChildren().addAll(tower3NameLabel, tower3ImageView, tower3PriceLabel);
        gridPane.add(tower3Box, 1, 0); // Add to cell (1, 0)

        // Tower 4: Tower 4 (Blue)
        VBox tower4Box = new VBox(10);
        tower4Box.setStyle("-fx-alignment: center; -fx-background-color: #004d00; -fx-border-color: #fff; -fx-border-width: 2px; -fx-padding: 10;");

        ImageView tower4ImageView = new ImageView(redMoon);
        tower4ImageView.setFitWidth(100);
        tower4ImageView.setFitHeight(100);
        tower4ImageView.setPreserveRatio(true);

        Label tower4NameLabel = new Label("Red Moon");
        tower4NameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Label tower4PriceLabel = new Label("Price: 100 Gold");
        tower4PriceLabel.setStyle("-fx-text-fill: white;");

        tower4ImageView.setOnMouseClicked(e -> {
            if (gold >= 100) { // Check if player has enough gold
                placeTower(row, col, "Tower 4 (Blue)", redMoon, 2, 50,spark);
                gold -= 100; // Deduct the gold for this tower
                goldLabel.setText(String.valueOf(gold)); // Update the gold display
                towerSelectionStage.close(); // Close the selection window
            } else {
                System.out.println("Not enough gold!");
            }
        });

        tower4Box.getChildren().addAll(tower4NameLabel, tower4ImageView, tower4PriceLabel);
        gridPane.add(tower4Box, 1, 1); // Add to cell (1, 1)

        // Create the scene and show the selection window
        Scene scene = new Scene(gridPane, 500, 500); // Adjust scene size as necessary
        towerSelectionStage.setScene(scene);
        towerSelectionStage.showAndWait();
    }



    private void placeTower(int row, int col, String type, Image img, double fireRate, int damage, Image projectileImg) {
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
        Tower tower = new Tower(col * tileSize, row * tileSize, tileSize, type, img, projectileImg, fireRate, damage);

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
