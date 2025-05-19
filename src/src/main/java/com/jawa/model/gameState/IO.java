package com.jawa.model.gameState;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jawa.model.gameComponent.Board;
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
                        offsetX = 1;
                        offsetY = 0;
                        int exitRow = r;
                        int exitCol = 1;
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

        return board;
    }
}
