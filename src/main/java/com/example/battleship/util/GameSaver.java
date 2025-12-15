package com.example.battleship.util;

//Handles game saving

import com.example.battleship.model.GameState;
import com.example.battleship.controller.FileManager;
import com.example.battleship.exceptions.FileOperationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles game saving operations with concurrency.
 * HU-5: Implements automatic game saving after each move
 * Uses Thread for concurrency requirement
 */
public class GameSaver {

    private FileManager fileManager;
    private ExecutorService executorService;

    public GameSaver() {
        this.fileManager = new FileManager();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Save game state asynchronously
     */
    public void saveGame(GameState gameState) {
        executorService.submit(() -> {
            try {
                // Save serializable board state
                fileManager.saveSerializable(gameState, Constants.SAVE_FILE_PATH);

                // Save player data to flat file
                fileManager.savePlayerData(
                        gameState.getPlayer().getName(),
                        gameState.getPlayer().getScore(),
                        gameState.getPlayer().getShipsSunk()
                );

                System.out.println("Game saved successfully.");

            } catch (FileOperationException e) {
                System.err.println("Failed to save game: " + e.getMessage());
            }
        });
    }
}