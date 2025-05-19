package com.jawa.model.algorithm;

import java.util.*;

import com.jawa.model.gameComponent.Board;
import com.jawa.model.gameComponent.Movement;
import com.jawa.model.gameComponent.Piece;
import com.jawa.model.gameState.Result;

public class UCLSolver implements Solver {

    private static class Node {
        Board board;
        List<Movement> path;
        int g; // cost so far

        Node(Board board, List<Movement> path, int g) {
            this.board = board;
            this.path = path;
            this.g = g;
        }
    }

    private static class ChildNode {
        Board board;
        Movement move;

        ChildNode(Board board, Movement move) {
            this.board = board;
            this.move = move;
        }
    }

    @Override
    public Result solve(Board initial) {
        long startTime = System.currentTimeMillis();

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.g));
        Set<String> closed = new HashSet<>();
        int nodesExpanded = 0;

        Node startNode = new Node(initial, new ArrayList<>(), 0);
        open.add(startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();
            nodesExpanded++;

            if (current.board.isSolved()) {
                long endTime = System.currentTimeMillis();
                Result result = new Result();
                result.setMoves(current.path);
                result.setSolvingTime(endTime - startTime);
                result.setNodesExpanded(nodesExpanded);
                return result;
            }

            String stateKey = current.board.toString();
            if (closed.contains(stateKey))
                continue;
            closed.add(stateKey);

            for (var child : generateChildren(current)) {
                if (closed.contains(child.board.toString()))
                    continue;

                int gNew = current.g + 1;
                List<Movement> newPath = new ArrayList<>(current.path);
                newPath.add(child.move);
                open.add(new Node(child.board, newPath, gNew));
            }
        }

        return null;
    }

    private List<ChildNode> generateChildren(Node node) {
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
                        // begitu ketemu satu langkah yang tidak valid, tidak perlu coba lebih jauh
                        break;
                    }
                }
            }

        }

        return children;
    }

    private boolean canMove(Board board, Piece piece, String direction, int distance) {
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
