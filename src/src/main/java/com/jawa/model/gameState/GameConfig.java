// package com.jawa.model.gameState;

// import java.util.ArrayList;
// import java.util.List;

// import com.jawa.model.gameComponent.Board;
// import com.jawa.model.gameComponent.Piece;

// public class GameConfig {
//     private int rows;
//     private int cols;
//     private int countPiece;
//     private List<Piece> pieces;

//     public GameConfig(){
//         this.rows = 0;
//         this.cols = 0;
//         this.countPiece =0 ;
//         this.pieces = new ArrayList<>();
//         this.board = new Board(0,0);
//     }
//     public GameConfig(int rows, int cols, int countPiece) {
//         this.rows = rows;
//         this.cols = cols;
//         this.countPiece = countPiece;
//         this.pieces = new ArrayList<>();
//         this.board = new Board(rows,cols);
//     }
    
//     public int getRows() {
//         return rows;
//     }
    
//     public void setRows(int rows) {
//         this.rows = rows;
//     }
    
//     public int getCols() {
//         return cols;
//     }
    
//     public void setCols(int cols) {
//         this.cols = cols;
//     }
    
//     public int getCountPiece() {
//         return countPiece;
//     }
    
    
    
//     // public String getAlgorithm() {
//     //     return algorithm;
//     // }
    
//     // public void setAlgorithm(String algorithm) {
//     //     this.algorithm = algorithm;
//     // }
    
//     // public String getHeuristic() {
//     //     return heuristic;
//     // }
    
//     // public void setHeuristic(String heuristic) {
//     //     this.heuristic = heuristic;
//     // }
    
//     public List<Piece> getPieces() {
//         return pieces;
//     }
    
//     public void setPieces(List<Piece> pieces) {
//         this.pieces = pieces;
//         this.countPiece = pieces.size();
//     }
    

//     public void addPiece(Piece piece) {
//         this.pieces.add(piece);
//         this.countPiece = this.pieces.size();
//     }

//     public boolean removePiece(String id) {
//         for (int i = 0; i < pieces.size(); i++) {
//             if (pieces.get(i).getId().equals(id)) {
//                 pieces.remove(i);
//                 this.countPiece = this.pieces.size();
//                 return true;
//             }
//         }
//         return false;
//     }
    

//     public Piece getPieceById(String id) {
//         for (Piece piece : pieces) {
//             if (piece.getId().equals(id)) {
//                 return piece;
//             }
//         }
//         return null;
//     }
    

//     public Piece getPrimaryPiece() {
//         for (Piece piece : pieces) {
//             if (piece.isPrimary()) {
//                 return piece;
//             }
//         }
//         return null;
//     }
// }
