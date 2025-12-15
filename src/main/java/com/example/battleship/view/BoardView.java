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
        this.cells = new CellView[10][10]; // Fixed: hardcoded to 10x10
        initializeBoard();
    }

    /**
     * Initialize the visual board
     */
    private void initializeBoard() {
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setStyle("-fx-background-color: #1a237e; -fx-padding: 5;");

        // Add column headers (A-J)
        for (int col = 0; col < 10; col++) {
            Text header = new Text(String.valueOf((char)('A' + col)));
            header.setFill(Color.WHITE);
            header.setStyle("-fx-font-weight: bold;");
            grid.add(header, col + 1, 0);
        }

        // Add row headers (1-10)
        for (int row = 0; row < 10; row++) {
            Text header = new Text(String.valueOf(row + 1));
            header.setFill(Color.WHITE);
            header.setStyle("-fx-font-weight: bold;");
            grid.add(header, 0, row + 1);
        }

        // Create cells
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Position position = new Position(row, col);
                CellView cellView = new CellView();

                // Set up event handlers
                cellView.getView().setOnMouseClicked(event -> handleCellClick(event, position));

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
     */
    public void updateAllCells() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Position position = new Position(row, col);
                Cell cell = board.getCell(position);

                if (cell.isShot()) {
                    if (cell.hasShip()) {
                        if (cell.isSunk()) {
                            updateCell(position, Board.ShotResult.SUNK);
                        } else {
                            updateCell(position, Board.ShotResult.HIT);
                        }
                    } else {
                        updateCell(position, Board.ShotResult.MISS);
                    }
                } else if (isPlayerBoard || revealShips) {
                    if (cell.hasShip()) {
                        cells[row][col].showShip();
                    }
                }
            }
        }
    }

    /**
     * Update specific cell based on shot result
     */
    public void updateCell(Position position, Board.ShotResult result) {
        CellView cellView = cells[position.getRow()][position.getCol()];

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
            default:
                // Do nothing for ALREADY_SHOT
                break;
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
        updateAllCells();
    }

    /**
     * Get the view of this board
     */
    public VBox getView() {
        VBox container = new VBox(5);

        if (title != null) {
            Text titleText = new Text(title);
            titleText.setFill(Color.WHITE);
            titleText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            container.getChildren().add(titleText);
        }

        container.getChildren().add(grid);
        container.setAlignment(Pos.CENTER);
        return container;
    }
}