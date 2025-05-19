package com.jawa.model.gameComponent;

public class Movement {
    private String pieceId;
    private String direction; // "R", "L", "U", "D"
    private int distance; // pindah brp langkah

    public Movement(String pieceId, String direction, int distance) {
        this.pieceId = pieceId;
        this.direction = direction;
        this.distance = distance;
    }

    public String getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }

    public String getPieceId() {
        return pieceId;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setPieceId(String pieceId) {
        this.pieceId = pieceId;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
    @Override
    public String toString() {
        String directionText = "";
        switch (direction) {
            case "R": directionText = "Right"; break;
            case "L": directionText = "Left"; break;
            case "U": directionText = "Up"; break;
            case "D": directionText = "Down"; break;
            default: directionText = direction;
        }
        
        return "Piece " + pieceId + " moves " + directionText + " " + distance + 
            (distance > 1 ? " steps" : " step");
    }
}