package com.example.battleship.util;

// Constants.java - Game constants
/**
 * Utility class containing game constants.
 */
public class Constants {

    // Board dimensions
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 10;

    // Ship counts
    public static final int AIRCRAFT_CARRIER_COUNT = 1;
    public static final int SUBMARINE_COUNT = 2;
    public static final int DESTROYER_COUNT = 3;
    public static final int FRIGATE_COUNT = 4;

    // Ship sizes
    public static final int AIRCRAFT_CARRIER_SIZE = 4;
    public static final int SUBMARINE_SIZE = 3;
    public static final int DESTROYER_SIZE = 2;
    public static final int FRIGATE_SIZE = 1;

    // Scoring
    public static final int HIT_SCORE = 100;
    public static final int SINK_SCORE = 500;

    // File paths
    public static final String SAVE_FILE_PATH = "saves/battleship_save.ser";
    public static final String PLAYER_DATA_PATH = "data/player_data.txt";

    // Thread pool size
    public static final int THREAD_POOL_SIZE = 2;
}
