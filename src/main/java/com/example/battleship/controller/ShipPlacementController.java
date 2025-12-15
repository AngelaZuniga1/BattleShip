package com.example.battleship.controller;

// ShipPlacementController.java - Handles ship placement logic

import com.example.battleship.model.*;
import com.example.battleship.exceptions.InvalidPlacementException;
import java.util.List;

/**
 * Handles ship placement logic for the human player.
 * HU-1: Validates ship placement and manages placement state
 */
public class ShipPlacementController {

    private GameState gameState;
    private List<Ship> shipsToPlace;
    private int currentShipIndex;
    private boolean isHorizontal;

    public ShipPlacementController(GameState gameState) {
        this.gameState = gameState;
        this.shipsToPlace = gameState.getPlayer().getShips();
        this.currentShipIndex = 0;
        this.isHorizontal = true;
    }

    /**
     * Place current ship at specified position
     */
    public boolean placeCurrentShip(Position position) {
        if (currentShipIndex >= shipsToPlace.size()) {
            return false;
        }

        Ship currentShip = shipsToPlace.get(currentShipIndex);

        // Call placeShip which should return boolean
        boolean placed = gameState.getPlayerBoard().placeShip(currentShip, position, isHorizontal);

        if (placed) {
            currentShipIndex++;
            return true;
        }
        return false;
    }

    public Ship getCurrentShip() {
        if (currentShipIndex < shipsToPlace.size()) {
            return shipsToPlace.get(currentShipIndex);
        }
        return null;
    }

    public boolean allShipsPlaced() {
        return currentShipIndex >= shipsToPlace.size();
    }

    public void toggleOrientation() {
        isHorizontal = !isHorizontal;
    }

    public boolean isHorizontal() { return isHorizontal; }
    public int getCurrentShipIndex() { return currentShipIndex; }
    public int getTotalShips() { return shipsToPlace.size(); }
}