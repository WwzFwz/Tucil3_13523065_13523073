package com.jawa.model.heuristic;

import com.jawa.model.gameComponent.Board;

public abstract class Heuristic {
    public Heuristic() {
    }

    public abstract int estimate(Board board);

}
