package com.jawa.model.algorithm;

import com.jawa.model.gameComponent.Board;

public interface Heuristic {
    int estimate(Board board);
}
