package com.example.battleship.exceptions;

//Invalid shot exception
/**
 * Checked exception for invalid shots.
 * HU-2: Used when player attempts invalid shot
 */
public class InvalidShotException extends GameExceptions {

    public InvalidShotException(String message) {
        super(message);
    }

    public InvalidShotException(String message, Throwable cause) {
        super(message, cause);
    }
}