package com.example.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.battleship.controller.GameController;
import com.example.battleship.model.GameState;
import com.example.battleship.model.Position;
import com.example.battleship.exceptions.InvalidShotException;

/**
 * Unit tests for GameController class.
 */
public class GameControllerTest {

    private GameController gameController;
    private GameState gameState;

    @BeforeEach
    public void setUp() {
        gameState = new GameState();
        gameController = new GameController(gameState);
    }

    @Test
    public void testGameInitialization() {
        assertNotNull(gameController.getGameState());
        assertNotNull(gameController.getGameState().getPlayer());
        assertNotNull(gameController.getGameState().getComputer());
        assertTrue(gameController.getGameState().isPlayerTurn());
        assertFalse(gameController.getGameState().isGameStarted());
        assertFalse(gameController.getGameState().isGameOver());
    }

    @Test
    public void testInvalidShotOnSamePosition() {
        Position position = new Position(0, 0);

        try {
            // First shot should work
            gameController.playerFire(position);

            // Second shot should throw exception
            assertThrows(InvalidShotException.class, () -> {
                gameController.playerFire(position);
            }, "Should not allow shooting same position twice");

        } catch (InvalidShotException e) {
            fail("First shot should be valid: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidShotOutOfBounds() {
        Position invalidPosition = new Position(10, 10); // Out of bounds

        assertThrows(InvalidShotException.class, () -> {
            gameController.playerFire(invalidPosition);
        }, "Should not allow shooting out of bounds");
    }

    @Test
    public void testShipPlacementController() {
        assertNotNull(gameController.getShipPlacementController());
        assertFalse(gameController.getShipPlacementController().allShipsPlaced());
        assertEquals(0, gameController.getShipPlacementController().getCurrentShipIndex());
        assertEquals(10, gameController.getShipPlacementController().getTotalShips());
    }
}