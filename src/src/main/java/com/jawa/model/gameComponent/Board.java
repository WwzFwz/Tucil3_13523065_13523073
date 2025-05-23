package com.jawa.model.gameComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Board {
    private int rows;
    private int cols;
    private int countPiece;
    private Map<String, Piece> pieces;
    private Position exitPosition;

    public Board(int rows, int cols, int countPiece) {
        this.rows = rows;
        this.cols = cols;
        this.countPiece = countPiece;
        this.pieces = new HashMap<>();
    }

    public Board(Board other) {
        this.rows = other.rows;
        this.cols = other.cols;
        this.countPiece = other.countPiece;
        this.exitPosition = other.exitPosition != null ? new Position(other.exitPosition) : null;
        this.pieces = new HashMap<>();
        for (Map.Entry<String, Piece> e : other.pieces.entrySet()) {
            this.pieces.put(e.getKey(), new Piece(e.getValue()));
        }
    }

    public void addPiece(Piece p) {
        pieces.put(p.getId(), p);
    }

    public boolean isSolved() {
        Piece primary = pieces.get("P");
        if (primary == null || exitPosition == null)
            return false;

        int exitRow = exitPosition.getRow();
        int exitCol = exitPosition.getCol();
        int pr = primary.getRow();
        int pc = primary.getCol();
        int tailRow = pr;
        int tailCol = pc;

        if (primary.isHorizontal()) {
            tailCol = pc + primary.getLength();
            if (pr == exitRow && tailCol == exitCol) {
                System.err.println("1");
                return true;
            }
            if (pr == exitRow && exitCol == pc + 1) {
                System.err.println("2");
                return true;
            }

        } else {
            tailRow = pr + primary.getLength();
            if (pc == exitCol && tailRow == exitRow) {
                System.err.println("3");
                return true;
            }
            if (pc == exitCol && exitRow == pr) {
                System.err.println("4");
                return true;
            }
        }

        return false;
    }

    public boolean isCellEmpty(int r, int c) {
        for (Piece p : pieces.values()) {
            int rr = p.getRow();
            int cc = p.getCol();
            for (int i = 0; i < p.getLength(); i++) {
                int tr = rr + (!p.isHorizontal() ? i : 0);
                int tc = cc + (p.isHorizontal() ? i : 0);
                if (tr == r && tc == c)
                    return false;
            }
        }
        return true;
    }

    public boolean canMove(Piece p, String direction, int distance) {
        if (distance <= 0)
            return false;

        int dr = 0, dc = 0;
        switch (direction) {
            case "R":
                dc = 1;
                break;
            case "L":
                dc = -1;
                break;
            case "U":
                dr = -1;
                break;
            case "D":
                dr = 1;
                break;
            default:
                return false;
        }

        if ((p.isHorizontal() && (direction.equals("U") || direction.equals("D"))) ||
                (!p.isHorizontal() && (direction.equals("L") || direction.equals("R")))) {
            return false;
        }

        int startRow = p.getRow();
        int startCol = p.getCol();

        for (int step = 1; step <= distance; step++) {
            int checkRow, checkCol;

            if (direction.equals("R")) {
                checkRow = startRow;
                checkCol = startCol + p.getLength() - 1 + step;
            } else if (direction.equals("L")) {
                checkRow = startRow;
                checkCol = startCol - step;
            } else if (direction.equals("U")) {
                checkRow = startRow - step;
                checkCol = startCol;
            } else {
                checkRow = startRow + p.getLength() - 1 + step;
                checkCol = startCol;
            }

            if (checkRow < 0 || checkRow >= rows || checkCol < 0 || checkCol >= cols)
                return false;

            if (!isCellEmpty(checkRow, checkCol)) {
                return false;
            }
        }

        return true;
    }

    public Movement movePiece(Piece p, String direction, int distance) {
        if (!canMove(p, direction, distance)) {
            throw new IllegalArgumentException("Invalid move");
        }
        return p.move(direction, distance);
    }

    public List<Movement> getAllPossibleMoves() {
        List<Movement> moves = new ArrayList<>();
        String[] directions = { "R", "L", "U", "D" };

        for (Piece p : pieces.values()) {
            for (String dir : directions) {
                for (int dist = 1; dist < Math.max(rows, cols); dist++) {
                    if (canMove(p, dir, dist)) {
                        moves.add(new Movement(p.getId(), dir, dist));
                    } else {
                        break;
                    }
                }
            }
        }
        return moves;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Board))
            return false;

        Board other = (Board) obj;
        if (rows != other.rows || cols != other.cols)
            return false;

        if (!Objects.equals(exitPosition, other.exitPosition))
            return false;

        for (String key : pieces.keySet()) {
            Piece p1 = pieces.get(key);
            Piece p2 = other.pieces.get(key);
            if (p2 == null)
                return false;
            if (p1.getRow() != p2.getRow())
                return false;
            if (p1.getCol() != p2.getCol())
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(rows, cols, exitPosition);
        for (Piece p : pieces.values()) {
            hash = hash * 31 + p.getRow();
            hash = hash * 31 + p.getCol();
        }
        return hash;
    }

    public Board deepCopy() {
        Board copy = new Board(this.rows, this.cols, this.countPiece);
        copy.setExitPosition(new Position(this.exitPosition));

        for (Piece p : this.pieces.values()) {
            copy.addPiece(new Piece(p));
        }
        return copy;
    }

    public Position getFinishPosition() {
        return exitPosition;
    }

    public void setFinishPosition(Position exitPosition) {
        this.exitPosition = exitPosition;
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Board(").append(rows).append("x").append(cols).append("), Exit=").append(exitPosition).append("\n");
        for (Piece p : pieces.values()) {
            sb.append(p).append("\n");
        }
        return sb.toString();
    }

    public void reverseMovement(Movement movement) {
        Piece piece = pieces.get(movement.getPieceId());
        if (piece == null)
            return;

        String oppositeDirection = getOppositeDirection(movement.getDirection());
        int distance = movement.getDistance();

        piece.move(oppositeDirection, distance);
    }

    public String getOppositeDirection(String direction) {
        switch (direction) {
            case "R":
                return "L";
            case "L":
                return "R";
            case "U":
                return "D";
            case "D":
                return "U";
            default:
                return direction;
        }
    }

    public Piece getPrimaryPiece() {
        return pieces.get("P");
    }

    public char[][] getBoardState() {
        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            Arrays.fill(grid[r], '.');
        }

        for (Piece p : pieces.values()) {
            int pr = p.getRow();
            int pc = p.getCol();
            for (int i = 0; i < p.getLength(); i++) {
                int r = pr + (!p.isHorizontal() ? i : 0);
                int c = pc + (p.isHorizontal() ? i : 0);
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    grid[r][c] = p.getId().charAt(0);
                }
            }
        }
        return grid;
    }

    public int getRow() {
        return this.rows;
    }

    public int getCol() {
        return this.cols;
    }

    public Position getExitPosition() {
        return exitPosition;
    }

    public void setExitPosition(Position exitPosition) {
        this.exitPosition = exitPosition;
    }

    public Map<String, Piece> getPieces() {
        return pieces;
    }

    public void printBoard() {
        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            Arrays.fill(grid[r], '.');
        }

        for (Piece p : pieces.values()) {
            int pr = p.getRow();
            int pc = p.getCol();
            for (int i = 0; i < p.getLength(); i++) {
                int r = pr + (!p.isHorizontal() ? i : 0);
                int c = pc + (p.isHorizontal() ? i : 0);
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    grid[r][c] = p.getId().charAt(0);
                }
            }
        }

        if (exitPosition != null &&
                (exitPosition.getRow() < 0 || exitPosition.getRow() >= rows ||
                        exitPosition.getCol() < 0 || exitPosition.getCol() >= cols)) {
        }

        for (int r = 0; r < rows; r++) {
            StringBuilder line = new StringBuilder();
            for (int c = 0; c < cols; c++) {
                line.append(colorize(grid[r][c]));
            }
            System.out.println(line);
        }

    }

    private static final String[] ANSI_COLORS = {
            "\u001B[31m",
            "\u001B[32m",
            "\u001B[33m",
            "\u001B[34m",
            "\u001B[35m",
            "\u001B[36m",
            "\u001B[91m",
            "\u001B[92m",
            "\u001B[93m",
            "\u001B[94m",
            "\u001B[95m",
            "\u001B[96m",
            "\u001B[37m",
    };

    private static String colorize(char ch) {
        if (ch == '.')
            return ".";
        if (ch == 'P')
            return "\u001B[35mP\u001B[0m";
        int idx = (ch - 'A') % ANSI_COLORS.length;
        return ANSI_COLORS[idx] + ch + "\u001B[0m";
    }
}
