package com.example.battleship.exceptions;

//Ship placement exception

/**
 * Checked exception for invalid ship placement.
 * HU-1: Used when ship placement violates rules
 */
public class InvalidPlacementException extends GameExceptions {

    public InvalidPlacementException(String message) {
        super(message);
    }

    public InvalidPlacementException(String message, Throwable cause) {
        super(message, cause);
    }
}