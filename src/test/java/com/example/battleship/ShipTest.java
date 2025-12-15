package com.example.battleship;

// ShipTest.java - Unit tests for Ship class

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.battleship.model.Ship;
import com.example.battleship.model.ShipType;

/**
 * Unit tests for Ship class.
 */
public class ShipTest {

    private Ship ship;

    @BeforeEach
    public void setUp() {
        ship = new Ship(ShipType.SUBMARINE);
    }

    @Test
    public void testShipInitialization() {
        assertEquals(ShipType.SUBMARINE, ship.getType());
        assertEquals(3, ship.getSize());
        assertEquals("SUBMARINE", ship.getName());
        assertFalse(ship.isPlaced());
        assertFalse(ship.isSunk());
        assertEquals(0, ship.getHitCount());
    }

    @Test
    public void testShipHit() {
        ship.hit();
        assertEquals(1, ship.getHitCount());
        assertFalse(ship.isSunk());

        ship.hit();
        ship.hit();
        assertTrue(ship.isSunk());
    }
}
