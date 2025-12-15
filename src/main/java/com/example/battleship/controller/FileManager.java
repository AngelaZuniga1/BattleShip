package com.example.battleship.controller;

// FileManager - Handles file operations

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import com.example.battleship.exceptions.FileOperationException;

/**
 * Handles file operations for game persistence.
 * HU-5: Manages serializable and flat files for game saving
 */
public class FileManager {

    private static final String SAVE_FILE = "battleship_save.ser";
    private static final String PLAYER_DATA_FILE = "player_data.txt";

    /**
     * Save serializable object to file
     */
    public void saveSerializable(Object obj, String filename) throws FileOperationException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new FileOperationException("Failed to save file: " + e.getMessage());
        }
    }

    /**
     * Load serializable object from file
     */
    public Object loadSerializable(String filename) throws FileOperationException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new FileOperationException("Failed to load file: " + e.getMessage());
        }
    }

    /**
     * Save player data to flat file
     */
    public void savePlayerData(String playerName, int score, int shipsSunk)
            throws FileOperationException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PLAYER_DATA_FILE))) {
            writer.println("Player: " + playerName);
            writer.println("Score: " + score);
            writer.println("Ships Sunk: " + shipsSunk);
            writer.println("Date: " + new java.util.Date());
        } catch (IOException e) {
            throw new FileOperationException("Failed to save player data: " + e.getMessage());
        }
    }

    /**
     * Load player data from flat file
     */
    public List<String> loadPlayerData() throws FileOperationException {
        List<String> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PLAYER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            throw new FileOperationException("Failed to load player data: " + e.getMessage());
        }
        return data;
    }
}