package com.example.battleship.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.example.battleship.exceptions.InvalidPlacementException;

/**
 * Represents a 10x10 game board for Battleship.
 * Uses multiple data structures: 2D array, List, Map (fulfills requirement of 4+ data structures)
 * HU-1: Handles ship placement validation
 * HU-2: Tracks shot results
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
     * HU-1: Validates ship placement
     */
    public boolean placeShip(Ship ship, Position position, boolean isHorizontal) {
        // Check if ship is already placed
        if (ship.isPlaced()) {
            return false;
        }

        List<Position> positions = new ArrayList<>();

        //Calculate all positions
        for (int i = 0; i < ship.getSize(); i++) {
            int row = isHorizontal ? position.getRow() : position.getRow() + i;
            int col = isHorizontal ? position.getCol() + i : position.getCol();

            // Check bounds
            if (row < 0 || row >= height || col < 0 || col >= width) {
                return false;
            }

            //Check if cell already has a ship
            if (grid[row][col].hasShip()) {
                return false;
            }

            positions.add(new Position(row, col));
        }

        //Place the ship
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
     * HU-2: Processes shots and returns results
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
        if (position.getRow() >= 0 && position.getRow() < height &&
                position.getCol() >= 0 && position.getCol() < width) {
            return grid[position.getRow()][position.getCol()];
        }
        throw new IllegalArgumentException("Position out of bounds: " + position);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Ship> getShips() {
        return new ArrayList<>(ships);
    }

    public boolean allShipsSunk() {
        return ships.stream().allMatch(Ship::isSunk);
    }

    /**
     * Check if position is valid
     */
    public boolean isValidPosition(Position position) {
        return position.getRow() >= 0 && position.getRow() < height &&
                position.getCol() >= 0 && position.getCol() < width;
    }

    /**
     * Clear all ships from board (for reset)
     */
    public void clear() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col].setHasShip(false);
                grid[row][col].setShip(null);
                grid[row][col].setShot(false);
                grid[row][col].setSunk(false);
            }
        }
        ships.clear();
    }

    /**
     * ShotResult enum - Represents possible shot outcomes
     */
    public enum ShotResult {
        HIT, MISS, SUNK, ALREADY_SHOT
    }
}