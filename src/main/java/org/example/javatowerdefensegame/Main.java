package org.example.javatowerdefensegame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Set custom icon
        Image icon = new Image("Assets/zombie.png");
        primaryStage.getIcons().add(icon);

        // Create the Start Menu
        StackPane startMenu = new StackPane();
        startMenu.setStyle("-fx-background-color: #556B2F;"); // Zombie green background

        // Create the Start Game button
        Button startButton = new Button("Start Game");
        startButton.setFont(Font.font("Arial", 30));
        startButton.setTextFill(Color.WHITE);
        startButton.setStyle("-fx-background-color: #2E8B57; -fx-border-color: #000000; -fx-border-width: 2px;");

        // Add drop shadow for a 3D effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        startButton.setEffect(shadow);

        // Change button style on hover
        startButton.setOnMouseEntered(event -> {
            startButton.setStyle("-fx-background-color: #3CB371; -fx-border-color: #000000; -fx-border-width: 2px;");
            startButton.setTextFill(Color.BLACK);
        });
        startButton.setOnMouseExited(event -> {
            startButton.setStyle("-fx-background-color: #2E8B57; -fx-border-color: #000000; -fx-border-width: 2px;");
            startButton.setTextFill(Color.WHITE);
        });

        // Align the button to the center of the start menu
        startMenu.getChildren().add(startButton);
        StackPane.setAlignment(startButton, Pos.CENTER);

        Scene startMenuScene = new Scene(startMenu, 880, 760);
        primaryStage.setTitle("Doomsday-Z");
        primaryStage.setResizable(false);
        primaryStage.setScene(startMenuScene);
        primaryStage.show();

        // Event handler for Start Game button
        startButton.setOnAction(event -> {
            try {
                // Load the game map and setup the game scene
                char[][] tilemap = TilemapLoader.loadTilemap("C:\\Users\\Public\\Documents\\JavaTowerDefenseGame\\src\\main\\resources\\map.csv");
                GameBoard gameBoard = new GameBoard(tilemap, 40); // Adjust tile size as needed

                StackPane gameRoot = new StackPane();
                gameRoot.getChildren().add(gameBoard);

                Scene gameScene = new Scene(gameRoot, 880, 760); // Adjust scene size as needed
                primaryStage.setScene(gameScene);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
