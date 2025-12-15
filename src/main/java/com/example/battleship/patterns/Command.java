package com.example.battleship.patterns;

//Command interface

/**
 * Command interface for the Command pattern.
 * Encapsulates game actions as objects.
 */
public interface Command {
    void execute();
    void undo();
}