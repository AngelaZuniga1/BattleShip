package com.example.battleship.model;

// Cell.java - Represents a single cell on the board

import java.io.Serializable;

/**
 * Represents a single cell on the game board.
 * Tracks whether it has a ship, has been shot, etc.
 */
public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Position position;
    private boolean hasShip;
    private boolean isShot;
    private boolean isSunk;
    private Ship ship;

    public Cell(Position position) {
        this.position = position;
        this.hasShip = false;
        this.isShot = false;
        this.isSunk = false;
        this.ship = null;
    }

    // Getters and setters
    public Position getPosition() { return position; }
    public boolean hasShip() { return hasShip; }
    public void setHasShip(boolean hasShip) { this.hasShip = hasShip; }
    public boolean isShot() { return isShot; }
    public void setShot(boolean shot) { this.isShot = shot; }
    public boolean isSunk() { return isSunk; }
    public void setSunk(boolean sunk) { this.isSunk = sunk; }
    public Ship getShip() { return ship; }
    public void setShip(Ship ship) { this.ship = ship; }
}