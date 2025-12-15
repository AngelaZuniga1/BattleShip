package com.example.battleship.patterns;

// GameCommand.java - Concrete command implementation

import com.example.battleship.model.*;

/**
 * Concrete implementation of Command pattern for game actions.
 */
public abstract class GameCommand implements Command {

    protected GameState gameState;

    public GameCommand(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public abstract void execute();

    @Override
    public abstract void undo();
}
