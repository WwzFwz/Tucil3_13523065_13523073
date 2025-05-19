package com.jawa.model.algorithm;

import com.jawa.model.gameComponent.Board;
import com.jawa.model.gameState.Result;

public interface Solver {
    public Result solve(Board initial);

}
