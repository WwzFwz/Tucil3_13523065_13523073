package com.jawa.model;

public class Movement {
    private String pieceId;
    private String direction; // "R", "L", "U", "D"
    private int distance; // pindah brp langkah

    public Movement(String pieceId, String direction, int distance){
        this.pieceId = pieceId;
        this.direction = direction ;
        this. distance = distance;
    }
    public String getDirection(){
        return direction;
    }
    public int getDistance(){
        return distance;
    }
    public String getPieceId(){
        return pieceId;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    public void setPieceId(String pieceId){
        this.pieceId = pieceId;
    }
    public void setDistance(int distance){
        this.distance = distance;
    }
}