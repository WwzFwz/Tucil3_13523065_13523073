package com.jawa.model;

import javafx.scene.paint.Color;

public class Vehicle {
    private String id;
    private int row;
    private int col;
    private int length;
    private boolean isHorizontal;
    private Color color;
    private boolean isPrimary;
    

    public Vehicle(String id, int row, int col, int length, boolean isHorizontal, boolean isPrimary) {
        this.id = id;
        this.row = row;
        this.col = col;
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
    

    public Vehicle(String id, int row, int col, int length, boolean isHorizontal, boolean isPrimary, Color color) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.length = length;
        this.isHorizontal = isHorizontal;
        this.isPrimary = isPrimary;
        this.color = color;
    }
    
    public Vehicle(Vehicle other) {
        this.id = other.id;
        this.row = other.row;
        this.col = other.col;
        this.length = other.length;
        this.isHorizontal = other.isHorizontal;
        this.color = other.color;
        this.isPrimary = other.isPrimary;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    
    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }
    
    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }
    
    public boolean isHorizontal() { return isHorizontal; }
    public void setHorizontal(boolean horizontal) { isHorizontal = horizontal; }
    
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
    
    public boolean isPrimary() { return isPrimary; }
    public void setPrimary(boolean primary) { isPrimary = primary; }

    public void move(String direction, int distance) {
        switch (direction) {
            case "R": 
                if (isHorizontal) {
                    col += distance;
                }
                break;
            case "L": 
                if (isHorizontal) {
                    col -= distance;
                }
                break;
            case "U": 
                if (!isHorizontal) {
                    row -= distance;
                }
                break;
            case "D": 
                if (!isHorizontal) {
                    row += distance;
                }
                break;
        }
    }
    
    @Override
    public String toString() {
        return id + " at (" + row + "," + col + "), length=" + length + 
               ", " + (isHorizontal ? "horizontal" : "vertical") +
               (isPrimary ? " (Primary)" : "");
    }
}