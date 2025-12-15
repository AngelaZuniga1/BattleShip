package com.example.battleship.model;

// ComputerPlayer.java - Computer opponent implementation

import java.util.Random;
import com.example.battleship.controller.AIStrategy;

/**
 * Represents the computer opponent.
 * HU-4: Implements AI for autonomous gameplay
 */
public class ComputerPlayer extends Player {

    private AIStrategy aiStrategy;
    private Random random;

    public ComputerPlayer() {
        super("Computer");
        this.random = new Random();
        this.aiStrategy = new AIStrategy();
    }

    /**
     * HU-4: Generate random shot position
     */
    public Position generateShot() {
        return aiStrategy.generateShot();
    }

    /**
     * HU-4: Place ships randomly on board
     */
    public void placeShipsRandomly(Board board) {
        aiStrategy.placeShipsRandomly(board, getShips());
    }

    public AIStrategy getAiStrategy() {
        return aiStrategy;
    }
}