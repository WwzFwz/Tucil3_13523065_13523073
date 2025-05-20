package com.jawa.model.algorithm;

import java.util.*;

import com.jawa.model.gameComponent.Board;
import com.jawa.model.gameComponent.Movement;
import com.jawa.model.gameComponent.Piece;
import com.jawa.model.gameState.Result;

public class UCSSolver extends Solver {

    @Override
    public Result solve(Board initial) {
        long startTime = System.currentTimeMillis();

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.g));
        Set<String> closed = new HashSet<>();
        int nodesExpanded = 0;

        Node startNode = new Node(initial, new ArrayList<>(), 0, false);
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
                open.add(new Node(child.board, newPath, gNew, false));
            }
        }

        return null;
    }

}
