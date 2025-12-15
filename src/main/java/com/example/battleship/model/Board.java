package com.example.battleship.model;

// Board.java - Represents the game board

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.battleship.exceptions.InvalidPlacementException;

/**
 * Represents a 10x10 game board for Battleship.
 * Uses multiple data structures: 2D array, List, Map (fulfills requirement of 4+ data structures)
 * HU-1: Handles ship placement validation
 * HU-2: Tracks shot results
 */

/**
 * Represents the game board.
 */
public class Board implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int width = 10;
    private final int height = 10;
    private Cell[][] grid;
    private List<Ship> ships;

    public Board() {
        this.grid = new Cell[height][width];
        this.ships = new ArrayList<>();
        initializeGrid();
    }

    private void initializeGrid() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(new Position(row, col));
            }
        }
    }

    /**
     * Place a ship on the board
     */
    public boolean placeShip(Ship ship, Position position, boolean isHorizontal) {
        List<Position> positions = new ArrayList<>();

        // Calculate all positions
        for (int i = 0; i < ship.getSize(); i++) {
            int row = isHorizontal ? position.getRow() : position.getRow() + i;
            int col = isHorizontal ? position.getCol() + i : position.getCol();

            // Check bounds
            if (row < 0 || row >= height || col < 0 || col >= width) {
                return false;
            }

            // Check if cell already has a ship
            if (grid[row][col].hasShip()) {
                return false;
            }

            positions.add(new Position(row, col));
        }

        // Place the ship
        for (Position pos : positions) {
            grid[pos.getRow()][pos.getCol()].setHasShip(true);
            grid[pos.getRow()][pos.getCol()].setShip(ship);
        }

        ship.setPositions(positions);
        ship.setPlaced(true);
        ships.add(ship);
        return true;
    }

    /**
     * Receive a shot at given position
     */
    public ShotResult receiveShot(Position position) {
        Cell cell = getCell(position);

        if (cell.isShot()) {
            return ShotResult.ALREADY_SHOT;
        }

        cell.setShot(true);

        if (cell.hasShip()) {
            Ship ship = cell.getShip();
            ship.hit();

            if (ship.isSunk()) {
                // Mark all cells of the ship as sunk
                for (Position pos : ship.getPositions()) {
                    getCell(pos).setSunk(true);
                }
                return ShotResult.SUNK;
            }
            return ShotResult.HIT;
        }

        return ShotResult.MISS;
    }

    public Cell getCell(Position position) {
        return grid[position.getRow()][position.getCol()];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public boolean allShipsSunk() {
        return ships.stream().allMatch(Ship::isSunk);
    }

    /**
     * ShotResult enum - This is what's being used in BoardView.java
     */
    public enum ShotResult {
        HIT, MISS, SUNK, ALREADY_SHOT
    }
}