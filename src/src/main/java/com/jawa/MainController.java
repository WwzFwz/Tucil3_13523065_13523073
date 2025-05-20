package com.jawa;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.jawa.model.algorithm.AStarSolver;
import com.jawa.model.algorithm.BlockingHeuristic;
import com.jawa.model.algorithm.GreatSolver;
import com.jawa.model.algorithm.GreedySolver;
import com.jawa.model.algorithm.Heuristic;
import com.jawa.model.algorithm.ShortestHeuristic;
import com.jawa.model.algorithm.Solver;
import com.jawa.model.algorithm.UCLSolver;
import com.jawa.model.algorithm.ZeroHeuristic;
import com.jawa.model.gameComponent.Board;
import com.jawa.model.gameComponent.Movement;
import com.jawa.model.gameComponent.Piece;
import com.jawa.model.gameComponent.Position;
import com.jawa.model.gameState.IO;
import com.jawa.model.gameState.Result;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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
import javafx.util.Duration;

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
    private Button saveButton;

    private boolean fileUploaded = false;
    private File selectedFile;
    private Board board;
    private Board originalBoard;
    private Result result;
    private final int stepsPerPage = 12;
    private Timeline playbackTimeline;
    private boolean isPlaying = false;
    private int currentStepIndex = -1;

    @FXML
    private void initialize() {
        initializeBoard(6, 6);

        algorithmComboBox.getItems().addAll(
                "A*",
                "Greedy Best-First",
                "Uniform-Cost Search");

        algorithmComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateHeuristicOptions(newValue);
        });

        heuristicComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSolveButtonState();
        });

        stepsPagination.setPageCount(1);
        stepsPagination.setCurrentPageIndex(0);
        stepsPagination.setMaxPageIndicatorCount(5);

        stepsPagination.currentPageIndexProperty().addListener((obs, oldVal, newVal) -> {
            updateStepsListView(newVal.intValue());
        });

        saveButton.setDisable(true);
        stepsListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0 && result != null && result.getMovements() != null) {
                int stepIndex = stepsPagination.getCurrentPageIndex() * stepsPerPage + newVal.intValue();
                if (stepIndex < result.getMovements().size()) {
                    showSolutionStep(stepIndex);
                }
            }
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
        } else {
            solveButton.setDisable(false);
        }
        switch (algorithm) {
            case "A*":
                heuristicComboBox.setDisable(false);
                heuristicComboBox.setPromptText("Select Heuristic");
                heuristicComboBox.getItems().addAll(
                        "Shortest Distance",
                        "Blocking Pieces");
                break;

            case "Greedy Best-First":
                heuristicComboBox.setDisable(false);
                heuristicComboBox.getItems().addAll(
                        "Shortest Distance",
                        "Blocking Pieces");
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
        } else {
            solveButton.setDisable(false);
        }
    }

    private void updateStepsListView(int pageIndex) {
        System.err.println("Updating ListView for page " + pageIndex);
        stepsListView.getItems().clear();

        if (result == null || result.getMovements().isEmpty()) {
            return;
        }

        int startIndex = pageIndex * stepsPerPage;
        int endIndex = Math.min(startIndex + stepsPerPage, result.getMovements().size());

        for (int i = startIndex; i < endIndex; i++) {
            Movement step = result.getMovements().get(i);
            stepsListView.getItems().add("Step " + (i + 1) + ": " + step.toString());
            System.err.println(step);
        }

        if (currentStepIndex >= startIndex && currentStepIndex < endIndex) {
            stepsListView.getSelectionModel().select(currentStepIndex - startIndex);
        }
    }

    @FXML
    private void handleUploadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Puzzle File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        try {
            board = IO.loadFromFile(selectedFile);
        } catch (Exception e) {
            fileNameLabel.setText("Error Occured : " + e.getMessage());
        }
        initializeBoard(board.getRow(), board.getCol());
        originalBoard = board.deepCopy();
        displayBoard(board);
        fileUploaded = true;
        fileNameLabel.setText("File: " + selectedFile.getName());
        algorithmComboBox.setDisable(false);
        algorithmComboBox.setPromptText("Select Algorithm");
        updateHeuristicOptions(algorithmComboBox.getValue());
        solveButton.setDisable(true);
    }
    @FXML
    private void handleSolve() {

        if (result != null)
            result = null;

        String algorithm = algorithmComboBox.getValue();
        String heuristicChoice = heuristicComboBox.getValue();
        currentStepIndex = -1;
        board = originalBoard.deepCopy();

        Heuristic selectedHeuristic;

        if (heuristicChoice != null) {
            switch (heuristicChoice) {
                case "Shortest Distance":
                    selectedHeuristic = new ShortestHeuristic();
                    break;
                case "Blocking Pieces":
                    selectedHeuristic = new BlockingHeuristic();
                    break;
                default:
                    selectedHeuristic = new ZeroHeuristic();
            }
        } else {

            selectedHeuristic = new ZeroHeuristic();
        }

        Solver solver;
        if (algorithm == null) {
            solver = new GreatSolver(selectedHeuristic);
        } else {
            switch (algorithm) {
                case "A*":
                    solver = new AStarSolver(selectedHeuristic);
                    break;
                case "Greedy Best-First":
                    solver = new GreedySolver(selectedHeuristic);
                    break;
                case "Uniform-Cost Search":
                    solver = new UCLSolver();
                    break;
                default:
                    solver = new GreatSolver(selectedHeuristic);
            }
        }

        try {

            statusLabel.setText("Solving puzzle...");
            result = solver.solve(board);
            if (result == null || result.getMovements() == null || result.getMovements().isEmpty()) {
                statusLabel.setText("No solution found.");
                return;
            }
            saveButton.setDisable(false);
            int totalPages = (int) Math.ceil((double) result.getMovements().size() / stepsPerPage);
            stepsPagination.setPageCount(Math.max(1, totalPages));
            stepsPagination.setCurrentPageIndex(0);

            nextButton.setDisable(result.getMovements().size() <= 1);
            playPauseButton.setDisable(false);

            updateStepsListView(0);
            statusLabel.setText( result.getNodesExpanded()+ " Nodes explored" + 
                    " in " + result.getSolvingTime() + "ms");
            // munculin board saat ini
            displayBoard(board);
            solveButton.setDisable(true);

            // startPlayback();
        } catch (Exception e) {
            // e.printStackTrace();
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        if (currentStepIndex > 0) {
            showSolutionStep(currentStepIndex - 1);
        }
    }

    @FXML
    private void handleNext() {
        List<Movement> solutionSteps = result.getMovements();
        if (solutionSteps != null && currentStepIndex < solutionSteps.size() - 1) {
            showSolutionStep(currentStepIndex + 1);
        }
    }

    @FXML
    private void handlePlayPause() {
        List<Movement> solutionSteps = result.getMovements();
        if (solutionSteps == null || solutionSteps.isEmpty()) {
            return;
        }

        if (isPlaying) {

            playPauseButton.setText("▶");
            if (playbackTimeline != null) {
                playbackTimeline.stop();
            }
            isPlaying = false;
        } else {
            isPlaying = true;
            playPauseButton.setText("⏸");
            startPlayback();

        }
    }

    private void startPlayback() {
        if (playbackTimeline != null) {
            playbackTimeline.stop();
        }

        playbackTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    List<Movement> movements = result.getMovements();
                    if (movements == null || movements.isEmpty()) {
                        playbackTimeline.stop();
                        isPlaying = false;
                        playPauseButton.setText("▶");
                        return;
                    }

                    if (currentStepIndex < movements.size() - 1) {
                        currentStepIndex++; 
                        showSolutionStep(currentStepIndex);
                    } else {
                        playbackTimeline.stop();
                        isPlaying = false;
                        playPauseButton.setText("▶");
                    }
                }));

        playbackTimeline.setCycleCount(Timeline.INDEFINITE);
        playbackTimeline.play();
        isPlaying = true;
        playPauseButton.setText("⏸"); 
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
        if (availableWidth <= 0)
            availableWidth = boardPane.getPrefWidth();
        if (availableHeight <= 0)
            availableHeight = boardPane.getPrefHeight();

        if (availableWidth <= 0)
            availableWidth = 400;
        if (availableHeight <= 0)
            availableHeight = 400;

        availableWidth -= 40;
        availableHeight -= 40;

        double maxCellWidth = availableWidth / cols;
        double maxCellHeight = availableHeight / rows;

        double cellSize = Math.min(maxCellWidth, maxCellHeight);

        if (cellSize > 60)
            cellSize = 60;

        if (cellSize < 20)
            cellSize = 20;
        return cellSize;
    }

    private void displayBoard(Board board) {
        GridPane boardGrid = (GridPane) boardPane.getChildren().get(0);
        clearPieces(boardGrid);
        double cellSize = calculateCellSize(board.getRows(), board.getCols());
        Map<String, Piece> pieces = board.getPieces();
        for (Piece piece : pieces.values()) {
            displaySinglePiece(boardGrid, piece, cellSize);
        }
    }

    private void clearPieces(GridPane boardGrid) {
        boardGrid.getChildren().removeIf(node -> !(node instanceof StackPane) ||
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

    private void updateSinglePiece(String pieceId) {
        GridPane boardGrid = (GridPane) boardPane.getChildren().get(0);
        Piece piece = board.getPieces().get(pieceId);

        if (piece == null)
            return;

        StackPane pieceNode = null;
        for (Node node : boardGrid.getChildren()) {
            if (node instanceof StackPane &&
                    node.getId() != null &&
                    node.getId().equals(pieceId)) {
                pieceNode = (StackPane) node;
                break;
            }
        }

        if (pieceNode != null) {
            boardGrid.getChildren().remove(pieceNode);

            Position pos = piece.getPosition();
            if (piece.isHorizontal()) {
                GridPane.setColumnSpan(pieceNode, piece.getLength());
                boardGrid.add(pieceNode, pos.getCol(), pos.getRow());
            } else {
                GridPane.setRowSpan(pieceNode, piece.getLength());
                boardGrid.add(pieceNode, pos.getCol(), pos.getRow());
            }
        } else {
            double cellSize = calculateCellSize(board.getRows(), board.getCols());
            displaySinglePiece(boardGrid, piece, cellSize);
        }
    }

    private void showSolutionStep(int stepIndex) {
        List<Movement> solutionSteps = result.getMovements();
        if (solutionSteps == null || stepIndex < 0 || stepIndex >= solutionSteps.size()) {
            return;
        }

        // Selalu rebuild board dari awal sampai stepIndex
        board = originalBoard.deepCopy();
        for (int i = 0; i <= stepIndex; i++) {
            Movement move = solutionSteps.get(i);
            Piece piece = board.getPieces().get(move.getPieceId());
            if (piece != null) {
                piece.move(move.getDirection(), move.getDistance());
            }
        }
        displayBoard(board);

        currentStepIndex = stepIndex;

        int pageIndex = stepIndex / stepsPerPage;
        if (pageIndex != stepsPagination.getCurrentPageIndex()) {
            stepsPagination.setCurrentPageIndex(pageIndex);
        } else {
            int listIndex = stepIndex % stepsPerPage;
            stepsListView.getSelectionModel().select(listIndex);
        }

        backButton.setDisable(stepIndex == 0);
        nextButton.setDisable(stepIndex == solutionSteps.size() - 1);

        // statusLabel.setText("Step " + (stepIndex + 1) + " of " + solutionSteps.size());
    }
    @FXML
    private void handleSaveSolution() {
        if (result == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No solution available to save.");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Solution");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        
        // Set initial filename based on the loaded puzzle file name
        String initialFileName = "solution.txt";
        fileChooser.setInitialFileName(initialFileName);
        
        File file = fileChooser.showSaveDialog(saveButton.getScene().getWindow());
        if (file != null) {
            try {
                IO.saveResultToFile(originalBoard, result, file);
                
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                    "Solution saved successfully to: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", 
                    "Failed to save solution: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
// Add a helper method for showing alerts
private void showAlert(Alert.AlertType type, String title, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
}
}