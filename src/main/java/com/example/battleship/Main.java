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
            //Initialize game state
            GameState gameState = new GameState();

            //Initialize controller
            gameController = new GameController(gameState);

            //Initialize view
            gameUI = new GameUI(gameController);

            //Get the main layout
            Scene scene = new Scene(gameUI.getMainLayout(), 1300, 630);

            //Set up primary stage
            primaryStage.setTitle("Naval Battle Game");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1100);
            primaryStage.setMinHeight(100);
            primaryStage.show();

            //This load saved game if exists
            gameController.loadGame();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Application Error", e.getMessage());
        }
    }

    @Override
    public void stop() {
        //This save game when application closes
        if (gameController != null) {
            gameController.saveGame();
        }
    }

    private void showErrorDialog(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}