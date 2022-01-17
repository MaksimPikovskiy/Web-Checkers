package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

/**
 * This class will test the {@linkplain Row} class and all of its methods.
 *
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
@Tag("Model-Tier")
public class RowTest {
    public RowTest(){

    }

    /**
     * Test the creation of the {@linkplain Row} class
     */
    @Test
    public void testRowInit(){
        Space[] spaces = buildRowArray(8);
        Row row = new Row(spaces, 1);
        assertNotNull(row, "Row initialization must not be null");
    }
    
    /**
     * Test that Row.iterator() does not return null.
     */
    @Test
    public void testRowIterator() {
        Space[] spaces = buildRowArray(8);
        Row row = new Row(spaces, 1);
        Iterator<Space> iterator = row.iterator();
        assertNotNull(iterator);
        int i = 0;
        while(iterator.hasNext()){
            assertEquals(spaces[i], iterator.next());
            i++;
        }
        assertEquals(8, i);
    }

    /**
     * Test that Row.getIndex() returns the row number passed in the constructor
     */
    @Test
    public void testRowIndex() {
        Space[] spaces = buildRowArray(8);
        Row row = new Row(spaces, 1);
        assertEquals(1, row.getIndex(), "The index does not match the expected ");
        row = new Row(spaces, 2);
        assertEquals(2, row.getIndex(), "The index does not match the expected ");
        row = new Row(spaces, 3);
        assertEquals(3, row.getIndex(), "The index does not match the expected ");
        row = new Row(spaces, 4);
        assertEquals(4, row.getIndex(), "The index does not match the expected ");
    }

    /**
     * Test that Row.getLength() returns the number of spaces passed into it
     */
    @Test
    public void testRowLength() {
        Space[] spaces = buildRowArray(8);
        Row row = new Row(spaces, 1);
        assertEquals(8, row.getLength(), "The row length should equal 8");

        spaces = buildRowArray(4);
        row = new Row(spaces, 1);
        assertEquals(4, row.getLength(), "The row length should equal 4");
    }

    /**
     * Test that Row.getSpace() returns the same space as the space in the array
     */
    @Test
    public void testRowGetSpace() {
        Space[] spaces = buildRowArray(8);
        Row row = new Row(spaces, 1);
        assertSame(spaces[0], row.getSpace(0), "getSpace(0) should be the same as the object in spaces[0]");

        assertNotSame(spaces[0], row.getSpace(4), "getSpace(4) should not be the same as the object in spaces[0]");

        assertSame(spaces[7], row.getSpace(7), "getSpace(7) should be the same as the object in spaces[7]");
    }

    /**
     * Test the Row.flipRow() correctly flips a the row and flips it back to the original
     */
    @Test
    public void testRowFlip() {
        Space[] spaces = buildRowArray(7);
        Row row = new Row(spaces, 1);
        Row flipRow = row.flipRow();
        Row reFlipRow = flipRow.flipRow();

        assertSame(row.getSpace(0), flipRow.getSpace(6), "The first space is the same as the last in the flipped");
        assertSame(row.getSpace(3), flipRow.getSpace(3), "The middle space should be the same for the flipped and non flipped");

        assertSame(row.getSpace(0), reFlipRow.getSpace(0), "The first space should be the same as the original after another flip");
        assertSame(row.getSpace(6), reFlipRow.getSpace(6), "The last space should be the same as the original after another flip");
    }

    /**
     * helper function for building an array of spaces
     * @param number The size of the array
     * @return
     */
    private Space[] buildRowArray(int number){
        Space[] spaces = new Space[number];
        Piece redPiece = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
        for (int i = 0; i < number; i++) {
            spaces[i] = new Space(i, redPiece, false);
        }
        return spaces;
    }
}
