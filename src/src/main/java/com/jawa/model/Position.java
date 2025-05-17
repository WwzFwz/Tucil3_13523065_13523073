package com.jawa.model;

public class Position{
    public int row;
    public int col;

    public Position(int row,int col){
        this.row = row ;
        this.col = col;
    }
    public Position(Position other){
        this.row = other.row ;
        this.col = other.col;
    }
    public Position translate(int x, int y) {
        return new Position(row + x, col + y);
    }
    public void setRow(int row){
        this.row = row ;
    }
    public void setCol (int col){
        this.col = col;
    }
    public int getRow(){
        return this.row;
    }
    public int getCol(){
        return this.col;
    }
    // @Override
    // public boolean equals(Object obj) {
    //     if (this == obj) return true;
    //     if (!(obj instanceof Position)) return false;
    //     Position other = (Position) obj;
    //     return this.row == other.row && this.col == other.col;
    // }
    // @Override
    // public int hashCode() {
    //     return 31 * row + col;
    // }
}   