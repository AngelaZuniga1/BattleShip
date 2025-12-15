package com.example.battleship.controller;
// GameController.java - Main controller coordinating game logic

import com.example.battleship.model.*;
import com.example.battleship.exceptions.*;
import com.example.battleship.util.GameSaver;
import com.example.battleship.util.GameLoader;
import com.example.battleship.patterns.GameCommand;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main controller class coordinating all game logic.
 * Follows MVC architecture and implements Command Pattern for game actions.
 * HU-2, HU-4, HU-5: Orchestrates firing, AI turns, and game saving
 */
public class GameController {

    private GameState gameState;
    private GameSaver gameSaver;
    private GameLoader gameLoader;
    private ExecutorService executorService;
    private ShipPlacementController shipPlacementController;

    public GameController(GameState gameState) {
        this.gameState = gameState;
        this.gameSaver = new GameSaver();
        this.gameLoader = new GameLoader();
        this.executorService = Executors.newFixedThreadPool(2); // Thread pool for concurrency
        this.shipPlacementController = new ShipPlacementController(gameState);
    }

    /**
     * HU-1: Place player's ship
     */
    public void placePlayerShip(Ship ship, Position position, boolean isHorizontal)
            throws InvalidPlacementException {

        GameCommand placeShipCommand = new GameCommand(gameState) {
            @Override
            public void execute() {
                gameState.getPlayerBoard().placeShip(ship, position, isHorizontal);
            }

            @Override
            public void undo() {
                // Implementation for undo functionality
            }
        };

        placeShipCommand.execute();
    }

    /**
     * HU-2: Player fires at computer's board
     */
    public Board.ShotResult playerFire(Position position) throws InvalidShotException {
        // Validate shot
        Cell cell = gameState.getComputerBoard().getCell(position);
        if (cell.isShot()) {
            throw new InvalidShotException("Position already shot");
        }

        // Execute shot
        Board.ShotResult result = gameState.getComputerBoard().receiveShot(position);

        // Update game state
        if (result == Board.ShotResult.HIT || result == Board.ShotResult.SUNK) {
            gameState.getPlayer().incrementScore(100);
            if (result == Board.ShotResult.SUNK) {
                gameState.getPlayer().incrementShipsSunk();
                checkGameOver();
            }
            // Player gets another turn on hit/sink
        } else {
            gameState.setPlayerTurn(false);
            // Start computer's turn in separate thread
            executorService.submit(this::computerTurn);
        }

        // Save game after each move (HU-5)
        saveGame();

        return result;
    }

    /**
     * HU-4: Computer's turn (executed in separate thread)
     */
    private void computerTurn() {
        try {
            // Simulate thinking time
            Thread.sleep(1000);

            Position shotPosition = gameState.getComputer().generateShot();
            Board.ShotResult result = gameState.getPlayerBoard().receiveShot(shotPosition);

            // Update game state
            if (result == Board.ShotResult.HIT || result == Board.ShotResult.SUNK) {
                if (result == Board.ShotResult.SUNK) {
                    gameState.getComputer().incrementShipsSunk();
                    checkGameOver();
                }
                // Computer gets another turn
                executorService.submit(this::computerTurn);
            } else {
                gameState.setPlayerTurn(true);
            }

            // Save game after computer's move (HU-5)
            saveGame();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * HU-5: Save game state
     */
    public void saveGame() {
        gameSaver.saveGame(gameState);
    }

    /**
     * HU-5: Load game state
     */
    public void loadGame() {
        GameState loadedState = gameLoader.loadGame();
        if (loadedState != null) {
            this.gameState = loadedState;
        }
    }

    /**
     * Check if game is over
     */
    private void checkGameOver() {
        if (gameState.getPlayerBoard().allShipsSunk()) {
            gameState.setGameOver(true);
            gameState.setWinner("Computer");
        } else if (gameState.getComputerBoard().allShipsSunk()) {
            gameState.setGameOver(true);
            gameState.setWinner("Player");
        }
    }

    /**
     * HU-3: Get computer's board for display (teacher mode)
     */
    public Board getComputerBoardForDisplay() {
        return gameState.getComputerBoard();
    }

    /**
     * Start the game
     */
    public void startGame() {
        // Place computer's ships randomly
        gameState.getComputer().placeShipsRandomly(gameState.getComputerBoard());
        gameState.setGameStarted(true);
        saveGame(); // Save initial game state
    }

    /**
     * Reset the game
     */
    public void resetGame() {
        // Create new game state
        this.gameState = new GameState();
        this.shipPlacementController = new ShipPlacementController(gameState);
        this.executorService.shutdown();
        this.executorService = Executors.newFixedThreadPool(2);
    }

    // Getters
    public GameState getGameState() { return gameState; }
    public ShipPlacementController getShipPlacementController() { return shipPlacementController; }
}