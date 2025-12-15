package com.example.battleship;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.battleship.view.GameUI;
import com.example.battleship.controller.GameController;
import com.example.battleship.model.GameState;

/**
 * Main application class for Battleship game.
 * This class initializes the JavaFX application and sets up the primary stage.
 * HU-1, HU-2, HU-3, HU-4, HU-5: All user stories are orchestrated from here
 */
public class Main extends Application {

    private GameController gameController;
    private GameUI gameUI;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize game state
            GameState gameState = new GameState();

            // Initialize controller
            gameController = new GameController(gameState);

            // Initialize view
            gameUI = new GameUI(gameController);

            // Create scene
            Scene scene = new Scene(gameUI.createMainLayout(), 1000, 600);

            // Set up primary stage
            primaryStage.setTitle("Battleship - Naval Warfare");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(700);
            primaryStage.show();

            // Load saved game if exists (HU-5)
            gameController.loadGame();

        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Save game on exit (HU-5)
        if (gameController != null) {
            gameController.saveGame();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}