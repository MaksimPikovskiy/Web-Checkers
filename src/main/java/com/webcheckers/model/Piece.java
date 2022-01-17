package com.webcheckers.model;

/**
 * This class represents a single piece on a checkerboard. Can be a normal piece or a king, red or white, and can be
 * initialized with these values. Holds enums for Type and Color.
 *
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 */
public class Piece {

    /**
     * The type of a given checkers piece. Can be SINGLE or KING.
     */
    public enum Type {
        SINGLE, KING
    }

    /**
     * The color of a given checkers piece. Can be RED or WHITE.
     */
    public enum Color {
        RED, 
        WHITE
    }

    /**
     * This piece's type.
     */
    private Type myType;

    /**
     * This piece's color.
     */
    private final Color myColor;

    /**
     * Constructor for a checkers Piece.
     * @param myType the type that this piece will have. SINGLE or KING.
     * @param myColor the color that this piece will have. RED or WHITE.
     */
    public Piece(Type myType, Color myColor) {
        this.myType = myType;
        this.myColor = myColor;
    }

    /**
     * Get the type of this piece.
     * @return the type of this piece.
     */
    public Type getType() {
        return this.myType;
    }

    /**
     * Change this piece's Type to KING.
     */
    public void makeKing() {
        this.myType = Type.KING;
    }

    /**
     * Get the color of this piece.
     * @return the color of this piece.
     */
    public Color getColor () {
        return this.myColor;
    }

    /**
     * Get a string of this piece, showing type and color.
     * @return the string listing type and color.
     */
    @Override
    public String toString() {
        return "Piece{" +
                "myType=" + myType +
                ",myColor=" + myColor +
                '}';
    }
}
