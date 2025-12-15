package com.example.battleship.exceptions;

//File operation exception

/**
 * Unchecked exception for file operations.
 * HU-5: Used when file operations fail
 */
public class FileOperationException extends RuntimeException {

    public FileOperationException(String message) {
        super(message);
    }

    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}