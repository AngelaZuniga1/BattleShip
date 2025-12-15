package com.example.battleship.model;

// Position.java - Represents board coordinates

import java.io.Serializable;

/**
 * Represents a position (row, column) on the game board.
 * Implements equals and hashCode for use in collections.
 */
public class Position implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
