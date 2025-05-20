package com.jawa.model.gameState;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jawa.model.gameComponent.Board;
import com.jawa.model.gameComponent.Movement;
import com.jawa.model.gameComponent.Piece;
import com.jawa.model.gameComponent.Position;

public class IO {
    public static class InvalidConfigException extends Exception {
        public InvalidConfigException(String message) {
            super(message);
        }
    }

    public static Board loadFromFile(File file) throws IOException, InvalidConfigException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String[] dim = br.readLine().trim().split("\\s+");
            if (dim.length != 2) {
                throw new InvalidConfigException("Error at line 1: Expected <row> <col>");
            }

            int declaredRows, declaredCols;
            try {
                declaredRows = Integer.parseInt(dim[0]);
                declaredCols = Integer.parseInt(dim[1]);
            } catch (NumberFormatException e) {
                throw new InvalidConfigException("Error at line 1: Row and column must be integers");
            }

            String secondLine = br.readLine();
            if (secondLine == null) {
                throw new InvalidConfigException("Error at line 2: Missing piece count");
            }

            int pieceCount;
            try {
                pieceCount = Integer.parseInt(secondLine.trim());
            } catch (NumberFormatException e) {
                throw new InvalidConfigException("Error at line 2: Piece count must be an integer");
            }

            List<String> allLines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                allLines.add(line);
            }

            Position exitPos = null;
            int actualRows = allLines.size();
            int offsetX = 0, offsetY = 0;
            int countK = 0;

            outerLoop: for (int r = 0; r < actualRows; r++) {
                String currentLine = allLines.get(r);
                for (int c = 0; c < currentLine.length(); c++) {
                    if (currentLine.charAt(c) == 'K') {
                        countK++;
                        if (countK > 1) {
                            throw new InvalidConfigException(
                                    "Error at line " + r + " : Invalid K (more than one K on config)");
                        }
                        if (r == 0) {
                            int exitRow = 0;
                            int exitCol = c;
                            offsetX = 0;

                            if (declaredCols + 1 <= currentLine.length()) {

                                if (c == 0) {
                                    offsetX = 1;
                                    System.out.println("ll");
                                    exitCol = c + 1;

                                } else {
                                    offsetX = 0;
                                }
                                offsetY = 0;
                            } else {
                                offsetY = 1;

                            }

                            exitPos = new Position(exitRow, exitCol);
                            System.out.println(exitRow + " " + exitCol);
                        } else if (c == 0) {
                            int exitCol = 1;
                            if (declaredCols + 1 <= currentLine.length()) {
                                offsetX = 1;
                                exitCol = 1;
                            } else {
                                offsetX = 0;
                                exitCol = c;
                            }
                            offsetY = 0;
                            int exitRow = r;
                            exitPos = new Position(exitRow, exitCol);
                            System.out.println(exitRow + " " + exitCol);
                        } else {
                            int exitRow = r;
                            int exitCol = c;
                            exitPos = new Position(exitRow, exitCol);
                            System.out.println(exitRow + " " + exitCol);
                        }
                        break outerLoop;
                    }
                }
            }

            List<String> boardLines = new ArrayList<>();
            for (int i = offsetY; i < declaredRows + offsetY; i++) {
                String rawLine = i < allLines.size() ? allLines.get(i) : "";
                StringBuilder processedLine = new StringBuilder();
                for (int c = offsetX; c < declaredCols + offsetX; c++) {
                    processedLine.append(
                            (c < rawLine.length()) ? (rawLine.charAt(c) == ' ' ? '.' : rawLine.charAt(c)) : '.');
                }
                boardLines.add(processedLine.toString());
            }

            Board board = new Board(declaredRows, declaredCols, pieceCount);
            if (exitPos != null)
                board.setExitPosition(exitPos);

            Map<Character, List<Position>> pieceMap = new HashMap<>();
            for (int r = 0; r < declaredRows; r++) {
                String boardRow = boardLines.get(r);
                for (int c = 0; c < declaredCols; c++) {
                    char ch = boardRow.charAt(c);
                    if (ch != '.' && ch != 'K') {
                        pieceMap.putIfAbsent(ch, new ArrayList<>());
                        pieceMap.get(ch).add(new Position(r, c));
                    }
                }
            }

            for (Map.Entry<Character, List<Position>> entry : pieceMap.entrySet()) {
                List<Position> positions = entry.getValue();
                positions.sort(Comparator.comparingInt(Position::getRow).thenComparingInt(Position::getCol));
                Position first = positions.get(0);
                boolean isHorizontal = positions.size() > 1 &&
                        positions.get(1).getRow() == first.getRow();

                Piece piece = new Piece(
                        String.valueOf(entry.getKey()),
                        first.getRow(), first.getCol(),
                        positions.size(),
                        isHorizontal,
                        entry.getKey() == 'P');
                board.addPiece(piece);
            }

            if (board.getPrimaryPiece() == null) {
                throw new InvalidConfigException("Invalid config : Primary Pieces not found");
            }

            if (board.getPrimaryPiece().isHorizontal()) {
                if (board.getPrimaryPiece().getRow() != exitPos.getRow()) {
                    throw new InvalidConfigException("Invalid config : Primary Piece and exit not alligned");
                }
            } else {
                if (board.getPrimaryPiece().getCol() != exitPos.getCol()) {
                    throw new InvalidConfigException("Invalid config : Primary Piece and exit not alligned");
                }
            }

            if (board.getPieces().size() - 1 != pieceCount) {
                throw new InvalidConfigException("Invalid config : Pieces count wrong");
            }

            return board;
        }
    }

    public static void saveResultToFile(Board initialBoard, Result result, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            Board currentBoard = new Board(initialBoard);

            Board finalBoard = new Board(initialBoard);
            List<Movement> moves = result.getMoves();
            for (Movement move : moves) {
                Piece piece = finalBoard.getPieces().get(move.getPieceId());
                piece.move(move.getDirection(), move.getDistance());
            }

            Piece primaryPiece = finalBoard.getPieces().get("P");
            Position exitPosition = determineExitPosition(finalBoard, primaryPiece);

            writer.write("Puzzle Solution\n");
            writer.write("==============\n");
            writer.write(String.format("Solving Time: %d ms\n", result.getSolvingTime()));
            writer.write(String.format("Nodes Expanded: %d\n", result.getNodesExpanded()));
            writer.write(String.format("Total Moves: %d\n\n", result.getMoves().size()));

            writer.write("Initial State\n");
            writer.write(boardToStringWithExit(currentBoard, exitPosition));
            writer.write("\n");

            for (int i = 0; i < moves.size(); i++) {
                Movement move = moves.get(i);
                Piece piece = currentBoard.getPieces().get(move.getPieceId());

                piece.move(move.getDirection(), move.getDistance());

                writer.write(String.format("Move %d: %s-%s %d\n",
                        i + 1,
                        move.getPieceId(),
                        getDirectionName(move.getDirection()),
                        move.getDistance()));
                writer.write(boardToStringWithExit(currentBoard, exitPosition));
                writer.write("\n");
            }
        }
    }

    private static Position determineExitPosition(Board solvedBoard, Piece primaryPiece) {
        int pr = primaryPiece.getRow();
        int pc = primaryPiece.getCol();

        if (primaryPiece.isHorizontal()) {
            if (pc == 0) {
                return new Position(pr, -1);
            } else if (pc + primaryPiece.getLength() == solvedBoard.getCols()) {
                return new Position(pr, solvedBoard.getCols());
            }
        } else {
            if (pr == 0) {
                return new Position(-1, pc);
            } else if (pr + primaryPiece.getLength() == solvedBoard.getRows()) {
                return new Position(solvedBoard.getRows(), pc);
            }
        }

        return new Position(2, -1);
    }

    private static String getDirectionName(String direction) {
        switch (direction) {
            case "R":
                return "right";
            case "L":
                return "left";
            case "U":
                return "up";
            case "D":
                return "down";
            default:
                return direction;
        }
    }

    private static String boardToStringWithExit(Board board, Position exitPos) {
        int rows = board.getRows();
        int cols = board.getCols();

        char[][] extendedGrid = new char[rows + 2][cols + 2];
        for (int r = 0; r < rows + 2; r++) {
            for (int c = 0; c < cols + 2; c++) {
                extendedGrid[r][c] = ' ';
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                extendedGrid[r + 1][c + 1] = '.';
            }
        }

        for (Piece piece : board.getPieces().values()) {
            String id = piece.getId();
            int row = piece.getRow();
            int col = piece.getCol();
            int length = piece.getLength();
            boolean isHorizontal = piece.isHorizontal();

            for (int i = 0; i < length; i++) {
                int r = row + (isHorizontal ? 0 : i);
                int c = col + (isHorizontal ? i : 0);

                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    extendedGrid[r + 1][c + 1] = id.charAt(0);
                }
            }
        }

        if (exitPos != null) {
            int exitRow = exitPos.getRow();
            int exitCol = exitPos.getCol();

            if (exitRow < 0) {
                extendedGrid[0][exitCol + 1] = 'K';
            } else if (exitRow >= rows) {
                extendedGrid[rows + 1][exitCol + 1] = 'K';
            } else if (exitCol < 0) {
                extendedGrid[exitRow + 1][0] = 'K';
            } else if (exitCol >= cols) {
                extendedGrid[exitRow + 1][cols + 1] = 'K';
            }
        } else {
            extendedGrid[3][0] = 'K';
        }
        StringBuilder result = new StringBuilder();
        for (int r = 0; r < rows + 2; r++) {
            for (int c = 0; c < cols + 2; c++) {
                result.append(extendedGrid[r][c]);
            }
            result.append('\n');
        }

        return result.toString();
    }
}
