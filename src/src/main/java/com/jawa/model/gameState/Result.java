package com.jawa.model.gameState;

import java.util.List;

import com.jawa.model.gameComponent.Movement;

public class Result {
    private List<Movement> moves;
    private long solvingTime;
    private int nodesExpanded;

    public Result() {
    }

    public Result(List<Movement> moves, long solvingTime, int nodesExpanded) {
        this.moves = moves;
        this.solvingTime = solvingTime;
        this.nodesExpanded = nodesExpanded;
    }

    public List<Movement> getMoves() {
        return moves;
    }

    public void setMoves(List<Movement> moves) {
        this.moves = moves;
    }

    public long getSolvingTime() {
        return solvingTime;
    }

    public void setSolvingTime(long solvingTime) {
        this.solvingTime = solvingTime;
    }

    public int getNodesExpanded() {
        return nodesExpanded;
    }

    public void setNodesExpanded(int nodesExpanded) {
        this.nodesExpanded = nodesExpanded;
    }

    public List<Movement> getMovements() {
        return moves;
    }
}
