package com.webcheckers.model;

/**
 * Move holds {@linkplain Position start and end positions} of the pieces.
 * It also validates the moves.
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class Move {

    // Fields
    /**
     * Position object that is the start position of a given move
     */
    private Position start;
    /**
     * Position object that is the end position of a given move
     */
    private Position end;

    /**
     * Constructs a Move class with {@linkplain Position start and end positions}
     * a piece.
     *
     * @param start starting {@link Position position} of a piece
     * @param end ending {@link Position position} of a piece
     */
    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Retrieves starting {@linkplain Position position} of a piece.
     *
     * @return starting {@link Position position} of a piece
     */
    public Position getStart() {
        return this.start;
    }

    /**
     * Retrieves ending {@linkplain Position position} of a piece.
     *
     * @return ending {@link Position position} of a piece
     */
    public Position getEnd() {
        return this.end;
    }

    /**
     * Checks if the move made is diagonal.
     * (deltaY == deltaX) determines diagonally.
     *
     * @return true if the move made is diagonal
     *         false if the move made is not diagonal
     */
    public boolean isDiagonal() {
        int deltaY = Math.abs(start.getRow() - end.getRow());
        int deltaX = Math.abs(start.getCell() - end.getCell());

        return deltaY == deltaX;
    }

    /**
     * Checks if the move made is only forward.
     * (deltaY > 0) determines forward move.
     * Single Piece property, not King Piece property.
     *
     * @return true if the move made is forward
     *         false if the move made is not forward
     */
    public boolean isForwardMove() {
        int deltaY = start.getRow() - end.getRow();
        return deltaY > 0;
    }

    /**
     * Determines if the move made is on the board (valid).
     *
     * @return true if the move made is valid
     *         false if the move made is not valid
     */
    public boolean isValid() {
        boolean diagonalMove = isDiagonal();
        boolean properMove = isJump() || isSingleSpace();
        boolean onBoard = start.isOnBoard() && end.isOnBoard();
        return diagonalMove && properMove && onBoard;
    }

    /**
     * Determines if the move made is only a single space.
     *
     * @return true if the move made is a single space
     *         false if the move made is not a single space
     */
    public boolean isSingleSpace() {
        int deltaY = Math.abs(start.getRow() - end.getRow());
        int deltaX = Math.abs(start.getCell() - end.getCell());

        return (deltaY == 1) && (deltaX == 1);
    }

    /**
     * Determines if the move made is only a jump.
     *
     * @return true if the move made is a jump
     *         false if the move made is not a jump
     */
    public boolean isJump() {
        int deltaY = Math.abs(start.getRow() - end.getRow());
        int deltaX = Math.abs(start.getCell() - end.getCell());

        return (deltaY == 2) && (deltaX == 2);
    }

    /**
     * Determines the midpoint of the move line.
     *
     * @return {@link Position position} of the midpoint
     */
    public Position getMidPoint() {
        if(isJump()) {
            int midPointRow = (start.getRow() + end.getRow()) / 2;
            int midPointCell = (start.getCell() + end.getCell()) / 2;

            return new Position(midPointRow, midPointCell);
        }
        return start;
    }

    /**
     * {@inheritDoc}
     *
     * @return a String in format of either
     *              "Move from [start row,start cell] to [end row,end cell]"
     *              "Jump from [start row,start cell] to [end row,end cell]"
     */
    @Override
    public String toString(){
        return String.format("%s from %s to %s", isJump() ? "Jump" : "Move", start, end);
    }
}
