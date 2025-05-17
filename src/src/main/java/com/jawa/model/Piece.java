package com.jawa.model;
import javafx.scene.paint.Color;

public class Piece {
    private String id;
    private final Position position;
    private int length;
    private boolean isHorizontal;
    private Color color;
    private boolean isPrimary;
    

    public Piece(String id, int row, int col, int length, boolean isHorizontal, boolean isPrimary) {
        this.id = id;
        this.position = new Position(row,col);
        this.length = length;
        this.isHorizontal = isHorizontal;
        this.isPrimary = isPrimary;
        
        if (isPrimary) {
            this.color = Color.PURPLE;
        } else {
            int hue = 0;
            if (id.length() > 0 && id.charAt(0) >= 'A' && id.charAt(0) <= 'Z') {
                // Map A-Z ke warna berbeda pada color wheel
                // A=0°, B=360°/26, C=2*360°/26, ..., Z=25*360°/26
                int charValue = id.charAt(0) - 'A'; // 0 untuk A, 25 untuk Z
                hue = (charValue * 360 / 26) % 360; // Map ke 0-359 derajat
            }
            this.color = Color.hsb(hue, 0.7, 0.8); // Saturasi dan brightness tetap
        }
    }
    

    public Piece(String id, int row, int col, int length, boolean isHorizontal, boolean isPrimary, Color color) {
        this.id = id;
        this.position = new Position(row,col);
        this.length = length;
        this.isHorizontal = isHorizontal;
        this.isPrimary = isPrimary;
        this.color = color;
    }
    
    public Piece(Piece other) {
        this.id = other.id;
        this.position = new Position(other.position);
        this.length = other.length;
        this.isHorizontal = other.isHorizontal;
        this.color = other.color;
        this.isPrimary = other.isPrimary;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public int getRow() { return position.getRow() ; }
    public void setRow(int row) { position.setRow(row); }
    
    public int getCol() { return position.getCol(); }
    public void setCol(int col) {  position.setCol(col);}
    
    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }
    
    public boolean isHorizontal() { return isHorizontal; }
    public void setHorizontal(boolean horizontal) { isHorizontal = horizontal; }
    
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
    
    public boolean isPrimary() { return isPrimary; }
    public void setPrimary(boolean primary) { isPrimary = primary; }

    public Movement move(String direction, int distance) {
        switch (direction) {
            case "R": 
                if (isHorizontal) {
                     setCol(getCol()+distance);
                }
                break;
            case "L": 
                if (isHorizontal) {
                    setCol((getCol()-distance));
                }
                break;
            case "U": 
                if (!isHorizontal) {
                    setRow(getRow()-distance);
                }
                break;
            case "D": 
                if (!isHorizontal) {
                    setRow(getRow()+ distance);
                }
                break;
        }
        return new Movement(getId(),direction,distance);
    }
    
    @Override
    public String toString() {
        return id + " at (" + getRow() + "," + getCol() + "), length=" + length + 
               ", " + (isHorizontal ? "horizontal" : "vertical") +
               (isPrimary ? " (Primary)" : "");
    }
}