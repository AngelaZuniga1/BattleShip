package com.example.battleship.view;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;

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
    private DropShadow glowEffect;

    public CellView() {
        container = new StackPane();
        container.setPrefSize(CELL_SIZE, CELL_SIZE);

        // Create glow effect for ships
        glowEffect = new DropShadow();
        glowEffect.setColor(Color.rgb(0, 200, 255, 0.7));
        glowEffect.setRadius(10);
        glowEffect.setSpread(0.5);

        // Create background (water)
        background = new Rectangle(CELL_SIZE, CELL_SIZE);
        background.setFill(Color.rgb(173, 216, 230)); // Light blue
        background.setStroke(Color.rgb(25, 118, 210)); // Darker blue border
        background.setStrokeWidth(1.5);
        background.setArcWidth(5);
        background.setArcHeight(5);

        // Create ship shape (hidden by default)
        shipShape = new Rectangle(CELL_SIZE - 6, CELL_SIZE - 6);
        shipShape.setFill(Color.rgb(100, 100, 100)); // Dark gray for ships
        shipShape.setStroke(Color.rgb(50, 50, 50)); // Even darker border
        shipShape.setStrokeWidth(2);
        shipShape.setArcWidth(4);
        shipShape.setArcHeight(4);
        shipShape.setVisible(false);
        shipShape.setEffect(glowEffect);

        // Create hit marker
        hitMarker = new Circle(CELL_SIZE / 3);
        hitMarker.setFill(Color.rgb(255, 50, 50, 0.8)); // Semi-transparent red
        hitMarker.setStroke(Color.rgb(200, 0, 0)); // Dark red border
        hitMarker.setStrokeWidth(2);
        hitMarker.setVisible(false);

        // Create miss marker
        missMarker = new Circle(CELL_SIZE / 4);
        missMarker.setFill(Color.rgb(255, 255, 255, 0.9)); // Semi-transparent white
        missMarker.setStroke(Color.rgb(100, 149, 237)); // Cornflower blue border
        missMarker.setStrokeWidth(1.5);
        missMarker.setVisible(false);

        // Create sunk cross markers
        sunkCross1 = new Line(4, 4, CELL_SIZE - 4, CELL_SIZE - 4);
        sunkCross1.setStroke(Color.rgb(255, 0, 0)); // Bright red
        sunkCross1.setStrokeWidth(4);
        sunkCross1.setVisible(false);

        sunkCross2 = new Line(4, CELL_SIZE - 4, CELL_SIZE - 4, 4);
        sunkCross2.setStroke(Color.rgb(255, 0, 0));
        sunkCross2.setStrokeWidth(4);
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
        background.setFill(Color.rgb(150, 150, 150, 0.3)); // Light gray overlay
    }

    /**
     * Show hit marker
     */
    public void showHit() {
        hitMarker.setVisible(true);
        background.setFill(Color.rgb(255, 100, 100, 0.5)); // Light red background
        shipShape.setVisible(true); // Show ship outline for hits
        shipShape.setFill(Color.rgb(150, 50, 50)); // Dark red for hit ship
    }

    /**
     * Show miss marker
     */
    public void showMiss() {
        missMarker.setVisible(true);
        background.setFill(Color.rgb(200, 220, 255)); // Very light blue
    }

    /**
     * Show sunk marker
     */
    public void showSunk() {
        sunkCross1.setVisible(true);
        sunkCross2.setVisible(true);
        shipShape.setVisible(true);
        shipShape.setFill(Color.rgb(100, 0, 0)); // Very dark red
        background.setFill(Color.rgb(255, 150, 150, 0.7)); // Medium red background

        // Add pulsating effect for sunk ships
        javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(
                javafx.util.Duration.millis(500), sunkCross1
        );
        st.setByX(0.1);
        st.setByY(0.1);
        st.setCycleCount(javafx.animation.Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.play();

        javafx.animation.ScaleTransition st2 = new javafx.animation.ScaleTransition(
                javafx.util.Duration.millis(500), sunkCross2
        );
        st2.setByX(0.1);
        st2.setByY(0.1);
        st2.setCycleCount(javafx.animation.Animation.INDEFINITE);
        st2.setAutoReverse(true);
        st2.play();
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
        background.setFill(Color.rgb(173, 216, 230)); // Reset to light blue

        // Stop any animations
        sunkCross1.setScaleX(1);
        sunkCross1.setScaleY(1);
        sunkCross2.setScaleX(1);
        sunkCross2.setScaleY(1);
    }

    public StackPane getView() {
        return container;
    }
}