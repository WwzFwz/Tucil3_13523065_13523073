package com.jawa.model.algorithm;

import com.jawa.model.gameComponent.Board;
import com.jawa.model.gameComponent.Piece;
import com.jawa.model.gameComponent.Position;

public class ShortestHeuristic implements Heuristic {

    @Override
    public int estimate(Board board) {
        Piece primary = board.getPieces().get("P");
        Position exit = board.getExitPosition();
        if (primary == null || exit == null)
            return Integer.MAX_VALUE;

        int r = primary.getRow();
        int c = primary.getCol();
        if (primary.isHorizontal()) {
            int frontCol = c + primary.getLength() - 1;
            if (r == exit.getRow() && exit.getCol() > frontCol)
                return exit.getCol() - frontCol - 1;

            if (r == exit.getRow() && exit.getCol() < c)
                return c - exit.getCol() - 1;

        } else {
            int frontRow = r + primary.getLength() - 1;
            if (c == exit.getCol() && exit.getRow() > frontRow)
                return exit.getRow() - frontRow - 1;

        
            if (c == exit.getCol() && exit.getRow() < r)
                return r - exit.getRow() - 1;
        }

        return Integer.MAX_VALUE; 
    }

}
