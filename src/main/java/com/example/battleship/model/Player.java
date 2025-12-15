package com.example.battleship.model;

//Base player class

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class representing a player in the game.
 * Follows SOLID's Liskov Substitution Principle - can be extended.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private List<Ship> ships;
    private int score;
    private int shipsSunk;

    public Player(String name) {
        this.name = name;
        this.ships = new ArrayList<>();
        this.score = 0;
        this.shipsSunk = 0;
    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }

    public void incrementScore(int points) {
        score += points;
    }

    public void incrementShipsSunk() {
        shipsSunk++;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Ship> getShips() { return ships; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getShipsSunk() { return shipsSunk; }
    public void setShipsSunk(int shipsSunk) { this.shipsSunk = shipsSunk; }

    /**
     * Check if player has lost (all ships sunk)
     */
    public boolean hasLost() {
        return ships.stream().allMatch(Ship::isSunk);
    }
}