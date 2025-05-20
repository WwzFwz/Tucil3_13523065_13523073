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

    public static Board loadFromFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String[] dim = br.readLine().split(" ");
        int rows = Integer.parseInt(dim[0]);
        int cols = Integer.parseInt(dim[1]);

        int pieceCount = Integer.parseInt(br.readLine());

        List<String> lines = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            lines.add(br.readLine());
        }
        br.close();

        Board board = new Board(rows, cols, pieceCount);
        char[][] grid = new char[rows][];
        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        Map<Character, List<Position>> piecePositions = new HashMap<>();

        // Cari semua karakter dan simpan posisinya, kecuali '.' dan 'K'
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                char ch = grid[r][c];
                if (ch == '.')
                    continue;
                if (ch == 'K') {
                    if (r < 0 || r >= rows || c < 0 || c >= cols ||
                            r == 0 || r == rows - 1 || c == 0 || c == cols - 1) {
                        board.setExitPosition(new Position(r, c));
                    }
                    continue;
                }
                piecePositions.putIfAbsent(ch, new ArrayList<>());
                piecePositions.get(ch).add(new Position(r, c));
            }
        }

        // Buat Piece dari kumpulan posisi
        for (Map.Entry<Character, List<Position>> entry : piecePositions.entrySet()) {
            char id = entry.getKey();
            List<Position> posList = entry.getValue();
            posList.sort(Comparator.comparingInt(Position::getRow).thenComparingInt(Position::getCol));
            Position first = posList.get(0);

            boolean isHorizontal = posList.size() > 1 && posList.get(1).getRow() == first.getRow();
            int length = posList.size();
            boolean isPrimary = id == 'P';

            Piece piece = new Piece(String.valueOf(id), first.getRow(), first.getCol(), length, isHorizontal,
                    isPrimary);
            board.addPiece(piece);
        }

        return board;
    }

    public static Board loadFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] dim = br.readLine().trim().split("\\s+");
        int declaredRows = Integer.parseInt(dim[0]);
        int declaredCols = Integer.parseInt(dim[1]);

        int pieceCount = Integer.parseInt(br.readLine().trim());

        // Baca semua baris tanpa melakukan trim
        List<String> allLines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            allLines.add(line);
        }
        br.close();

        // Cari posisi exit (K) di seluruh file
        Position exitPos = null;
        int actualRows = allLines.size();
        int offsetX = 0;
        int offsetY = 0;

        // Loop melalui setiap karakter di semua baris
        outerLoop: for (int r = 0; r < actualRows; r++) {
            String currentLine = allLines.get(r);
            for (int c = 0; c < currentLine.length(); c++) {
                if (currentLine.charAt(c) == 'K') {
                    // Tentukan posisi relatif terhadap board yang dideklarasikan
                    boolean isInsideBoard = (r < declaredRows) && (c < declaredCols);
                    if (r == 0) {
                        offsetX = 0;
                        offsetY = 1;
                        int exitRow = 0;
                        int exitCol = c;
                        exitPos = new Position(exitRow, exitCol);
                        System.out.println(exitRow + " " + exitCol);
                    } else if (c == 0) {
                        // offsetX = 1;
                        // offsetY = 0;
                        // int exitRow = r;
                        // int exitCol = 1;
                        // exitPos = new Position(exitRow, exitCol);
                        int exitCol = 1;
                        if (declaredCols + 1 <= currentLine.length()) {
                            offsetX = 1;
                            exitCol = 0;
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

                    // if (isInsideBoard) {
                    // // Pastikan K berada di tepi board
                    // if (r == 0 || r == declaredRows - 1 || c == 0 || c == declaredCols - 1) {
                    // exitPos = new Position(r, c);
                    // break outerLoop;
                    // }
                    // } else {
                    // // Hitung posisi exit di luar board
                    // int exitRow = (r < declaredRows) ? r : (r < 0 ? -1 : declaredRows);
                    // int exitCol = (c < declaredCols) ? -1 : declaredCols;
                    // exitPos = new Position(exitRow, exitCol);
                    // break outerLoop;
                    // }
                }
            }
        }

        // Bangun grid resmi dengan konversi spasi ke '.'
        List<String> boardLines = new ArrayList<>();
        for (int i = offsetY; i < declaredRows + offsetY; i++) {
            String rawLine = i < allLines.size() ? allLines.get(i) : "";
            StringBuilder processedLine = new StringBuilder();

            // Konversi karakter per kolom
            for (int c = offsetX; c < declaredCols + offsetX; c++) {
                if (c < rawLine.length()) {
                    char ch = rawLine.charAt(c);
                    processedLine.append(ch == ' ' ? '.' : ch);
                } else {
                    processedLine.append('.'); // Padding
                }
            }
            boardLines.add(processedLine.toString());
        }

        Board board = new Board(declaredRows, declaredCols, pieceCount);
        if (exitPos != null) {
            board.setExitPosition(exitPos);
        }

        // Proses piece dari grid yang sudah dibersihkan
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

        // Bangun objek Piece
        for (Map.Entry<Character, List<Position>> entry : pieceMap.entrySet()) {
            List<Position> positions = entry.getValue();
            positions.sort(Comparator.comparingInt(Position::getRow).thenComparingInt(Position::getCol));

            Position first = positions.get(0);
            boolean isHorizontal = positions.size() > 1 &&
                    positions.get(1).getRow() == first.getRow();

            Piece piece = new Piece(
                    String.valueOf(entry.getKey()),
                    first.getRow(),
                    first.getCol(),
                    positions.size(),
                    isHorizontal,
                    entry.getKey() == 'P');
            board.addPiece(piece);
        }
            // Map<String,Piece> pieces = board.getPieces();
            // for (Map.Entry<String, Piece> entry  : pieces.entrySet()) {

            //     System.out.println(entry.getValue());
            // }
            // System.out.println(board.getExitPosition());;

            return board;
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
                i+1, 
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
        case "R": return "right";
        case "L": return "left";
        case "U": return "up";
        case "D": return "down";
        default: return direction;
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
