package com.example.battleship.controller;

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

        //Reset placement status
        for (Ship ship : shipsToPlace) {
            ship.setPlaced(false);
            ship.setPositions(null);
        }
    }

    /**
     * Place current ship at specified position
     */
    public boolean placeCurrentShip(Position position) {
        if (currentShipIndex >= shipsToPlace.size()) {
            return false;
        }

        Ship currentShip = shipsToPlace.get(currentShipIndex);

        if (currentShip.isPlaced()) {
            currentShipIndex++;
            return placeCurrentShip(position);
        }

        // Try to place ship
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
        for (Ship ship : shipsToPlace) {
            if (!ship.isPlaced()) {
                return false;
            }
        }
        return true;
    }

    public void toggleOrientation() {
        isHorizontal = !isHorizontal;
    }

    public boolean isHorizontal() { return isHorizontal; }
    public int getCurrentShipIndex() {
        int placed = 0;
        for (Ship ship : shipsToPlace) {
            if (ship.isPlaced()) placed++;
        }
        return placed;
    }
    public int getTotalShips() { return shipsToPlace.size(); }

    /**
     * Reset placement state
     */
    public void reset() {
        currentShipIndex = 0;
        isHorizontal = true;
        for (Ship ship : shipsToPlace) {
            ship.setPlaced(false);
            ship.setPositions(null);
        }
    }
}