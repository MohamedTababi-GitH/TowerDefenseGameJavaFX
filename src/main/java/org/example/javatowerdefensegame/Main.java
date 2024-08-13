package org.example.javatowerdefensegame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {

            char[][] tilemap = TilemapLoader.loadTilemap("C:\\Users\\Public\\Documents\\JavaTowerDefenseGame\\src\\main\\resources\\map.csv");
            GameBoard gameBoard = new GameBoard(tilemap, 40); // Adjust tile size as needed

            BorderPane root = new BorderPane();
            root.setCenter(gameBoard);

            Scene scene = new Scene(root, 800, 760); // Adjust scene size as needed
            primaryStage.setTitle("Tower Defense");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

