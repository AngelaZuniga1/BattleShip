package com.example.battleship.model;

//Main game state management

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import com.example.battleship.patterns.Observer;

/**
 * Represents the complete state of the Battleship game.
 * Implements Serializable for game saving (HU-5).
 * Follows SOLID's Single Responsibility Principle - manages only game state.
 */
public class GameState extends Observable implements Serializable {
    private static final long serialVersionUID = 1L;

    private Board playerBoard;
    private Board computerBoard;
    private Player player;
    private ComputerPlayer computer;
    private boolean isPlayerTurn;
    private boolean gameStarted;
    private boolean gameOver;
    private String winner;
    private List<Observer> observers;

    /**
     * Constructor initializes game state with empty boards and players.
     */
    public GameState() {
        this.playerBoard = new Board();
        this.computerBoard = new Board();
        this.player = new Player("Player");
        this.computer = new ComputerPlayer();
        this.isPlayerTurn = true;
        this.gameStarted = false;
        this.gameOver = false;
        this.observers = new ArrayList<>();

        // Initialize boards with ships
        initializeShips();
    }

    /**
     * HU-1: Initialize all ships for both players
     */
    private void initializeShips() {
        // Player's ships
        player.addShip(new Ship(ShipType.AIRCRAFT_CARRIER));
        player.addShip(new Ship(ShipType.SUBMARINE));
        player.addShip(new Ship(ShipType.SUBMARINE));
        player.addShip(new Ship(ShipType.DESTROYER));
        player.addShip(new Ship(ShipType.DESTROYER));
        player.addShip(new Ship(ShipType.DESTROYER));
        player.addShip(new Ship(ShipType.FRIGATE));
        player.addShip(new Ship(ShipType.FRIGATE));
        player.addShip(new Ship(ShipType.FRIGATE));
        player.addShip(new Ship(ShipType.FRIGATE));

        // Computer's ships
        computer.addShip(new Ship(ShipType.AIRCRAFT_CARRIER));
        computer.addShip(new Ship(ShipType.SUBMARINE));
        computer.addShip(new Ship(ShipType.SUBMARINE));
        computer.addShip(new Ship(ShipType.DESTROYER));
        computer.addShip(new Ship(ShipType.DESTROYER));
        computer.addShip(new Ship(ShipType.DESTROYER));
        computer.addShip(new Ship(ShipType.FRIGATE));
        computer.addShip(new Ship(ShipType.FRIGATE));
        computer.addShip(new Ship(ShipType.FRIGATE));
        computer.addShip(new Ship(ShipType.FRIGATE));
    }

    // Getters and setters
    public Board getPlayerBoard() { return playerBoard; }
    public Board getComputerBoard() { return computerBoard; }
    public Player getPlayer() { return player; }
    public ComputerPlayer getComputer() { return computer; }
    public boolean isPlayerTurn() { return isPlayerTurn; }
    public void setPlayerTurn(boolean playerTurn) {
        this.isPlayerTurn = playerTurn;
        notifyObservers();
    }
    public boolean isGameStarted() { return gameStarted; }
    public void setGameStarted(boolean started) {
        this.gameStarted = started;
        notifyObservers();
    }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean over) {
        this.gameOver = over;
        notifyObservers();
    }
    public String getWinner() { return winner; }
    public void setWinner(String winner) {
        this.winner = winner;
        notifyObservers();
    }

    /**
     * Notify all observers of state changes
     * Observer Pattern implementation
     */
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }
}