package com.example.battleship.controller;

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

        // Convert to readable format for debugging
        char colChar = (char) ('A' + shot.getCol());
        int rowNum = shot.getRow() + 1;
        System.out.println("Computer shooting at: " + colChar + rowNum + " (Position: " + shot + ")");

        return shot;
    }

    /**
     * HU-4: Place ships randomly on board
     */
    public void placeShipsRandomly(Board board, List<Ship> ships) {
        System.out.println("AI Strategy: Placing " + ships.size() + " ships");

        //This reset all ships
        for (Ship ship : ships) {
            ship.setPlaced(false);
            ship.setPositions(null);
        }

        //This clear the board
        board.clear();

        //Try to place each ship
        for (Ship ship : ships) {
            boolean placed = false;
            int attempts = 0;
            int maxAttempts = 500; //Increased from 100 to 500

            while (!placed && attempts < maxAttempts) {
                int row = random.nextInt(10);
                int col = random.nextInt(10);
                boolean horizontal = random.nextBoolean();

                Position position = new Position(row, col);

                // Check if this placement is valid
                if (canPlaceShip(board, ship, position, horizontal)) {
                    //place the ship
                    placed = board.placeShip(ship, position, horizontal);
                    if (placed) {
                        System.out.println("Successfully placed " + ship.getName() +
                                " at (" + row + "," + col + ") " +
                                (horizontal ? "horizontal" : "vertical"));
                    }
                }

                attempts++;

                if (attempts % 100 == 0) {
                    System.out.println("Attempt " + attempts + " to place " + ship.getName());
                }
            }

            if (!placed) {
                System.err.println("FAILED to place ship: " + ship.getName() + " after " + maxAttempts + " attempts");
                //Instead of recursive call, reset and try again from beginning
                System.out.println("Resetting board and trying again...");
                board.clear();
                for (Ship s : ships) {
                    s.setPlaced(false);
                    s.setPositions(null);
                }
                //Restart the loop
                placeShipsRandomly(board, ships);
                return;
            }
        }

        System.out.println("All ships placed successfully!");
    }

    /**
     * Check if a ship can be placed at a given position
     */
    private boolean canPlaceShip(Board board, Ship ship, Position startPos, boolean horizontal) {
        for (int i = 0; i < ship.getSize(); i++) {
            int row = horizontal ? startPos.getRow() : startPos.getRow() + i;
            int col = horizontal ? startPos.getCol() + i : startPos.getCol();

            //Check bounds
            if (row < 0 || row >= 10 || col < 0 || col >= 10) {
                return false;
            }

            //Check if cell already has a ship
            Position pos = new Position(row, col);
            if (board.getCell(pos).hasShip()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Remove position from available shots (when it's already been shot)
     */
    public void removeShotPosition(Position position) {
        availableShots.remove(position);
    }

    /**
     * Reset available shots
     */
    public void reset() {
        initializeAvailableShots();
    }
}