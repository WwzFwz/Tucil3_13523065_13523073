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


    // public static void main(String[] args) throws IOException {
    //     Board board = loadFromFile("path/to/config.txt");
    //     // board.printBoard(); // Tambahkan fungsi ini di kelas Board kalau belum ada
    //     System.out.println("Exit at: " + board.getExitPosition().getRow() + "," + board.getExitPosition().getCol());
    // }
}
