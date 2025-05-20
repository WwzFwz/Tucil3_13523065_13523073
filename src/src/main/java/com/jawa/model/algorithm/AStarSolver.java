package com.jawa.model.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.jawa.model.gameComponent.Board;
import com.jawa.model.gameComponent.Movement;
import com.jawa.model.gameComponent.Piece;
import com.jawa.model.gameState.Result;
import com.jawa.model.heuristic.Heuristic;

public class AStarSolver extends Solver {
    private Heuristic heuristic;

    public AStarSolver(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public Result solve(Board initial) {
        long startTime = System.currentTimeMillis();

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Set<String> closed = new HashSet<>();
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

            for (var child : generateChildren(current)) {
                if (closed.contains(child.board.toString()))
                    continue;

                int gNew = current.g + 1;
                int hNew = heuristic.estimate(child.board);
                int fNew = gNew + hNew;

                List<Movement> newPath = new ArrayList<>(current.path);
                newPath.add(child.move);

                Node newNode = new Node(child.board, newPath, gNew, fNew);
                open.add(newNode);
            }
        }

        return null;
    }

}
