package com.jawa;

import com.jawa.model.algorithm.GreatSolver;
import com.jawa.model.algorithm.Heuristic;
import com.jawa.model.algorithm.ZeroHeuristic;
import com.jawa.model.gameComponent.Board;
import com.jawa.model.gameComponent.Movement;
import com.jawa.model.gameComponent.Piece;
import com.jawa.model.gameState.IO;
import com.jawa.model.gameState.Result;

import java.io.IOException;
import java.util.List;

public class cliTest {
    public static void main(String[] args) {
        String filename = "test/input/config.txt"; // sesuaikan path

        try {
            Board initialBoard = IO.loadFromFile(filename);

            System.out.println("Board size: " + initialBoard.getRow() + " x " + initialBoard.getCol());
            System.out.println("Pieces found:");
            initialBoard.getPieces().values().forEach(System.out::println);

            System.out.println("Exit position: (" +
                    initialBoard.getExitPosition().getRow() + ", " +
                    initialBoard.getExitPosition().getCol() + ")");
            System.out.println("=== Initial Board ===");
            initialBoard.printBoard();

            if (initialBoard.isSolved()) {
                System.out.println("Board already solved!");
                return;
            }

            /* ---------- jalankan solver ---------- */
            Heuristic heuristic = new ZeroHeuristic(); // UCS
            GreatSolver solver = new GreatSolver(heuristic);

            Result result = solver.solve(initialBoard);

            if (result == null) {
                System.out.println("No solution found.");
                return;
            }

            System.out.println("\nSolved in " + result.getSolvingTime() + " ms");
            System.out.println("Nodes expanded : " + result.getNodesExpanded());
            System.out.println("Total moves    : " + result.getMoves().size());

            /* ---------- replay & cetak tiap langkah ---------- */
            System.out.println("\n=== Replay Solution ===");
            Board replay = new Board(initialBoard); // deep copy via copy‑ctor
            int step = 1;
            for (Movement mv : result.getMoves()) {
                Piece p = replay.getPieces().get(mv.getPieceId());
                replay.movePiece(p, mv.getDirection(), mv.getDistance());

                System.out.println("Step " + step++ + " : " +
                        mv.getPieceId() + " " + mv.getDirection() + " " + mv.getDistance());
                replay.printBoard();
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
