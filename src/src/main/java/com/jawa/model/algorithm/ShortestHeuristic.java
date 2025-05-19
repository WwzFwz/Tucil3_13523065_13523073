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
        System.out.println("prim row " + r + " prim col " + c);
        if (primary.isHorizontal()) {
            int frontCol = c + primary.getLength() - 1;

            // Exit di kanan
            if (r == exit.getRow() && exit.getCol() > frontCol)
                return exit.getCol() - frontCol - 1;

            // Exit di kiri
            if (r == exit.getRow() && exit.getCol() < c)
                return c - exit.getCol() - 1;

        } else {
            int frontRow = r + primary.getLength() - 1;

            // Exit di bawah
            if (c == exit.getCol() && exit.getRow() > frontRow)
                return exit.getRow() - frontRow - 1;

            // Exit di atas
            if (c == exit.getCol() && exit.getRow() < r)
                return r - exit.getRow() - 1;
        }

        return Integer.MAX_VALUE; // Exit tidak sejajar
    }

}
