package com.example.battleship.view;

// CellView.java - Visual representation of a single cell

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

/**
 * Visual representation of a single game cell with 2D graphics.
 * Implements detailed graphics for ships, water, hits, and sunk markers.
 */

public class CellView {
    private static final int CELL_SIZE = 40;
    private StackPane container;
    private Rectangle background;
    private Rectangle shipShape;
    private Circle hitMarker;
    private Circle missMarker;
    private Line sunkCross1;
    private Line sunkCross2;

    public CellView() {
        container = new StackPane();
        container.setPrefSize(CELL_SIZE, CELL_SIZE);

        // Create background (water)
        background = new Rectangle(CELL_SIZE, CELL_SIZE);
        background.setFill(Color.LIGHTBLUE);
        background.setStroke(Color.DARKBLUE);
        background.setStrokeWidth(1);

        // Create ship shape (hidden by default)
        shipShape = new Rectangle(CELL_SIZE - 4, CELL_SIZE - 4);
        shipShape.setFill(Color.GRAY);
        shipShape.setStroke(Color.DARKGRAY);
        shipShape.setStrokeWidth(2);
        shipShape.setVisible(false);

        // Create hit marker
        hitMarker = new Circle(CELL_SIZE / 4);
        hitMarker.setFill(Color.RED);
        hitMarker.setStroke(Color.DARKRED);
        hitMarker.setStrokeWidth(2);
        hitMarker.setVisible(false);

        // Create miss marker
        missMarker = new Circle(CELL_SIZE / 6);
        missMarker.setFill(Color.WHITE);
        missMarker.setStroke(Color.BLUE);
        missMarker.setStrokeWidth(1);
        missMarker.setVisible(false);

        // Create sunk cross markers
        sunkCross1 = new Line(2, 2, CELL_SIZE - 2, CELL_SIZE - 2);
        sunkCross1.setStroke(Color.RED);
        sunkCross1.setStrokeWidth(3);
        sunkCross1.setVisible(false);

        sunkCross2 = new Line(2, CELL_SIZE - 2, CELL_SIZE - 2, 2);
        sunkCross2.setStroke(Color.RED);
        sunkCross2.setStrokeWidth(3);
        sunkCross2.setVisible(false);

        container.getChildren().addAll(
                background, shipShape, missMarker, hitMarker, sunkCross1, sunkCross2
        );
    }

    /**
     * Show ship in this cell
     */
    public void showShip() {
        shipShape.setVisible(true);
    }

    /**
     * Show hit marker
     */
    public void showHit() {
        hitMarker.setVisible(true);
        background.setFill(Color.rgb(255, 200, 200)); // Light red
    }

    /**
     * Show miss marker
     */
    public void showMiss() {
        missMarker.setVisible(true);
        background.setFill(Color.rgb(200, 220, 255)); // Light blue
    }

    /**
     * Show sunk marker
     */
    public void showSunk() {
        sunkCross1.setVisible(true);
        sunkCross2.setVisible(true);
        shipShape.setFill(Color.DARKGRAY);
        background.setFill(Color.rgb(255, 150, 150)); // Medium red
    }

    /**
     * Clear all markers
     */
    public void clear() {
        shipShape.setVisible(false);
        hitMarker.setVisible(false);
        missMarker.setVisible(false);
        sunkCross1.setVisible(false);
        sunkCross2.setVisible(false);
        background.setFill(Color.LIGHTBLUE);
    }

    public StackPane getView() {
        return container;
    }
}