package com.webcheckers.model;

import java.util.Iterator;

/**
 * A class that represents an entire Checkerboard. Holds a list of Row objects.
 *
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 */
public class BoardView implements Iterable<Row> {

    /**
     * The list of Rows that this BoardView contains.
     */
    private Row[] rows;

    /**
     * The constructor for a BoardView object. Initialize the list of Rows as blank.
     */
    public BoardView() {
        rows = new Row[8];
    }

    /**
     * Add a new Row to this BoardView.
     * @param spaces the Spaces that will be inside the new Row.
     * @param rowNumber the index of this Row inside the BoardView.
     */
    public void setRow(Space[] spaces, int rowNumber) {
        Row newRow = new Row(spaces, rowNumber);
        rows[rowNumber] = newRow;
    }

    /**
     * Add a new Row to this BoardView.
     * @param row the Row to be added.
     * @param rowNumber the index of this Row inside the BoardView.
     */
    public void setRow(Row row, int rowNumber) {
        rows[rowNumber] = row;
    }

    /**
     * Create and return an Iterator object of this BoardView.
     * @return the Iterator object of this BoardView.
     */
    @Override
    public Iterator<Row> iterator() {
        return new BoardViewIterator(this);
    }

    /**
     * Get a Row on this BoardView at a given index.
     * @param index the row number of the desired Row.
     * @return the Row at [index].
     */
    public Row getRow(int index) {
        return rows[index];
    }

    /**
     * Get the length of the list of Rows.
     * @return the length of the list of Rows.
     */
    public int getLength() {
        return rows.length;
    }

    /**
     * Return a copy of this BoardView, with its rows and columns flipped. Do not change the row numbers.
     * @return the flipped copy of this BoardView.
     */
    public BoardView flipBoard() {
        BoardView flippedBoard = new BoardView();
        int revCounter = getLength();
        for (int i = 0; i < getLength(); i++) {
            Row flippedRow = getRow(i).flipRow();
            flippedBoard.setRow(flippedRow,revCounter -1);
            revCounter--;
        }
        return flippedBoard;
    }
}


/**
 * The Iterator implementation of a BoardView object. Holds a BoardView, and an internal index used for iteration.
 *
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 */
class BoardViewIterator implements Iterator<Row> {

    /**
     * The BoardView that this Iterator iterates over.
     */
    private BoardView boardView;

    /**
     * The internal index used for iteration over the BoardView.
     */
    private int index = 0;

    /**
     * The constructor of a BoardViewIterator object. Assign a BoardView to this Iterator.
     * @param boardView the BoardView that this Iterator will iterate over.
     */
    public BoardViewIterator(BoardView boardView) {
        this.boardView = boardView;
    }

    /**
     * Return whether iteration can continue over the BoardView using the internal index.
     * @return whether this.index is less than the length of BoardView
     */
    @Override
    public boolean hasNext() {
        return (index < boardView.getLength());
    }

    /**
     * Iterate over the BoardView and return the next element using the internal index. Increment the index.
     * @return the next element in the BoardView.
     */
    @Override
    public Row next() {
        Row next = boardView.getRow(index);
        index++;
        return next;
    }
}