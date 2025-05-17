package com.jawa; 

import java.io.File;
import java.util.Map;

import com.jawa.model.gameComponent.Board;
import com.jawa.model.gameComponent.Movement;
import com.jawa.model.gameComponent.Piece;
import com.jawa.model.gameComponent.Position;
import com.jawa.model.gameState.IO;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

public class MainController {
    @FXML
    private Button uploadButton;
    
    @FXML
    private Label fileNameLabel;
    
    @FXML
    private ComboBox<String> algorithmComboBox;
    
    @FXML
    private ComboBox<String> heuristicComboBox;

    @FXML
    private Button solveButton;
    
    @FXML
    private Button backButton;
    
    @FXML
    private Button playPauseButton;
    
    @FXML
    private Button nextButton;
    
    @FXML
    private ListView<String> stepsListView;
    
    @FXML
    private Pagination stepsPagination;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Pane boardPane;
    
    private boolean fileUploaded = false;
    private File selectedFile;
    private Board board;
    public File getSelectedFile(){
        return selectedFile;
    }
    @FXML
    private void initialize() {
        initializeBoard(30, 30);


        algorithmComboBox.getItems().addAll(
            "A*", 
            "Greedy Best-First", 
            "Uniform-Cost Search"
        );

        algorithmComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                    
            updateHeuristicOptions(newValue);
        });

        heuristicComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSolveButtonState();
        });


    }
    private void initializeBoard(int rows, int cols) {
        boardPane.getChildren().clear();
        
        GridPane boardGrid = new GridPane();
        boardGrid.setHgap(2); 
        boardGrid.setVgap(2); 
        boardGrid.setPadding(new Insets(10));
        double cellSize = calculateCellSize(rows, cols);
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                StackPane cell = createCell(row, col, cellSize);
                boardGrid.add(cell, col, row);
            }
        }
    
        boardPane.getChildren().add(boardGrid);
       
        boardGrid.layoutXProperty().bind(
                boardPane.widthProperty().subtract(boardGrid.widthProperty()).divide(2));
        boardGrid.layoutYProperty().bind(
                boardPane.heightProperty().subtract(boardGrid.heightProperty()).divide(2));
    }

    private void updateHeuristicOptions(String algorithm) {
        heuristicComboBox.getItems().clear();
        heuristicComboBox.setValue(null);
        
        solveButton.setDisable(true);
        
        if (!fileUploaded) {
            heuristicComboBox.setDisable(true);
            heuristicComboBox.setPromptText("Upload File First");
            return;
        }
        
        if (algorithm == null) {
            heuristicComboBox.setDisable(true);
            heuristicComboBox.setPromptText("Select Algorithm First");
            return;
        }
        switch (algorithm) {
            case "A*":
                heuristicComboBox.setDisable(false);
                heuristicComboBox.setPromptText("Select Heuristic");
                heuristicComboBox.getItems().addAll(
                    "Manhattan Distance", 
                    "Blocking Vehicles", 
                    "Advanced Blocking"
                );
                break;
                    
            case "Greedy Best-First":
                heuristicComboBox.setDisable(false);
                heuristicComboBox.getItems().addAll(
                    "Manhattan Distance", 
                    "Blocking Vehicles"
                );
                break;
                    
            case "Uniform-Cost Search":
                heuristicComboBox.setDisable(true);
                heuristicComboBox.setPromptText("No Heuristic Needed");
                if (fileUploaded) {
                    solveButton.setDisable(false);
                }
                break;
                    
            default:
                heuristicComboBox.setDisable(true);
                heuristicComboBox.setPromptText("Unknown Algorithm");
        }
    }

    private void updateSolveButtonState() {

        if (!fileUploaded) {   
            solveButton.setDisable(true);
            return;
        }
        
       
        String algorithm = algorithmComboBox.getValue();
        if (algorithm == null) {
            solveButton.setDisable(true);
            return;
        }

        switch (algorithm) {
            case "A*":
            case "Greedy Best-First":
                solveButton.setDisable(heuristicComboBox.getValue() == null);
                break;
                
            case "Uniform-Cost Search":
                solveButton.setDisable(false);
                break;
                
            default:
                solveButton.setDisable(true);
                break;
        }
    }
    @FXML
    private void handleUploadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Puzzle File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

   
        selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        try {
            board = IO.loadFromFile(getSelectedFile());
        } catch (Exception e) {
            fileNameLabel.setText("Error Occured : " + e.getMessage());
        }
        initializeBoard(board.getRow(),board.getCol());
        displayPiecesOnBoard(board);
        fileUploaded = true;
        fileNameLabel.setText("File: " + selectedFile.getName()); 
        algorithmComboBox.setDisable(false);
        algorithmComboBox.setPromptText("Select Algorithm");
        updateHeuristicOptions(algorithmComboBox.getValue());
    }

    @FXML   
    private void handleSolve() {
        String selectedAlgorithm = algorithmComboBox.getValue();
        String selectedHeuristic = heuristicComboBox.getValue();


    }
    
    @FXML
    private void handlePlayPause() {
    }
    
    @FXML
    private void handleNext() { 
 
    }
    
    @FXML
    private void handleBack() {
     
    }
    private StackPane createCell(int row, int col, double size) {
        Rectangle cellBg = new Rectangle(size, size);
        cellBg.setFill(Color.web("#1a1158")); 
        cellBg.setStroke(Color.web("#4287f5")); 
        cellBg.setStrokeWidth(1.5); 
        StackPane cell = new StackPane(cellBg);
        cell.setId("cell_" + row + "_" + col);
        return cell;
    }

    private double calculateCellSize(int rows, int cols) {
        double availableWidth = boardPane.getWidth();
        double availableHeight = boardPane.getHeight();
        if (availableWidth <= 0) availableWidth = boardPane.getPrefWidth();
        if (availableHeight <= 0) availableHeight = boardPane.getPrefHeight();
        
        if (availableWidth <= 0) availableWidth = 400;
        if (availableHeight <= 0) availableHeight = 400;
        
        availableWidth -= 40; 
        availableHeight -= 40; 
        
        double maxCellWidth = availableWidth / cols;
        double maxCellHeight = availableHeight / rows;
        
        double cellSize = Math.min(maxCellWidth, maxCellHeight);

        if (cellSize > 60) cellSize = 60; 

        if (cellSize < 20) cellSize = 20; 
        return cellSize;
    }

    private void displayPiecesOnBoard(Board board) {
        GridPane boardGrid = (GridPane) boardPane.getChildren().get(0);
        clearPieces(boardGrid);
        double cellSize = calculateCellSize(board.getRows(), board.getCols());
        Map<String, Piece> pieces = board.getPieces();
        for (Piece piece : pieces.values()) {
            displaySinglePiece(boardGrid, piece, cellSize);
        }
    }
    private void clearPieces(GridPane boardGrid) {
        boardGrid.getChildren().removeIf(node -> 
            !(node instanceof StackPane) || 
            !((StackPane) node).getId().startsWith("cell_"));
    }

    private void displaySinglePiece(GridPane boardGrid, Piece piece, double cellSize) {
        Rectangle pieceRect = new Rectangle();
        
        if (piece.isHorizontal()) {
            pieceRect.setWidth(cellSize * piece.getLength() - 4); 
            pieceRect.setHeight(cellSize - 4);
        } else {
            pieceRect.setWidth(cellSize - 4);
            pieceRect.setHeight(cellSize * piece.getLength() - 4);
        }
        

        pieceRect.setFill(piece.getColor());
        pieceRect.setStroke(Color.BLACK);
        pieceRect.setStrokeWidth(2);
        pieceRect.setArcWidth(10);
        pieceRect.setArcHeight(10);
        
        // // Efek hover
        pieceRect.setOnMouseEntered(e -> pieceRect.setOpacity(0.8));
        pieceRect.setOnMouseExited(e -> pieceRect.setOpacity(1.0));

        StackPane pieceContainer = new StackPane();
        pieceContainer.getChildren().add(pieceRect);
 
        Label idLabel = new Label(piece.getId());
        idLabel.setTextFill(Color.WHITE);
        idLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        pieceContainer.getChildren().add(idLabel);
        
        pieceContainer.setId(piece.getId());
        
        Position pos = piece.getPosition();
        if (piece.isHorizontal()) {
            GridPane.setColumnSpan(pieceContainer, piece.getLength());
            boardGrid.add(pieceContainer, pos.getCol(), pos.getRow());
        } else {
            GridPane.setRowSpan(pieceContainer, piece.getLength());
            boardGrid.add(pieceContainer, pos.getCol(), pos.getRow());
        }
    }
    private void clearPieceById(GridPane boardGrid, String pieceId) {
        boardGrid.getChildren().removeIf(node ->
            (node instanceof StackPane) && 
            ((StackPane) node).getId() != null &&
            ((StackPane) node).getId().equals(pieceId)
        );
    }

    private void updatePieceByMovement(GridPane boardGrid, Movement movement) {
        String pieceId = movement.getPieceId();
        String direction = movement.getDirection();
        int distance = movement.getDistance();

        StackPane pieceContainer = null;
        
        for (javafx.scene.Node node : boardGrid.getChildren()) {
            if (node instanceof StackPane && 
                pieceId.equals(((StackPane) node).getId())) {
                pieceContainer = (StackPane) node;
                break;
            }
        }
        
        // Jika piece tidak ditemukan, keluar dari fungsi
        if (pieceContainer == null) {
            System.out.println("Piece with ID " + pieceId + " not found");
            return;
        }
        
        // Dapatkan posisi saat ini
        Integer currentRow = GridPane.getRowIndex(pieceContainer);
        Integer currentCol = GridPane.getColumnIndex(pieceContainer);
        
        // Jika posisi null, berikan nilai default 0
        currentRow = (currentRow == null) ? 0 : currentRow;
        currentCol = (currentCol == null) ? 0 : currentCol;
        
        // Hitung posisi baru berdasarkan direction dan distance
        int newRow = currentRow;
        int newCol = currentCol;
        
        switch (direction) {
            case "U": 
                newRow = currentRow - distance;
                break;
            case "D": 
                newRow = currentRow + distance;
                break;
            case "L":
                newCol = currentCol - distance;
                break;
            case "R": 
                newCol = currentCol + distance;
                break;
            default:
                System.out.println("Invalid direction: " + direction);
                return;
        }
        
        // Validasi posisi baru (pastikan dalam bounds GridPane)
        int rowCount = boardGrid.getRowCount();
        int colCount = boardGrid.getColumnCount();
        
        if (newRow < 0 || newRow >= rowCount || newCol < 0 || newCol >= colCount) {
            System.out.println("Movement out of bounds: " + newRow + ", " + newCol);
            return;
        }
        
        // Periksa apakah ada piece lain di posisi target
        boolean targetOccupied = false;
        StackPane occupyingPiece = null;
        
        for (javafx.scene.Node node : boardGrid.getChildren()) {
            if (node instanceof StackPane && node != pieceContainer) {
                Integer nodeRow = GridPane.getRowIndex(node);
                Integer nodeCol = GridPane.getColumnIndex(node);
                
                // Jika posisi null, berikan nilai default 0
                nodeRow = (nodeRow == null) ? 0 : nodeRow;
                nodeCol = (nodeCol == null) ? 0 : nodeCol;
                
                if (nodeRow == newRow && nodeCol == newCol) {
                    targetOccupied = true;
                    occupyingPiece = (StackPane) node;
                    break;
                }
            }
        }
        
  
        
        GridPane.setRowIndex(pieceContainer, newRow);
        GridPane.setColumnIndex(pieceContainer, newCol);
    }


    
}