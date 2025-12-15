package com.example.battleship.util;

// GameLoader.java - Handles game loading

import com.example.battleship.model.GameState;
import com.example.battleship.controller.FileManager;
import com.example.battleship.exceptions.FileOperationException;

/**
 * Handles game loading operations.
 * HU-5: Loads saved game state
 */
public class GameLoader {

    private FileManager fileManager;

    public GameLoader() {
        this.fileManager = new FileManager();
    }

    /**
     * Load saved game state
     */
    public GameState loadGame() {
        try {
            GameState gameState = (GameState) fileManager.loadSerializable(
                    Constants.SAVE_FILE_PATH);

            System.out.println("Game loaded successfully.");
            return gameState;

        } catch (FileOperationException e) {
            System.out.println("No saved game found, starting new game.");
            return null;
        }
    }
}