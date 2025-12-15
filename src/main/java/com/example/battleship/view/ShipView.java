package com.example.battleship.view;

// ShipView.java - Visual representation of a ship

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import com.example.battleship.model.Ship;

/**
 * Visual representation of a ship for the placement panel.
 */
public class ShipView {

    private HBox container;
    private Ship ship;
    private boolean selected;

    public ShipView(Ship ship) {
        this.ship = ship;
        this.selected = false;
        this.container = new HBox(5);

        initializeView();
    }

    /**
     * Initialize the ship view
     */
    private void initializeView() {
        container.setStyle("-fx-background-color: #78909c; -fx-padding: 5; -fx-background-radius: 3;");

        // Create ship segments
        for (int i = 0; i < ship.getSize(); i++) {
            Rectangle segment = new Rectangle(20, 20);
            segment.setFill(Color.GRAY);
            segment.setStroke(Color.DARKGRAY);
            segment.setStrokeWidth(1);
            container.getChildren().add(segment);
        }

        // Add ship label
        Label label = new Label(ship.getName() + " (" + ship.getSize() + ")");
        label.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
        container.getChildren().add(label);

        // Add click handler
        container.setOnMouseClicked(e -> select());
    }

    /**
     * Select this ship
     */
    public void select() {
        selected = true;
        container.setStyle("-fx-background-color: #ff9800; -fx-padding: 5; -fx-background-radius: 3;");
    }

    /**
     * Deselect this ship
     */
    public void deselect() {
        selected = false;
        container.setStyle("-fx-background-color: #78909c; -fx-padding: 5; -fx-background-radius: 3;");
    }

    public HBox getView() {
        return container;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean isSelected() {
        return selected;
    }
}
