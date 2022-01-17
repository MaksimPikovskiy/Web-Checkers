package com.webcheckers.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.model.Piece.Color;
import com.webcheckers.model.Piece.Type;

/**
 * The unit test suite for the Space component.
 * @author Sean Clifford
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 */
@Tag("Model-tier")
class SpaceTest {

    /**
     * Test the creation of Space
     */
    @Test
    public void testSpace() {
        Piece piece = new Piece(Type.SINGLE, Color.RED);
        boolean isBlack = false;
        Space space = new Space(0, piece, isBlack);
        assertNotNull(space);
    }

    /**
     * Test if getting cell index is valid
     */
    @Test
    public void testGetCellIdx() {
        Piece piece = new Piece(Type.SINGLE, Color.RED);
        boolean isBlack = false;
        Space space = new Space(0, piece, isBlack);

        assertEquals(space.getCellIdx(), 0);
        assertNotEquals(space.getCellIdx(), 12);
    }

    /**
     * Test if space is a valid move
     */
    @Test
    public void testIsValid() {
        Piece piece = new Piece(Type.SINGLE, Color.RED);
        boolean isBlack = false;
        Space space = new Space(0, piece, isBlack);
        
        assertFalse(space.isValid());

        isBlack = true;
        piece = null;
        space = new Space(0,piece, isBlack);

        assertTrue(space.isValid());
    }

    /**
     * Test if getPiece is a valid piece
     */
    @Test
    public void testGetPiece() {
        Piece piece = new Piece(Type.SINGLE, Color.RED);
        boolean isBlack = true;
        Space space = new Space(0, piece, isBlack);

        assertNotNull(space.getPiece());

        piece = null;
        space = new Space(0, piece, isBlack);

        assertNull(space.getPiece());
    }

    /**
     * this method tests if the Space object can have a piece set there
     */
    @Test
    public void testSetPiece(){
        Piece piece = new Piece(Type.SINGLE, Color.RED);
        boolean isBlack = true;
        Space space = new Space(0, piece, isBlack);

        assertNotNull(space.getPiece());
        Piece piece2 = new Piece(Type.SINGLE, Color.RED);
        space.setPiece(piece2);
        assertNotEquals(space.getPiece(), piece);

        Piece piece3 = new Piece(Type.KING, Color.WHITE);
        space.setPiece(piece3);
        assertNotEquals(space.getPiece(), piece2);
    }


    /**
     * Test if toString is a valid
     */
    @Test
    public void testToString() {
        Piece piece = new Piece(Type.SINGLE, Color.RED);
        boolean isBlack = true;
        Space space = new Space(0, piece, isBlack);
        String testStr = "";

        assertNotEquals(space.toString(), testStr);

        String testStr1 = "Space{" + space.getCellIdx() + ",isBlack=" + isBlack + ",piece=" + space.getPiece() + "}";
        assertTrue(space.toString().equals(testStr1));
    }
}
