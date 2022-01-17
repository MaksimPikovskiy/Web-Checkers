package com.webcheckers.model;

import java.util.Iterator;

/**
 * A class that represents a single row on a checkerboard. Has an index (row number) and a list of Spaces.
 *
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 */
public class Row implements Iterable<Space> {

    /**
     * The index of this Row on the checkerboard (0 means top row)
     */
    private final int index;

    /**
     * A list of spaces that make up this row.
     */
    private Space[] spaces;

    /**
     * The constructor for a Row object.
     * @param spaces the Spaces that will be contained in this Row.
     * @param index the row number of this Row.
     */
    public Row(Space[] spaces, int index) {
        this.spaces = spaces;
        this.index = index;
    }

    /**
     * Create and return an Iterator of this Row.
     * @return the Iterator of this Row.
     */
    @Override
    public Iterator<Space> iterator() {
        return new RowIterator(this);
    }

    /**
     * Get the index (row number) of this Row.
     * @return the index of this Row.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Get a Space on this Row from a given index (column)
     * @param index the index (column) of the Space
     * @return the Space at [index] on this Row.
     */
    public Space getSpace(int index) {
        return spaces[index];
    }

    /**
     * Get the length (number of elements) of this Row.
     * @return the length of this Row.
     */
    public int getLength() {
        return spaces.length;
    }

    /**
     * Return a flipped copy of this Row. Do not change the index.
     * @return the flipped copy of this Row.
     */
    public Row flipRow() {
        Space[] flipped = new Space[getLength()];
        int revCounter = getLength();
        for (int i = 0; i < getLength(); i++) {
            flipped[revCounter-1] = spaces[i];
            revCounter--;
        }
        return new Row(flipped, index);
    }
}

/**
 * An Iterator implementation of a Row object. Holds a Row, and an index variable that is used for iteration.
 *
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 */
class RowIterator implements Iterator<Space> {

    /**
     * The Row object that its Iterator iterates over.
     */
    private Row row;

    /**
     * The internal index used by next() when iterating.
     */
    private int index = 0;

    /**
     * The constructor for this RowIterator. Assign this RowIterator's internal Row.
     * @param row the Row that this RowIterator represents.
     */
    public RowIterator(Row row) {
        this.row = row;
    }

    /**
     * Return whether this row has more elements to iterate over.
     * @return whether this row has more elements to iterate over.
     */
    @Override
    public boolean hasNext() {
        return (index < row.getLength());
    }

    /**
     * Iterate over this RowIterator's Row and return the next element. Increment the index for further iteration.
     * @return the next element.
     */
    @Override
    public Space next() {
        Space next = row.getSpace(index);
        index++;
        return next;
    }
}