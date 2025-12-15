package com.example.battleship;

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
        assertEquals(2, ship.getHitCount());
        assertFalse(ship.isSunk());

        ship.hit();
        assertEquals(3, ship.getHitCount());
        assertTrue(ship.isSunk());
    }

    @Test
    public void testShipPlacement() {
        assertFalse(ship.isPlaced());
        ship.setPlaced(true);
        assertTrue(ship.isPlaced());
    }

    @Test
    public void testDifferentShipTypes() {
        Ship carrier = new Ship(ShipType.AIRCRAFT_CARRIER);
        assertEquals(4, carrier.getSize());
        assertEquals("AIRCRAFT CARRIER", carrier.getName());

        Ship destroyer = new Ship(ShipType.DESTROYER);
        assertEquals(2, destroyer.getSize());

        Ship frigate = new Ship(ShipType.FRIGATE);
        assertEquals(1, frigate.getSize());
    }
}