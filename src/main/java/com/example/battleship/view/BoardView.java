package com.example.battleship.view;

import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;
import com.example.battleship.model.*;
import java.util.function.Consumer;

/**
 * Visual representation of a game board with 2D graphics.
 * Uses JavaFX shapes for ships, water, hits, and sunk markers.
 */
public class BoardView {

    private GridPane grid;
    private CellView[][] cells;
    private Board board;
    private boolean isPlayerBoard;
    private String title;
    private boolean revealShips;
    private Consumer<Position> cellClickHandler;

    public BoardView(Board board, boolean isPlayerBoard) {
        this.board = board;
        this.isPlayerBoard = isPlayerBoard;
        this.revealShips = false;
        this.grid = new GridPane();
        this.cells = new CellView[10][10];
        initializeBoard();
    }

    /**
     * Initialize the visual board
     */
    private void initializeBoard() {
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setStyle("-fx-background-color: #0a2463; -fx-padding: 10; -fx-border-color: #1e3c72; -fx-border-width: 3; -fx-border-radius: 5;");

        // Add column headers (A-J)
        for (int col = 0; col < 10; col++) {
            Text header = new Text(String.valueOf((char)('A' + col)));
            header.setFill(Color.WHITE);
            header.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            GridPane.setHalignment(header, javafx.geometry.HPos.CENTER);
            grid.add(header, col + 1, 0);
        }

        // Add row headers (1-10)
        for (int row = 0; row < 10; row++) {
            Text header = new Text(String.valueOf(row + 1));
            header.setFill(Color.WHITE);
            header.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            GridPane.setValignment(header, javafx.geometry.VPos.CENTER);
            grid.add(header, 0, row + 1);
        }

        // Create cells
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Position position = new Position(row, col);
                CellView cellView = new CellView();

                // Set up event handlers
                cellView.getView().setOnMouseClicked(event -> handleCellClick(event, position));

                // Add hover effect
                cellView.getView().setOnMouseEntered(event -> {
                    if (cellClickHandler != null && !isPlayerBoard && board.getCell(position).isShot() == false) {
                        cellView.getView().setStyle("-fx-border-color: yellow; -fx-border-width: 2;");
                    }
                });

                cellView.getView().setOnMouseExited(event -> {
                    cellView.getView().setStyle("");
                });

                cells[row][col] = cellView;
                grid.add(cellView.getView(), col + 1, row + 1);
            }
        }

        updateAllCells();
    }

    /**
     * Handle cell click event
     */
    private void handleCellClick(MouseEvent event, Position position) {
        if (cellClickHandler != null && !isPlayerBoard) {
            cellClickHandler.accept(position);
        }
    }

    /**
     * Update all cells based on board state
     * FIXED: Now properly shows computer ships when revealShips is true
     */
    public void updateAllCells() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Position position = new Position(row, col);
                Cell cell = board.getCell(position);
                CellView cellView = cells[row][col];

                // First, clear the cell
                cellView.clear();

                // If cell has been shot, show the result
                if (cell.isShot()) {
                    if (cell.hasShip()) {
                        if (cell.isSunk()) {
                            cellView.showSunk();
                        } else {
                            cellView.showHit();
                        }
                    } else {
                        cellView.showMiss();
                    }
                }
                // If not shot, show ships if we're supposed to reveal them
                else if (revealShips && cell.hasShip()) {
                    cellView.showShip();
                }
                // For player board, always show ships (since they're visible to player)
                else if (isPlayerBoard && cell.hasShip()) {
                    cellView.showShip();
                }
            }
        }
    }

    /**
     * Update specific cell based on shot result
     */
    public void updateCell(Position position, Board.ShotResult result) {
        CellView cellView = cells[position.getRow()][position.getCol()];

        // Clear existing markers first
        cellView.clear();

        // Apply new state based on shot result
        switch (result) {
            case HIT:
                cellView.showHit();
                break;
            case MISS:
                cellView.showMiss();
                break;
            case SUNK:
                cellView.showSunk();
                break;
            case ALREADY_SHOT:
                break;
        }

        //Update the cell's shot status in the model
        Cell cell = board.getCell(position);
        cell.setShot(true);

        // If it's a sunk result, update all cells of that ship
        if (result == Board.ShotResult.SUNK && cell.hasShip()) {
            Ship ship = cell.getShip();
            if (ship != null && ship.getPositions() != null) {
                for (Position shipPos : ship.getPositions()) {
                    cells[shipPos.getRow()][shipPos.getCol()].showSunk();
                }
            }
        }
    }

    /**
     * Set cell click handler
     */
    public void setOnCellClick(Consumer<Position> handler) {
        this.cellClickHandler = handler;
    }

    /**
     * Set board title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set whether to reveal ships (for computer board)
     */
    public void setRevealShips(boolean reveal) {
        this.revealShips = reveal;
        updateAllCells(); // Re-render all cells with new reveal state
    }

    /**
     * Get the view of this board
     */
    public VBox getView() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new javafx.geometry.Insets(10));

        if (title != null) {
            Text titleText = new Text(title);
            titleText.setFill(Color.WHITE);
            titleText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            container.getChildren().add(titleText);
        }

        container.getChildren().add(grid);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: #1c2541; -fx-background-radius: 10; -fx-padding: 15;");
        return container;
    }

    /**
     * Reset the board view
     */
    public void reset() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                cells[row][col].clear();
            }
        }
        updateAllCells();
    }
}