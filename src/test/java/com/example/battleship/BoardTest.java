package com.example.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.battleship.model.*;
import com.example.battleship.controller.GameController;

/**
 * Unit tests for Board class.
 * Tests ship placement, shooting, and game logic.
 */
public class BoardTest {

    private Board board;
    private Ship ship;

    @BeforeEach
    public void setUp() {
        board = new Board();
        ship = new Ship(ShipType.DESTROYER);
    }

    @Test
    public void testPlaceShipValid() {
        boolean placed = board.placeShip(ship, new Position(0, 0), true);
        assertTrue(placed, "Ship should be placed successfully");
        assertTrue(ship.isPlaced(), "Ship should be marked as placed");
        assertEquals(2, ship.getPositions().size(), "Destroyer should occupy 2 positions");
    }

    @Test
    public void testPlaceShipOutOfBounds() {
        // Try to place ship that would go off board
        boolean placed = board.placeShip(ship, new Position(9, 9), true);
        assertFalse(placed, "Ship should not be placed out of bounds");
    }

    @Test
    public void testPlaceShipOverlap() {
        // Place first ship
        board.placeShip(ship, new Position(0, 0), true);

        // Try to place another ship in same position
        Ship ship2 = new Ship(ShipType.FRIGATE);
        boolean placed = board.placeShip(ship2, new Position(0, 0), true);
        assertFalse(placed, "Ships should not overlap");
    }

    @Test
    public void testReceiveShotHit() {
        board.placeShip(ship, new Position(0, 0), true);
        Board.ShotResult result = board.receiveShot(new Position(0, 0));
        assertEquals(Board.ShotResult.HIT, result, "Shot should hit the ship");
    }

    @Test
    public void testReceiveShotMiss() {
        Board.ShotResult result = board.receiveShot(new Position(0, 0));
        assertEquals(Board.ShotResult.MISS, result, "Shot should miss when no ship");
    }

    @Test
    public void testReceiveShotAlreadyShot() {
        board.receiveShot(new Position(0, 0));
        Board.ShotResult result = board.receiveShot(new Position(0, 0));
        assertEquals(Board.ShotResult.ALREADY_SHOT, result, "Should not allow shooting same cell twice");
    }

    @Test
    public void testShipSinking() {
        board.placeShip(ship, new Position(0, 0), true);

        // Hit first segment
        board.receiveShot(new Position(0, 0));
        assertFalse(ship.isSunk(), "Ship should not be sunk after one hit");

        // Hit second segment
        Board.ShotResult result = board.receiveShot(new Position(0, 1));
        assertEquals(Board.ShotResult.SUNK, result, "Ship should be sunk after all hits");
        assertTrue(ship.isSunk(), "Ship should be marked as sunk");
    }

    @Test
    public void testAllShipsSunk() {
        board.placeShip(ship, new Position(0, 0), true);

        // Sink the ship
        board.receiveShot(new Position(0, 0));
        board.receiveShot(new Position(0, 1));

        assertTrue(board.allShipsSunk(), "All ships should be sunk");
    }
}