package com.jawa.model.algorithm;

import com.jawa.model.gameComponent.Board;
import com.jawa.model.gameComponent.Piece;
import com.jawa.model.gameComponent.Position;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.Set;

public class BlockingHeuristic implements Heuristic {
    @Override
    public int estimate(Board board) {
        Piece primary = board.getPrimaryPiece();
        Position exit = board.getExitPosition();
        if (primary == null || exit == null)
            return Integer.MAX_VALUE;

        char[][] grid = board.getBoardState();
        Set<Character> blockingIds = new HashSet<>();

        int pr = primary.getRow();
        int pc = primary.getCol();
        int len = primary.getLength();

        if (primary.isHorizontal()) {
            int row = pr;
            int startCol = (exit.getCol() > pc) ? pc + len : pc - 1;
            int endCol = exit.getCol();
            int step = (exit.getCol() > pc) ? 1 : -1;

            for (int c = startCol; c != endCol + step && c >= 0 && c < board.getCol(); c += step) {
                char cell = grid[row][c];
                if (cell != '.' && cell != 'P') {
                    blockingIds.add(cell);
                }
            }
        } else {
            int col = pc;
            int startRow = (exit.getRow() > pr) ? pr + len : pr - 1;
            int endRow = exit.getRow();
            int step = (exit.getRow() > pr) ? 1 : -1;

            for (int r = startRow; r != endRow + step && r >= 0 && r < board.getRow(); r += step) {
                char cell = grid[r][col];
                if (cell != '.' && cell != 'P') {
                    blockingIds.add(cell);
                }
            }
        }

        return blockingIds.size();
    }
}
