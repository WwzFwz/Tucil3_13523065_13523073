package com.jawa; 

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

    @FXML
    private void initialize() {
        initializeBoard(30, 30);


        algorithmComboBox.getItems().addAll(
            "A*", 
            "Greedy Best-First", 
            "Uniform-Cost Search"
        );

        algorithmComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
        
            // resetHeuristicAndSolveButton();
            
            updateHeuristicOptions(newValue);
        });
        
        // Listener untuk heuristik juga
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
        fileUploaded = true;
        fileNameLabel.setText("File: " + "test.txt"); 
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
    // public void handleReset() {
    //     // hapus aja klo nanti ga ada tombol reset
    //     fileUploaded = false;
    //     // currentFile = null;
    //     fileNameLabel.setText("No file selected");
        
    //     algorithmComboBox.setValue(null);
    //     heuristicComboBox.setValue(null);
        
    //     algorithmComboBox.setDisable(true);
    //     heuristicComboBox.setDisable(true);
    //     solveButton.setDisable(true);
    //     backButton.setDisable(true);
    //     playPauseButton.setDisable(true);
    //     nextButton.setDisable(true);
        

    //     algorithmComboBox.setPromptText("Upload File First");
    //     heuristicComboBox.setPromptText("Select Algorithm First");
        
 
    //     stepsListView.getItems().clear();
        

    //     // markStepAsPending(uploadButton);
    //     // markStepAsPending(algorithmComboBox);
    //     // markStepAsPending(heuristicComboBox);
    //     // markStepAsPending(solveButton);
        
    //     stepsPagination.setDisable(true);
    //     stepsPagination.setPageCount(1);
    //     stepsPagination.setCurrentPageIndex(0);
        
    //     System.out.println("dirsset");
    // }
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
}