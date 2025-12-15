package com.example.battleship.model;

import java.util.Random;
import com.example.battleship.controller.AIStrategy;

public class ComputerPlayer extends Player {

    private AIStrategy aiStrategy;
    private Random random;

    public ComputerPlayer() {
        super("Computer");
        this.random = new Random();
        this.aiStrategy = new AIStrategy();
        initializeComputerShips(); // ADD THIS LINE
    }

    /**
     * Initialize computer's ships
     */
    private void initializeComputerShips() {
        // Clear any existing ships
        getShips().clear();

        // Add computer's ships (same as player's ships)
        // 1 Aircraft Carrier (4 spaces)
        addShip(new Ship(ShipType.AIRCRAFT_CARRIER));

        // 2 Submarines (3 spaces each)
        addShip(new Ship(ShipType.SUBMARINE));
        addShip(new Ship(ShipType.SUBMARINE));

        // 3 Destroyers (2 spaces each)
        addShip(new Ship(ShipType.DESTROYER));
        addShip(new Ship(ShipType.DESTROYER));
        addShip(new Ship(ShipType.DESTROYER));

        // 4 Frigates (1 space each)
        addShip(new Ship(ShipType.FRIGATE));
        addShip(new Ship(ShipType.FRIGATE));
        addShip(new Ship(ShipType.FRIGATE));
        addShip(new Ship(ShipType.FRIGATE));

        System.out.println("Computer ships initialized: " + getShips().size() + " ships total");
    }

    /**
     * Generate random shot position
     */
    public Position generateShot() {
        return aiStrategy.generateShot();
    }

    /**
     * Place ships randomly on board
     */
    public void placeShipsRandomly(Board board) {
        System.out.println("=== STARTING COMPUTER SHIP PLACEMENT ===");
        System.out.println("Computer has " + getShips().size() + " ships to place");

        // Reset all ships to not placed
        for (Ship ship : getShips()) {
            ship.setPlaced(false);
            ship.setPositions(null);
        }

        //Clear the board first
        board.clear();

        //Place ships using AI strategy
        aiStrategy.placeShipsRandomly(board, getShips());

        //Verify placement
        int placedShips = 0;
        for (Ship ship : getShips()) {
            if (ship.isPlaced()) {
                placedShips++;
            }
        }

        System.out.println("Successfully placed " + placedShips + "/" + getShips().size() + " ships");

        //Count total ship cells
        int shipCells = 0;
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Position pos = new Position(row, col);
                if (board.getCell(pos).hasShip()) {
                    shipCells++;
                }
            }
        }
        System.out.println("Total ship cells on computer board: " + shipCells + " (should be 20)");
        System.out.println("=== COMPUTER SHIP PLACEMENT COMPLETE ===");
    }

    public AIStrategy getAiStrategy() {
        return aiStrategy;
    }
}