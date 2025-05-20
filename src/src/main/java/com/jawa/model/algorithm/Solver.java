package com.jawa.model.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.jawa.model.gameComponent.Board;
import com.jawa.model.gameComponent.Movement;
import com.jawa.model.gameComponent.Piece;
import com.jawa.model.gameState.Result;

public abstract class Solver {
    public abstract Result solve(Board initial);

    public Solver() {
    }

    protected static class Node {
        Board board;
        List<Movement> path;
        int g;
        int f;
        int h;

        Node(Board board, List<Movement> path, int g, int f) {
            this.board = board;
            this.path = path;
            this.g = g;
            this.f = f;
        }

        Node(Board board, List<Movement> path, int n, boolean t) {
            this.board = board;
            this.path = path;
            if (t) {
                this.h = n;
            } else {
                this.g = n;
            }

        }

    }

    protected static class ChildNode {
        Board board;
        Movement move;

        ChildNode(Board board, Movement move) {
            this.board = board;
            this.move = move;
        }
    }

    protected List<ChildNode> generateChildren(Node node) {
        List<ChildNode> children = new ArrayList<>();
        Board board = node.board;

        for (var piece : board.getPieces().values()) {
            for (String dir : piece.isHorizontal() ? new String[] { "L", "R" } : new String[] { "U", "D" }) {
                for (int distance = 1; distance < Math.max(board.getRow(), board.getCol()); distance++) {
                    Board newBoard = board.deepCopy();
                    Piece movedPiece = newBoard.getPieces().get(piece.getId());

                    if (canMove(newBoard, movedPiece, dir, distance)) {
                        Movement move = movedPiece.move(dir, distance);
                        children.add(new ChildNode(newBoard, move));
                    } else {

                        break;
                    }
                }
            }

        }

        return children;
    }

    protected boolean canMove(Board board, Piece piece, String direction, int distance) {
        int r = piece.getRow();
        int c = piece.getCol();

        switch (direction) {
            case "L":
                if (!piece.isHorizontal())
                    return false;
                for (int step = 1; step <= distance; step++) {
                    if (c - step < 0 || !board.isCellEmpty(r, c - step))
                        return false;
                }
                break;
            case "R":
                if (!piece.isHorizontal())
                    return false;
                for (int step = 1; step <= distance; step++) {
                    int checkCol = c + piece.getLength() - 1 + step;
                    if (checkCol >= board.getCol() || !board.isCellEmpty(r, checkCol))
                        return false;
                }
                break;
            case "U":
                if (piece.isHorizontal())
                    return false;
                for (int step = 1; step <= distance; step++) {
                    if (r - step < 0 || !board.isCellEmpty(r - step, c))
                        return false;
                }
                break;
            case "D":
                if (piece.isHorizontal())
                    return false;
                for (int step = 1; step <= distance; step++) {
                    int checkRow = r + piece.getLength() - 1 + step;
                    if (checkRow >= board.getRow() || !board.isCellEmpty(checkRow, c))
                        return false;
                }
                break;
        }

        return true;
    }
}