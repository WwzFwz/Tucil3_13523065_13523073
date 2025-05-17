package com.jawa.model.algorithm;

import com.jawa.model.gameComponent.Board;
import com.jawa.model.gameComponent.Movement;
import com.jawa.model.gameComponent.Piece;
import com.jawa.model.gameState.Result;

import java.util.*;

public class GreatSolver {
    private Heuristic heuristic;

    public GreatSolver(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    // Node represents a state in the search tree
    private static class Node {
        Board board;
        List<Movement> path; // moves to reach this state
        int g; // cost from start to this node
        int f; // total cost (g + h)

        Node(Board board, List<Movement> path, int g, int f) {
            this.board = board;
            this.path = path;
            this.g = g;
            this.f = f;
        }
    }

    public Result solve(Board initial) {
        long startTime = System.currentTimeMillis();

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Set<String> closed = new HashSet<>(); // store board states as string

        int nodesExpanded = 0;

        List<Movement> initialPath = new ArrayList<>();
        int h = heuristic.estimate(initial);
        Node startNode = new Node(initial, initialPath, 0, h);
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

            // Generate all valid children (next board states) and their moves
            for (var child : generateChildren(current)) {
                if (closed.contains(child.board.toString()))
                    continue;

                int gNew = current.g + 1; // cost per move = 1
                int hNew = heuristic.estimate(child.board);
                int fNew = gNew + hNew;

                List<Movement> newPath = new ArrayList<>(current.path);
                newPath.add(child.move);

                Node newNode = new Node(child.board, newPath, gNew, fNew);
                open.add(newNode);
            }
        }

        // No solution
        return null;
    }

    // Child node with board and movement to reach it
    private static class ChildNode {
        Board board;
        Movement move;

        ChildNode(Board board, Movement move) {
            this.board = board;
            this.move = move;
        }
    }

    // Generate all possible next board states with moves from current node
    private List<ChildNode> generateChildren(Node node) {
        List<ChildNode> children = new ArrayList<>();
        Board board = node.board;

        // For each piece, generate possible moves on the board (distance = 1 step)
        for (var piece : board.getPieces().values()) {
            // Try move piece in all valid directions (depends on orientation)
            for (String dir : piece.isHorizontal() ? new String[] { "L", "R" } : new String[] { "U", "D" }) {
                Board newBoard = board.deepCopy(); // You need to implement deep copy
                Piece movedPiece = newBoard.getPieces().get(piece.getId());
                if (canMove(newBoard, movedPiece, dir, 1)) {
                    Movement move = movedPiece.move(dir, 1);
                    children.add(new ChildNode(newBoard, move));
                }
            }
        }

        return children;
    }

    // Check if piece can move 'distance' in direction on given board
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
