package com.jawa.model.gameState;

import java.util.List;

import com.jawa.model.gameComponent.Piece;
import com.jawa.model.gameComponent.Position;

public class GameConfig {
    // private ArrayList<ArrayList<Piece>> board; //maybe ga perlu ?
    private int rows;
    private int cols;
    private int countPiece;
    private Position finishPosition;
    private String algorithm;
    private String heuristic;
    private List<Piece> pieces;

}
