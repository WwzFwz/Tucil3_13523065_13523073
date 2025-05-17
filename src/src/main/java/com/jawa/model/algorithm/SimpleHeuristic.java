package com.jawa.model.algorithm;

import com.jawa.model.gameComponent.Board;

public class SimpleHeuristic implements Heuristic {
    @Override
    public int estimate(Board board) {
        var primary = board.getPieces().get("P");
        var exit = board.getExitPosition();
        if (primary == null || exit == null)
            return 0;

        if (!primary.isHorizontal())
            return 0;
        int tailCol = primary.getCol() + primary.getLength() - 1;
        if (primary.getRow() != exit.getRow())
            return 0;

        return Math.max(0, exit.getCol() - tailCol);
    }
}
