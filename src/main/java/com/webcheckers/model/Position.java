package com.webcheckers.model;

/**
 * Position class holds the data values for the row and cell(column).
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class Position {

    // Fields
    /**
     * int object that is the row of the current position object
     */
    private int row;
    /**
     * int object that is the cell of the current position object
     */
    private int cell;

    /**
     * Creates a Position with specified row and cell values.
     *
     * @param row selected row on the board
     * @param cell selected cell on the board
     */
    public Position(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }

    /**
     * Retrieves row value.
     *
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * Retrieves cell value.
     *
     * @return the cell
     */
    public int getCell() {
        return cell;
    }

    /**
     * Validates if the Position is within the board.
     *
     * @return true if Position is on the board
     *         false if Position is off the board
     */
    public boolean isOnBoard() {
        return (row >= 0 && row < 8) &&
                (cell >= 0 && cell < 8);
    }

    /**
     * {@inheritDoc}
     *
     * @return toString for position in format "[row],[cell]"
     */
    @Override
    public String toString(){
        return row + "," + cell;
    }

    /**
     * {@inheritDoc}
     *
     * @param that The {@link Object} that the current object ({@link Position}) is going to be compared to
     * @return true if the Object is the same as the current object ({@link Position}), false otherwise
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (that instanceof Position) {
            Position thatPosition = (Position) that;
            return thatPosition.row == this.row && thatPosition.cell == this.cell;
        }
        return false;
    }
}
