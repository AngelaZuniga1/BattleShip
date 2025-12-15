package com.example.battleship;

// BoardTest.java - Unit tests for Board class

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.battleship.model.*;
import com.example.battleship.exceptions.InvalidPlacementException;

/**
 * Unit tests for Board class.
 * Tests ship placement, shooting, and game logic.
 */
public class BoardTest {

    private Board board;
    private Ship ship;

    @BeforeEach
    public void setUp() {
        board = new Board(10, 10);
        ship = new Ship(ShipType.DESTROYER);
    }

    @Test
    public void testPlaceShipValid() {
        try {
            board.placeShip(ship, new Position(0, 0), true);
            assertTrue(ship.isPlaced());
            assertEquals(2, ship.getPositions().size());
        } catch (InvalidPlacementException e) {
            fail("Should not throw exception for valid placement");
        }
    }

    @Test
    public void testPlaceShipOutOfBounds() {
        assertThrows(InvalidPlacementException.class, () -> {
            board.placeShip(ship, new Position(9, 9), true);
        });
    }

    @Test
    public void testReceiveShotHit() {
        try {
            board.placeShip(ship, new Position(0, 0), true);
            Board.ShotResult result = board.receiveShot(new Position(0, 0));
            assertEquals(Board.ShotResult.HIT, result);
        } catch (InvalidPlacementException e) {
            fail("Setup failed");
        }
    }

    @Test
    public void testReceiveShotMiss() {
        Board.ShotResult result = board.receiveShot(new Position(0, 0));
        assertEquals(Board.ShotResult.MISS, result);
    }

    @Test
    public void testAllShipsSunk() {
        try {
            board.placeShip(ship, new Position(0, 0), true);

            // Hit both segments
            board.receiveShot(new Position(0, 0));
            board.receiveShot(new Position(0, 1));

            assertTrue(ship.isSunk());
        } catch (InvalidPlacementException e) {
            fail("Setup failed");
        }
    }
}