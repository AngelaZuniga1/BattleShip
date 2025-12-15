module com.example.battleship {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.battleship to javafx.fxml;
    opens com.example.battleship.view to javafx.fxml;
    opens com.example.battleship.controller to javafx.fxml;
    opens com.example.battleship.model to javafx.base;

    exports com.example.battleship;
    exports com.example.battleship.view;
    exports com.example.battleship.controller;
    exports com.example.battleship.model;
    exports com.example.battleship.util;
    exports com.example.battleship.patterns;
    exports com.example.battleship.exceptions;
}