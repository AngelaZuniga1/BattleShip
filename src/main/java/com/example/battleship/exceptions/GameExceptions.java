package com.example.battleship.exceptions;

// GameException.java - Base custom exception

/**
 * Base custom exception for the Battleship game.
 * Implements both checked and unchecked exceptions through inheritance.
 */
public class GameExceptions extends Exception {

    public GameExceptions(String message) {
        super(message);
    }

    public GameExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}