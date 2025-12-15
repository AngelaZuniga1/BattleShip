package com.example.battleship.controller;

import com.example.battleship.model.*;
import com.example.battleship.exceptions.*;
import com.example.battleship.util.GameSaver;
import com.example.battleship.util.GameLoader;
import com.example.battleship.patterns.GameCommand;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Main controller class coordinating all game logic.
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
        this.executorService = Executors.newFixedThreadPool(2);
        this.shipPlacementController = new ShipPlacementController(gameState);

        System.out.println("GameController initialized");
        System.out.println("Player ships: " + gameState.getPlayer().getShips().size());
        System.out.println("Computer ships: " + gameState.getComputer().getShips().size());
    }

    /**
     * Player fires at computer's board
     */
    public Board.ShotResult playerFire(Position position) throws InvalidShotException {
        System.out.println("Player firing at position: " + position);

        // Validate shot
        if (position.getRow() < 0 || position.getRow() >= 10 ||
                position.getCol() < 0 || position.getCol() >= 10) {
            throw new InvalidShotException("Position out of bounds");
        }

        Cell cell = gameState.getComputerBoard().getCell(position);
        if (cell.isShot()) {
            throw new InvalidShotException("Position already shot");
        }

        // Execute shot
        Board.ShotResult result = gameState.getComputerBoard().receiveShot(position);
        System.out.println("Shot result: " + result);

        // Update game state
        if (result == Board.ShotResult.HIT || result == Board.ShotResult.SUNK) {
            gameState.getPlayer().incrementScore(100);
            if (result == Board.ShotResult.SUNK) {
                gameState.getPlayer().incrementShipsSunk();
                System.out.println("Player sunk a ship! Total sunk: " + gameState.getPlayer().getShipsSunk());
                checkGameOver();
            }
            // Player gets another turn on hit/sink
            gameState.setPlayerTurn(true);
        } else {
            gameState.setPlayerTurn(false);
            System.out.println("Player missed. Computer's turn now.");
            // Start computer's turn in separate thread
            executorService.submit(this::computerTurn);
        }

        //Save game after each move
        saveGame();

        return result;
    }

    /**
     * Computer's turn (executed in separate thread)
     */
    private void computerTurn() {
        try {
            System.out.println("Computer's turn starting...");

            //Simulate thinking time
            Thread.sleep(1000);

            Position shotPosition = gameState.getComputer().generateShot();
            System.out.println("Computer shooting at: " + shotPosition);

            Board.ShotResult result = gameState.getPlayerBoard().receiveShot(shotPosition);
            System.out.println("Computer shot result: " + result);

            //Update game state
            if (result == Board.ShotResult.HIT || result == Board.ShotResult.SUNK) {
                if (result == Board.ShotResult.SUNK) {
                    gameState.getComputer().incrementShipsSunk();
                    System.out.println("Computer sunk a ship! Total sunk: " + gameState.getComputer().getShipsSunk());
                    checkGameOver();
                }
                //Computer gets another turn
                System.out.println("Computer hit! Gets another turn.");
                executorService.submit(this::computerTurn);
            } else {
                gameState.setPlayerTurn(true);
                System.out.println("Computer missed. Player's turn now.");
            }

            //Save game after computer's move
            saveGame();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Computer turn interrupted");
        } catch (Exception e) {
            System.err.println("Error in computer turn: " + e.getMessage());
            e.printStackTrace();
            gameState.setPlayerTurn(true); // Give turn back to player on error
        }
    }

    /**
     * Save game state
     */
    public void saveGame() {
        System.out.println("Saving game...");
        gameSaver.saveGame(gameState);
    }

    /**
     * Load game state
     */
    public void loadGame() {
        System.out.println("Loading game...");
        GameState loadedState = gameLoader.loadGame();
        if (loadedState != null) {
            this.gameState = loadedState;
            this.shipPlacementController = new ShipPlacementController(gameState);
            System.out.println("Game loaded successfully");
            System.out.println("Game started: " + gameState.isGameStarted());
            System.out.println("Player turn: " + gameState.isPlayerTurn());
            System.out.println("Game over: " + gameState.isGameOver());
        } else {
            System.out.println("No saved game found, starting fresh");
        }
    }

    /**
     * Check if game is over
     */
    private void checkGameOver() {
        boolean playerAllSunk = gameState.getPlayerBoard().allShipsSunk();
        boolean computerAllSunk = gameState.getComputerBoard().allShipsSunk();

        if (playerAllSunk || computerAllSunk) {
            gameState.setGameOver(true);
            if (playerAllSunk) {
                gameState.setWinner("Computer");
                System.out.println("GAME OVER: Computer wins!");
            } else {
                gameState.setWinner("Player");
                System.out.println("GAME OVER: Player wins!");
            }
        }
    }

    /**
     * Start the game
     */
    public void startGame() {
        System.out.println("\n=== GAME CONTROLLER: STARTING GAME ===");

        // First, make sure all player ships are placed
        if (!shipPlacementController.allShipsPlaced()) {
            System.out.println("ERROR: Not all player ships are placed!");
            System.out.println("Placed: " + shipPlacementController.getCurrentShipIndex() +
                    "/" + shipPlacementController.getTotalShips());
            return;
        }

        System.out.println("All player ships are placed. Placing computer ships...");

        // Place computer's ships randomly
        gameState.getComputer().placeShipsRandomly(gameState.getComputerBoard());

        // Verify computer ships were placed
        int computerShipCount = gameState.getComputerBoard().getShips().size();
        System.out.println("Computer board now has " + computerShipCount + " ships");

        if (computerShipCount == 10) {
            gameState.setGameStarted(true);
            gameState.setPlayerTurn(true);
            gameState.setGameOver(false);
            gameState.setWinner(null);

            // Save initial game state
            saveGame();

            System.out.println("=== GAME STARTED SUCCESSFULLY ===");
        } else {
            System.out.println("ERROR: Failed to place all computer ships!");
            System.out.println("Expected: 10, Got: " + computerShipCount);
            // Try to fix by resetting and trying again
            gameState.getComputerBoard().clear();
            gameState.getComputer().placeShipsRandomly(gameState.getComputerBoard());
        }
    }

    /**
     * Reset the game
     */
    public void resetGame() {
        System.out.println("\n=== GAME CONTROLLER: RESETTING GAME ===");

        // Shutdown old executor
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        //Create new game state
        this.gameState = new GameState();
        this.shipPlacementController = new ShipPlacementController(gameState);
        this.executorService = Executors.newFixedThreadPool(2);

        System.out.println("Game reset complete.");
        System.out.println("=== GAME RESET COMPLETE ===\n");
    }

    /**
     * Get game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Get ship placement controller
     */
    public ShipPlacementController getShipPlacementController() {
        return shipPlacementController;
    }

    /**
     * Cleanup resources
     */
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        saveGame();
    }
}