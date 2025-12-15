package com.example.battleship.view;

// GameUI.java - Main user interface

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import com.example.battleship.controller.GameController;
import com.example.battleship.model.GameState;
import com.example.battleship.model.Position;
import com.example.battleship.model.Ship;
import com.example.battleship.model.Board;  // ADD THIS IMPORT
import com.example.battleship.exceptions.InvalidShotException;

/**
 * Main user interface for the Battleship game.
 * Follows SOLID's Dependency Inversion Principle - depends on abstractions.
 * Implements all visual components and event handling.
 */
public class GameUI {

    private GameController gameController;
    private BorderPane mainLayout;
    private BoardView playerBoardView;
    private BoardView computerBoardView;
    private Label statusLabel;
    private Button startButton;
    private Button showComputerBoardButton;
    private VBox shipPlacementPanel;
    private boolean showingComputerBoard = false;

    public GameUI(GameController gameController) {
        this.gameController = gameController;
        this.mainLayout = new BorderPane();
        initializeUI();
    }

    /**
     * Initialize the main UI components
     */
    private void initializeUI() {
        // Create top menu bar
        MenuBar menuBar = createMenuBar();

        // Create status panel
        HBox statusPanel = createStatusPanel();

        // Create game area
        HBox gameArea = createGameArea();

        // Create control panel
        VBox controlPanel = createControlPanel();

        // Assemble main layout
        mainLayout.setTop(menuBar);
        mainLayout.setCenter(gameArea);
        mainLayout.setBottom(statusPanel);
        mainLayout.setRight(controlPanel);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #1a237e, #283593);");
    }

    /**
     * Create menu bar
     */
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newGameItem = new MenuItem("New Game");
        MenuItem saveGameItem = new MenuItem("Save Game");
        MenuItem loadGameItem = new MenuItem("Load Game");
        MenuItem exitItem = new MenuItem("Exit");

        newGameItem.setOnAction(e -> gameController.resetGame());
        saveGameItem.setOnAction(e -> gameController.saveGame());
        loadGameItem.setOnAction(e -> gameController.loadGame());
        exitItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(newGameItem, saveGameItem, loadGameItem,
                new SeparatorMenuItem(), exitItem);

        Menu viewMenu = new Menu("View");
        CheckMenuItem showComputerBoardItem = new CheckMenuItem("Show Computer Board");
        showComputerBoardItem.setOnAction(e -> toggleComputerBoard());

        viewMenu.getItems().addAll(showComputerBoardItem);

        menuBar.getMenus().addAll(fileMenu, viewMenu);
        return menuBar;
    }

    /**
     * Create status panel
     */
    private HBox createStatusPanel() {
        HBox statusPanel = new HBox(10);
        statusPanel.setPadding(new Insets(10));
        statusPanel.setAlignment(Pos.CENTER);
        statusPanel.setStyle("-fx-background-color: #3949ab; -fx-background-radius: 5;");

        statusLabel = new Label("Place your ships on the board. Select ship and click on board.");
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

        statusPanel.getChildren().add(statusLabel);
        return statusPanel;
    }

    /**
     * Create game area with both boards
     */
    private HBox createGameArea() {
        HBox gameArea = new HBox(20);
        gameArea.setPadding(new Insets(20));
        gameArea.setAlignment(Pos.CENTER);

        // Create player's board view
        playerBoardView = new BoardView(gameController.getGameState().getPlayerBoard(), true);
        playerBoardView.setTitle("Your Fleet");

        // Create computer's board view
        computerBoardView = new BoardView(gameController.getGameState().getComputerBoard(), false);
        computerBoardView.setTitle("Enemy Waters");

        // Set up event handlers for computer board
        computerBoardView.setOnCellClick(this::handleComputerBoardClick);

        gameArea.getChildren().addAll(playerBoardView.getView(), computerBoardView.getView());
        return gameArea;
    }

    /**
     * Create control panel
     */
    private VBox createControlPanel() {
        VBox controlPanel = new VBox(10);
        controlPanel.setPadding(new Insets(20));
        controlPanel.setPrefWidth(200);
        controlPanel.setStyle("-fx-background-color: #5c6bc0; -fx-background-radius: 5;");

        // Start button
        startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        startButton.setOnAction(e -> startGame());
        startButton.setDisable(true);

        // Show computer board button
        showComputerBoardButton = new Button("Show Computer Board");
        showComputerBoardButton.setStyle("-fx-font-size: 12px;");
        showComputerBoardButton.setOnAction(e -> toggleComputerBoard());

        // Ship placement panel
        shipPlacementPanel = createShipPlacementPanel();

        controlPanel.getChildren().addAll(
                new Label("Controls"),
                startButton,
                new Separator(),
                showComputerBoardButton,
                new Separator(),
                shipPlacementPanel
        );

        return controlPanel;
    }

    /**
     * Create ship placement panel
     */
    private VBox createShipPlacementPanel() {
        VBox panel = new VBox(5);
        panel.setPadding(new Insets(10));

        Label title = new Label("Ships to Place:");
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");

        panel.getChildren().add(title);

        // Will be populated with ship placement controls
        return panel;
    }

    /**
     * HU-2: Handle click on computer board (firing)
     */
    private void handleComputerBoardClick(Position position) {
        if (!gameController.getGameState().isPlayerTurn() ||
                !gameController.getGameState().isGameStarted()) {
            return;
        }

        try {
            Board.ShotResult result = gameController.playerFire(position);
            updateStatus(result, position);
            computerBoardView.updateCell(position, result);

        } catch (InvalidShotException e) {
            statusLabel.setText("Invalid shot: " + e.getMessage());
        }
    }

    /**
     * Update status based on shot result
     */
    private void updateStatus(Board.ShotResult result, Position position) {
        String message;
        switch (result) {
            case HIT:
                message = "Hit at " + position + "! You get another turn.";
                break;
            case SUNK:
                message = "SUNK! You destroyed an enemy ship!";
                break;
            case MISS:
                message = "Miss at " + position + ". Computer's turn.";
                break;
            default:
                message = "Invalid shot.";
        }
        statusLabel.setText(message);
    }

    /**
     * HU-3: Toggle computer board visibility
     */
    private void toggleComputerBoard() {
        showingComputerBoard = !showingComputerBoard;
        computerBoardView.setRevealShips(showingComputerBoard);

        if (showingComputerBoard) {
            showComputerBoardButton.setText("Hide Computer Board");
            statusLabel.setText("Computer board revealed for verification.");
        } else {
            showComputerBoardButton.setText("Show Computer Board");
            statusLabel.setText("Computer board hidden.");
        }
    }

    /**
     * Start the game
     */
    private void startGame() {
        if (gameController.getShipPlacementController().allShipsPlaced()) {
            gameController.startGame();
            startButton.setDisable(true);
            statusLabel.setText("Game started! Your turn. Click on enemy board to fire.");
        } else {
            statusLabel.setText("Place all ships before starting the game.");
        }
    }

    public BorderPane getMainLayout() {
        return mainLayout;
    }

    public BorderPane createMainLayout() {
        return mainLayout;
    }
}