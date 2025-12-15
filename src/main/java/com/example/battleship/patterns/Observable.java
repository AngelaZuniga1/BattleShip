package com.example.battleship.patterns;

//Observable interface

/**
 * Observable interface for the Observer pattern.
 * Allows objects to be observed by observers.
 */
public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
