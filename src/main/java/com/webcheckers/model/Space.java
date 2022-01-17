package com.webcheckers.model;

/**
 * A class to represent a single space on a checkerboard. Has an index, has a color (isBlack), and may have a piece
 * on it.
 *
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 */
public class Space {
    /**
     * The index of this space on its row.
     */
    private final int cellIdx;

    /**
     * Whether this space is black (or white).
     */
    private final boolean isBlack;

    /**
     * The piece that resides on this space. (may be null)
     */
    private Piece piece;

    /**
     * The constructor for a Space on a checkerboard.
     * @param cellIdx the index in the row that this Space will have.
     * @param piece the piece that this Space will have on it (at initialization).
     * @param isBlack whether this Space will be black or white.
     */
    public Space(int cellIdx, Piece piece, boolean isBlack) {
        this.cellIdx = cellIdx;
        this.piece = piece;
        this.isBlack = isBlack;
    }

    /**
     * Get the index of this Space on its row.
     * @return the index of this space on its row
     */
    public int getCellIdx() {
        return cellIdx;
    }

    /**
     * Get whether this Space is a valid destination of a move. Must not have a piece on it, and must be black.
     * @return whether this Space is a valid destination of a move.
     */
    public boolean isValid() {
        return (isBlack && (piece == null));
    }

    /**
     * Get the Piece that resides on this Space.
     * @return the Piece that resides on this Space.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Set the Piece that resides on this Space.
     * @param piece the new Piece that will reside in this Space.
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Convert this Space to a string, showing index, isBlack, and the piece that resides on it.
     * @return the string conversion of this Space.
     */
    @Override
    public String toString() {
        return "Space{" + cellIdx +
                ",isBlack=" + isBlack +
                ",piece=" + piece +
                '}';
    }
}
