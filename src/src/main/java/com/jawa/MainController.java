package com.jawa; 

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.layout.Pane;

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
    
    @FXML
    private void initialize() {
        // Initialize UI components
        algorithmComboBox.getItems().addAll("BFS", "DFS", "A*", "Greedy Best-First");
        heuristicComboBox.getItems().addAll("Blocking Cars", "Distance to Exit", "Manhattan Distance");
    }
    
    @FXML
    private void handleUploadFile() {
        fileNameLabel.setText("File selected: example.txt");
    }
    
    @FXML   
    private void handleSolve() {
        // Handle solve button click
        statusLabel.setText("Solving...");
    }
    
    @FXML
    private void handlePlayPause() {
        // Handle play/pause button
    }
    
    @FXML
    private void handleNext() {
        // Handle next button
    }
    
    @FXML
    private void handleBack() {
        // Handle back button
    }
}