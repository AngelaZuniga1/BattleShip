package com.example.battleship.model;

// ShipType.java - Enumeration of ship types

/**
 * Enum representing different ship types with their sizes.
 * Follows SOLID's Interface Segregation - each ship type has clear properties.
 */
public enum ShipType {
    AIRCRAFT_CARRIER(4),
    SUBMARINE(3),
    DESTROYER(2),
    FRIGATE(1);

    private final int size;

    ShipType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}