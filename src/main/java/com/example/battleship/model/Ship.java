package com.example.battleship.model;

// Ship.java - Represents a ship in the game

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a ship with type, size, and state.
 * Follows SOLID's Open/Closed Principle - can be extended with new ship types.
 */

public class Ship implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ShipType type;
    private final int size;
    private List<Position> positions;
    private int hitCount;
    private boolean placed;
    private boolean sunk;

    public Ship(ShipType type) {
        this.type = type;
        this.size = type.getSize();
        this.positions = null;
        this.hitCount = 0;
        this.placed = false;
        this.sunk = false;
    }

    /**
     * Record a hit on the ship
     */
    public void hit() {
        hitCount++;
        if (hitCount >= size) {
            sunk = true;
        }
    }

    // Getters and setters
    public ShipType getType() { return type; }
    public int getSize() { return size; }
    public List<Position> getPositions() { return positions; }
    public void setPositions(List<Position> positions) { this.positions = positions; }
    public int getHitCount() { return hitCount; }
    public boolean isPlaced() { return placed; }
    public void setPlaced(boolean placed) { this.placed = placed; }
    public boolean isSunk() { return sunk; }

    /**
     * Get ship name based on type
     */
    public String getName() {
        return type.name().replace("_", " ");
    }
}