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
            // harus berada pada baris yang sama
            if (r != exit.getRow())
                return Integer.MAX_VALUE;
            return Math.max(0, exit.getCol() - frontCol - 1); // jarak ke kanan
        } else {
            int frontRow = r + primary.getLength() - 1;
            if (c != exit.getCol())
                return Integer.MAX_VALUE;
            return Math.max(0, exit.getRow() - frontRow - 1); // jarak ke bawah
        }
    }
}
