package com.example.battleship.controller;

// AIStrategy.java - Computer AI implementation

import com.example.battleship.model.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Implements the computer's AI strategy.
 * HU-4: Random ship placement and shot generation
 */
public class AIStrategy {

    private Random random;
    private List<Position> availableShots;

    public AIStrategy() {
        this.random = new Random();
        initializeAvailableShots();
    }

    /**
     * Initialize all possible shot positions
     */
    private void initializeAvailableShots() {
        availableShots = new ArrayList<>();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                availableShots.add(new Position(row, col));
            }
        }
    }

    /**
     * HU-4: Generate random shot
     */
    public Position generateShot() {
        if (availableShots.isEmpty()) {
            initializeAvailableShots();
        }

        int index = random.nextInt(availableShots.size());
        Position shot = availableShots.remove(index);
        return shot;
    }

    /**
     * HU-4: Place ships randomly on board
     */
    public void placeShipsRandomly(Board board, List<Ship> ships) {
        for (Ship ship : ships) {
            boolean placed = false;
            int attempts = 0;

            while (!placed && attempts < 100) {
                try {
                    int row = random.nextInt(10);
                    int col = random.nextInt(10);
                    boolean horizontal = random.nextBoolean();

                    Position position = new Position(row, col);
                    board.placeShip(ship, position, horizontal);
                    placed = true;

                } catch (Exception e) {
                    attempts++;
                }
            }

            if (!placed) {
                System.err.println("Failed to place ship: " + ship.getName());
            }
        }
    }

    /**
     * Remove position from available shots (when it's already been shot)
     */
    public void removeShotPosition(Position position) {
        availableShots.remove(position);
    }
}
