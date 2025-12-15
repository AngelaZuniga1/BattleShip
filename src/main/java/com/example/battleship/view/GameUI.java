package com.example.battleship.view;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.example.battleship.controller.GameController;
import com.example.battleship.model.GameState;
import com.example.battleship.model.Position;
import com.example.battleship.model.Ship;
import com.example.battleship.model.Board;
import com.example.battleship.model.Cell;
import com.example.battleship.exceptions.InvalidShotException;

/**
 * Main user interface for the Battleship game.
 */
public class GameUI {

    private GameController gameController;
    private BorderPane mainLayout;
    private BoardView playerBoardView;
    private BoardView computerBoardView;
    private Label statusLabel;
    private Button startButton;
    private Button showComputerBoardButton;
    private Button rotateButton;
    private VBox shipPlacementPanel;
    private Label currentShipLabel;
    private boolean showingComputerBoard = false;
    private ShipView selectedShipView;

    public GameUI(GameController gameController) {
        this.gameController = gameController;
        this.mainLayout = new BorderPane();
        initializeUI();
        setupShipPlacement();
    }

    /**
     * Setup ship placement
     */
    private void setupShipPlacement() {
        // Initialize ship placement panel
        shipPlacementPanel = createShipPlacementPanel();

        // Select first ship by default if available
        if (!shipPlacementPanel.getChildren().isEmpty()) {
            for (var child : shipPlacementPanel.getChildren()) {
                if (child instanceof HBox) {
                    HBox hbox = (HBox) child;
                    Object userData = hbox.getUserData();
                    if (userData instanceof ShipView) {
                        ShipView shipView = (ShipView) userData;
                        if (!shipView.getShip().isPlaced()) {
                            selectShip(shipView);
                            break;
                        }
                    }
                }
            }
        }

        System.out.println("Ship placement setup complete");
    }

    /**
     * Initialize the main UI components
     */
    private void initializeUI() {
        System.out.println("Initializing UI...");

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
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #0a2463, #1e3c72);");

        System.out.println("UI initialization complete");
    }

    /**
     * Create menu bar
     */
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #1e3c72;");

        Menu fileMenu = new Menu("File");
        fileMenu.setStyle("-fx-text-fill: white;");
        MenuItem newGameItem = new MenuItem("New Game");
        MenuItem saveGameItem = new MenuItem("Save Game");
        MenuItem loadGameItem = new MenuItem("Load Game");
        MenuItem exitItem = new MenuItem("Exit");

        newGameItem.setOnAction(e -> {
            gameController.resetGame();
            resetUI();
            statusLabel.setText("New game started! Place your ships.");
        });
        saveGameItem.setOnAction(e -> {
            gameController.saveGame();
            statusLabel.setText("Game saved successfully!");
        });
        loadGameItem.setOnAction(e -> {
            gameController.loadGame();
            statusLabel.setText("Game loaded successfully!");
            updateUI();
        });
        exitItem.setOnAction(e -> {
            gameController.saveGame();
            System.exit(0);
        });

        fileMenu.getItems().addAll(newGameItem, saveGameItem, loadGameItem,
                new SeparatorMenuItem(), exitItem);

        Menu viewMenu = new Menu("View");
        viewMenu.setStyle("-fx-text-fill: white;");
        CheckMenuItem showComputerBoardItem = new CheckMenuItem("Show Computer Board (Teacher Mode)");
        showComputerBoardItem.setOnAction(e -> toggleComputerBoard());
        showComputerBoardItem.setSelected(false);

        viewMenu.getItems().addAll(showComputerBoardItem);

        Menu helpMenu = new Menu("Help");
        helpMenu.setStyle("-fx-text-fill: white;");
        MenuItem rulesItem = new MenuItem("Game Rules");
        rulesItem.setOnAction(e -> showRules());
        helpMenu.getItems().add(rulesItem);

        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
        return menuBar;
    }

    /**
     * Create status panel
     */
    private HBox createStatusPanel() {
        HBox statusPanel = new HBox(10);
        statusPanel.setPadding(new Insets(10));
        statusPanel.setAlignment(Pos.CENTER);
        statusPanel.setStyle("-fx-background-color: #3a506b; -fx-background-radius: 10;");

        statusLabel = new Label("Welcome to Battleship! Place your ships on the board.");
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        statusPanel.getChildren().add(statusLabel);
        return statusPanel;
    }

    /**
     * Create game area with both boards
     */
    private HBox createGameArea() {
        HBox gameArea = new HBox(40);
        gameArea.setPadding(new Insets(20));
        gameArea.setAlignment(Pos.CENTER);

        // Create player's board view
        playerBoardView = new BoardView(gameController.getGameState().getPlayerBoard(), true);
        playerBoardView.setTitle("YOUR FLEET");

        // Create computer's board view
        computerBoardView = new BoardView(gameController.getGameState().getComputerBoard(), false);
        computerBoardView.setTitle("ENEMY WATERS");

        // Set up event handlers for player board (ship placement)
        playerBoardView.setOnCellClick(this::handlePlayerBoardClick);

        // Set up event handlers for computer board (firing)
        computerBoardView.setOnCellClick(this::handleComputerBoardClick);

        // Create containers for boards with titles
        VBox playerBoardContainer = createBoardContainer(playerBoardView.getView(), "Your Fleet");
        VBox computerBoardContainer = createBoardContainer(computerBoardView.getView(), "Enemy Waters");

        gameArea.getChildren().addAll(playerBoardContainer, computerBoardContainer);
        return gameArea;
    }

    /**
     * Create a container for a board with title
     */
    private VBox createBoardContainer(VBox boardView, String title) {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        container.getChildren().addAll(titleLabel, boardView);
        return container;
    }

    /**
     * Create control panel
     */
    private VBox createControlPanel() {
        VBox controlPanel = new VBox(15);
        controlPanel.setPadding(new Insets(20));
        controlPanel.setPrefWidth(250);
        controlPanel.setStyle("-fx-background-color: #1c2541; -fx-background-radius: 10;");

        // Title
        Label title = new Label("GAME CONTROLS");
        title.setStyle("-fx-text-fill: #5bc0be; -fx-font-size: 16px; -fx-font-weight: bold;");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Start button
        startButton = new Button("START GAME");
        startButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        startButton.setPrefWidth(200);
        startButton.setOnAction(e -> startGame());
        startButton.setDisable(true);

        // Rotate button (for ship placement)
        rotateButton = new Button("ROTATE SHIP");
        rotateButton.setStyle("-fx-font-size: 12px; -fx-background-color: #FF9800; -fx-text-fill: white;");
        rotateButton.setPrefWidth(200);
        rotateButton.setOnAction(e -> rotateShip());

        // Show computer board button
        showComputerBoardButton = new Button("SHOW COMPUTER BOARD");
        showComputerBoardButton.setStyle("-fx-font-size: 12px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        showComputerBoardButton.setPrefWidth(200);
        showComputerBoardButton.setOnAction(e -> toggleComputerBoard());
        showComputerBoardButton.setDisable(true);

        // Current ship label
        currentShipLabel = new Label("Select a ship to place");
        currentShipLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        // Ship placement panel
        shipPlacementPanel = createShipPlacementPanel();

        controlPanel.getChildren().addAll(
                title,
                new Separator(),
                currentShipLabel,
                new Separator(),
                rotateButton,
                startButton,
                new Separator(),
                showComputerBoardButton,
                new Separator(),
                new Label("SHIPS TO PLACE:"),
                shipPlacementPanel
        );

        updateControlPanel();
        return controlPanel;
    }

    /**
     * Create ship placement panel
     */
    private VBox createShipPlacementPanel() {
        VBox panel = new VBox(8);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: rgba(0,0,0,0.2); -fx-background-radius: 5;");

        // Create ship views for all player ships
        int shipCount = 0;
        for (Ship ship : gameController.getGameState().getPlayer().getShips()) {
            ShipView shipView = new ShipView(ship);
            shipView.getView().setOnMouseClicked(e -> selectShip(shipView));
            shipView.getView().setUserData(shipView);
            panel.getChildren().add(shipView.getView());
            shipCount++;
        }

        System.out.println("Created " + shipCount + " ship views in placement panel");

        return panel;
    }

    /**
     * Select a ship for placement
     */
    private void selectShip(ShipView shipView) {
        if (selectedShipView != null) {
            selectedShipView.deselect();
        }

        selectedShipView = shipView;
        selectedShipView.select();

        Ship ship = shipView.getShip();
        if (ship.isPlaced()) {
            currentShipLabel.setText(ship.getName() + " - Already placed");
            rotateButton.setDisable(true);
        } else {
            currentShipLabel.setText("Selected: " + ship.getName() + " (Size: " + ship.getSize() + ")");
            rotateButton.setDisable(false);
            statusLabel.setText("Selected " + ship.getName() + ". Click on your board to place it.");
        }
    }

    /**
     * Rotate current ship orientation
     */
    private void rotateShip() {
        if (gameController.getShipPlacementController() != null && selectedShipView != null) {
            gameController.getShipPlacementController().toggleOrientation();
            String orientation = gameController.getShipPlacementController().isHorizontal() ? "Horizontal" : "Vertical";
            statusLabel.setText("Ship orientation changed to: " + orientation);
        }
    }

    /**
     * Handle click on player board (ship placement)
     */
    private void handlePlayerBoardClick(Position position) {
        if (gameController.getGameState().isGameStarted()) {
            statusLabel.setText("Game already started! Cannot place more ships.");
            return;
        }

        if (selectedShipView == null) {
            statusLabel.setText("Please select a ship first!");
            return;
        }

        Ship ship = selectedShipView.getShip();
        if (ship.isPlaced()) {
            statusLabel.setText("This ship is already placed! Select another ship.");
            return;
        }

        try {
            System.out.println("Attempting to place " + ship.getName() + " at position " + position);

            boolean placed = gameController.getShipPlacementController().placeCurrentShip(position);

            if (placed) {
                // Update visual
                playerBoardView.updateAllCells();

                // Update status
                int placedShips = gameController.getShipPlacementController().getCurrentShipIndex();
                int totalShips = gameController.getShipPlacementController().getTotalShips();

                statusLabel.setText("Ship placed! " + placedShips + "/" + totalShips + " ships placed.");

                // Visual feedback
                selectedShipView.getView().setStyle("-fx-background-color: #4CAF50; -fx-padding: 5; -fx-background-radius: 3;");

                // Select next unplaced ship
                selectNextUnplacedShip();

                // Update control panel
                updateControlPanel();

            } else {
                statusLabel.setText("Invalid placement! Ships cannot overlap or go off board.");
            }

        } catch (Exception e) {
            statusLabel.setText("Error placing ship: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Select the next unplaced ship
     */
    private void selectNextUnplacedShip() {
        for (var child : shipPlacementPanel.getChildren()) {
            if (child instanceof HBox) {
                HBox hbox = (HBox) child;
                Object userData = hbox.getUserData();
                if (userData instanceof ShipView) {
                    ShipView shipView = (ShipView) userData;
                    if (!shipView.getShip().isPlaced()) {
                        selectShip(shipView);
                        return;
                    }
                }
            }
        }
        selectedShipView = null;
        currentShipLabel.setText("All ships placed! Ready to start game.");
        statusLabel.setText("All ships placed! Click START GAME to begin.");
    }

    /**
     * Handle click on computer board (firing)
     */
    private void handleComputerBoardClick(Position position) {
        if (!gameController.getGameState().isGameStarted()) {
            statusLabel.setText("Game not started! Place all ships and click START GAME.");
            return;
        }

        if (!gameController.getGameState().isPlayerTurn()) {
            statusLabel.setText("Wait for computer's turn!");
            return;
        }

        if (gameController.getGameState().isGameOver()) {
            statusLabel.setText("Game Over! " + gameController.getGameState().getWinner() + " wins!");
            return;
        }

        try {
            System.out.println("Player firing at position: " + position);
            Board.ShotResult result = gameController.playerFire(position);
            updateStatus(result, position);
            computerBoardView.updateCell(position, result);

            // Update player board view (in case computer shot back)
            playerBoardView.updateAllCells();

            // Check if game is over
            if (gameController.getGameState().isGameOver()) {
                statusLabel.setText("GAME OVER! " + gameController.getGameState().getWinner().toUpperCase() + " WINS!");
                startButton.setText("NEW GAME");
                startButton.setDisable(false);
                showComputerBoardButton.setDisable(false);
            }

        } catch (InvalidShotException e) {
            statusLabel.setText("Invalid shot: " + e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Update status based on shot result
     */
    private void updateStatus(Board.ShotResult result, Position position) {
        String message;
        char colChar = (char) ('A' + position.getCol());
        int rowNum = position.getRow() + 1;
        String positionStr = colChar + "" + rowNum;

        switch (result) {
            case HIT:
                message = "HIT at " + positionStr + "! You get another turn.";
                break;
            case SUNK:
                message = "SUNK! You destroyed an enemy ship at " + positionStr + "!";
                break;
            case MISS:
                message = "Miss at " + positionStr + ". Computer's turn.";
                break;
            case ALREADY_SHOT:
                message = "Already shot at " + positionStr + "!";
                break;
            default:
                message = "Shot fired at " + positionStr + ".";
        }
        statusLabel.setText(message);
    }

    /**
     * Toggle computer board visibility
     */
    private void toggleComputerBoard() {
        showingComputerBoard = !showingComputerBoard;

        System.out.println("Toggling computer board visibility: " + showingComputerBoard);
        System.out.println("Computer board has " +
                gameController.getGameState().getComputerBoard().getShips().size() + " ships");

        // Update the board view
        computerBoardView.setRevealShips(showingComputerBoard);

        // Force a complete refresh
        computerBoardView.updateAllCells();

        if (showingComputerBoard) {
            showComputerBoardButton.setText("HIDE COMPUTER BOARD");
            showComputerBoardButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;");
            statusLabel.setText("TEACHER MODE: Computer board revealed. All ships visible.");

            // Log the computer's ship positions for debugging
            logComputerShipPositions();
        } else {
            showComputerBoardButton.setText("SHOW COMPUTER BOARD");
            showComputerBoardButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
            statusLabel.setText("Computer board hidden. Game mode.");
        }
    }

    /**
     * Debug method to log computer ship positions
     */
    private void logComputerShipPositions() {
        System.out.println("\n=== COMPUTER SHIP POSITIONS ===");
        Board computerBoard = gameController.getGameState().getComputerBoard();
        int shipCells = 0;

        for (int row = 0; row < 10; row++) {
            StringBuilder rowStr = new StringBuilder();
            for (int col = 0; col < 10; col++) {
                Position pos = new Position(row, col);
                Cell cell = computerBoard.getCell(pos);
                if (cell.hasShip()) {
                    shipCells++;
                    rowStr.append("S ");
                } else {
                    rowStr.append(". ");
                }
            }
            System.out.println(rowStr.toString());
        }

        System.out.println("Total ship cells: " + shipCells + " (should be 20)");
        System.out.println("Total ships placed: " + computerBoard.getShips().size());
        System.out.println("===============================\n");
    }

    /**
     * Start the game
     */
    private void startGame() {
        System.out.println("\n=== UI: STARTING GAME ===");

        if (gameController.getGameState().isGameOver()) {
            // Restart game
            gameController.resetGame();
            resetUI();
            statusLabel.setText("New game started! Place your ships.");
            return;
        }

        if (gameController.getShipPlacementController().allShipsPlaced()) {
            System.out.println("All player ships placed. Calling gameController.startGame()...");

            // Call the game controller to start the game
            gameController.startGame();

            // Verify game started
            if (gameController.getGameState().isGameStarted()) {
                // Update UI state
                startButton.setDisable(true);
                startButton.setText("GAME IN PROGRESS");
                statusLabel.setText("Game started! Your turn. Click on enemy board to fire.");
                rotateButton.setDisable(true);
                showComputerBoardButton.setDisable(false);

                // Change board click handlers
                playerBoardView.setOnCellClick(null); // Disable ship placement

                // Force update of both boards
                playerBoardView.updateAllCells();
                computerBoardView.updateAllCells();

                System.out.println("UI: Game started successfully!");
                System.out.println("Player turn: " + gameController.getGameState().isPlayerTurn());
                System.out.println("Computer ships: " +
                        gameController.getGameState().getComputerBoard().getShips().size());
            } else {
                statusLabel.setText("Failed to start game. Please try again.");
            }

        } else {
            int placed = gameController.getShipPlacementController().getCurrentShipIndex();
            int total = gameController.getShipPlacementController().getTotalShips();
            statusLabel.setText("Place all ships first! " + placed + "/" + total + " ships placed.");
        }

        System.out.println("=== UI: GAME START COMPLETE ===\n");
    }

    /**
     * Update control panel based on game state
     */
    private void updateControlPanel() {
        boolean allShipsPlaced = gameController.getShipPlacementController().allShipsPlaced();
        boolean gameStarted = gameController.getGameState().isGameStarted();
        boolean gameOver = gameController.getGameState().isGameOver();

        startButton.setDisable(!allShipsPlaced || (gameStarted && !gameOver));
        rotateButton.setDisable(gameStarted || selectedShipView == null ||
                (selectedShipView != null && selectedShipView.getShip().isPlaced()));

        if (gameOver) {
            startButton.setText("NEW GAME");
            startButton.setDisable(false);
        } else if (gameStarted) {
            startButton.setText("GAME IN PROGRESS");
        } else if (allShipsPlaced) {
            startButton.setText("START GAME");
        } else {
            startButton.setText("PLACE ALL SHIPS FIRST");
        }

        // Update show computer board button
        if (!gameStarted) {
            showComputerBoardButton.setDisable(true);
            showComputerBoardButton.setText("START GAME FIRST");
        } else {
            showComputerBoardButton.setDisable(false);
            if (showingComputerBoard) {
                showComputerBoardButton.setText("HIDE COMPUTER BOARD");
            } else {
                showComputerBoardButton.setText("SHOW COMPUTER BOARD");
            }
        }
    }

    /**
     * Update UI after loading game
     */
    private void updateUI() {
        System.out.println("UI: Updating after game load...");

        playerBoardView.updateAllCells();
        computerBoardView.updateAllCells();
        updateControlPanel();

        if (gameController.getGameState().isGameStarted()) {
            statusLabel.setText("Game loaded. " +
                    (gameController.getGameState().isPlayerTurn() ? "Your turn!" : "Computer's turn!"));
            showComputerBoardButton.setDisable(false);
        } else {
            statusLabel.setText("Game loaded. Place your ships to continue.");
        }

        // Re-select first unplaced ship
        selectNextUnplacedShip();
    }

    /**
     * Reset UI for new game
     */
    private void resetUI() {
        System.out.println("UI: Resetting UI for new game...");

        // Reset ship placement panel
        shipPlacementPanel.getChildren().clear();
        for (Ship ship : gameController.getGameState().getPlayer().getShips()) {
            ShipView shipView = new ShipView(ship);
            shipView.getView().setOnMouseClicked(e -> selectShip(shipView));
            shipView.getView().setUserData(shipView);
            shipPlacementPanel.getChildren().add(shipView.getView());
        }

        // Reset boards
        playerBoardView = new BoardView(gameController.getGameState().getPlayerBoard(), true);
        playerBoardView.setTitle("YOUR FLEET");
        playerBoardView.setOnCellClick(this::handlePlayerBoardClick);

        computerBoardView = new BoardView(gameController.getGameState().getComputerBoard(), false);
        computerBoardView.setTitle("ENEMY WATERS");
        computerBoardView.setOnCellClick(this::handleComputerBoardClick);

        // Update game area
        HBox gameArea = (HBox) mainLayout.getCenter();
        if (gameArea != null) {
            gameArea.getChildren().clear();
            gameArea.getChildren().addAll(
                    createBoardContainer(playerBoardView.getView(), "Your Fleet"),
                    createBoardContainer(computerBoardView.getView(), "Enemy Waters")
            );
        }

        // Reset controls
        selectedShipView = null;
        currentShipLabel.setText("Select a ship to place");
        startButton.setText("START GAME");
        statusLabel.setText("New game started! Place your ships.");
        showingComputerBoard = false;
        showComputerBoardButton.setDisable(true);
        showComputerBoardButton.setText("SHOW COMPUTER BOARD");

        // Select first ship
        selectNextUnplacedShip();

        updateControlPanel();
        System.out.println("UI: Reset complete");
    }

    /**
     * Show game rules
     */
    private void showRules() {
        String rules = "BATTLESHIP GAME RULES:\n\n" +
                "1. Place all 10 ships on your board\n" +
                "2. Ships cannot overlap or go off board\n" +
                "3. Take turns firing at enemy board\n" +
                "4. Hit = Red circle, Miss = White circle\n" +
                "5. Sink all enemy ships to win!\n\n" +
                "Ship Types:\n" +
                "- 1 Aircraft Carrier (4 spaces)\n" +
                "- 2 Submarines (3 spaces each)\n" +
                "- 3 Destroyers (2 spaces each)\n" +
                "- 4 Frigates (1 space each)";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Rules");
        alert.setHeaderText("Battleship Game Rules");
        alert.setContentText(rules);
        alert.showAndWait();
    }

    /**
     * Show game over dialog
     */
    private void showGameOverDialog() {
        String winner = gameController.getGameState().getWinner();
        String message = winner + " wins!\n\n" +
                "Player Ships Sunk: " + gameController.getGameState().getPlayer().getShipsSunk() + "\n" +
                "Computer Ships Sunk: " + gameController.getGameState().getComputer().getShipsSunk() + "\n" +
                "Player Score: " + gameController.getGameState().getPlayer().getScore();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("GAME OVER");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane getMainLayout() {
        return mainLayout;
    }
}