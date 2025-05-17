package com.jawa.model.algorithm;

import com.jawa.model.gameComponent.Board;

public class ZeroHeuristic implements Heuristic {
    @Override
    public int estimate(Board board) {
        return 0; // for UCS
    }
}
