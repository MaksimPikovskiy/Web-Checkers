package com.webcheckers.model;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;


/**
 * The unit test suite for the Move component.
 * @author Sean Clifford
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 */
@Tag("Model-tier")
class MoveTest {
    /**
     * Test the creation of Move
     */
    @Test
    public void testMove() {
        Position start = new Position(0, 0);
        Position end = new Position(0, 0);
        Move move = new Move(start, end);
        assertNotNull(move);
    }

    /**
     * Test if start is valid
     */
    @Test
    public void testGetStart(){
        Position start = new Position(0, 0);
        Position end = new Position(0, 0);
        Move move = new Move(start, end);

        assertNotNull(move.getStart());
        assertEquals(move.getStart(), start);
    }

    /**
     * Test if end is valid
     */
    @Test
    public void testGetEnd(){
        Position start = new Position(0, 0);
        Position end = new Position(0, 0);
        Move move = new Move(start, end);

        assertNotNull(move.getEnd());
        assertSame(move.getEnd(), end);
    }

    /**
     * Test method which tests if the new position from
     * origin is a diagonal move
     */
    @Test
    public void testIsDiagonal(){
        Position position1 = new Position(0,0);
        Position position2 = new Position(1, 1);

        Position position3 = new Position(2,6);
        Move move1 = new Move(position1, position2);
        Move move2 = new Move(position1, position3);

        assertTrue(move1.isDiagonal());
        assertFalse(move2.isDiagonal());
    }

    /**
     * This method will test if the move is a forward (illegal) move
     */
    @Test
    public void testIsForwardMove(){
        Position startPos = new Position(0, 0);
        Position EndPos1 = new Position(1, 1);
        Position EndPos2 = new Position(1, 0);

        Move move1 = new Move(startPos,EndPos1);
        assertFalse(move1.isForwardMove());

        Move move2 = new Move(EndPos2,startPos);
        assertTrue(move2.isForwardMove());
    }

    /**
     * This method tests if the move is a valid checkers move
     */
    @Test
    public void testIsValid(){
        Position pos1 = new Position(0,0);
        Position pos2 = new Position(1,1);
        Position pos3 = new Position(0,1);
        Move move1 = new Move(pos1, pos2);

        assertTrue(move1.isValid());

        Move move2 = new Move(pos1, pos3);
        assertFalse(move2.isValid());
    }

    /**
     * This method tests if the move is a single space position or not
     */
    @Test
    public void testIsSingleSpace(){
        Position pos1 = new Position(0,0);
        Position pos2 = new Position(1,1);
        Position pos3 = new Position(2,2);
        Move move1 = new Move(pos1, pos2);
        Move move2 = new Move(pos1, pos3);

        assertTrue(move1.isSingleSpace());
        assertFalse(move2.isSingleSpace());

    }

    /**
     * This method tests if the move is a jump
     */
    @Test
    public void testIsJump(){
        Position pos1 = new Position(0,0);
        Position pos2 = new Position(1,1);

        Position pos3 = new Position(2,2);
        Move move1 = new Move(pos1, pos2);
        Move move2 = new Move(pos1, pos3);

        assertTrue(move2.isJump());
        assertFalse(move1.isJump());
    }

    /**
     * This method tests getting the mid point position from two positions
     */
    @Test
    public void testGetMidPoint(){
        Position pos1 = new Position(0,0);
        Position pos2 = new Position(1,1);
        Position pos3 = new Position(2,2);
        Move move1 = new Move(pos1, pos2);
        Move move2 = new Move(pos1, pos3);

        assertEquals(pos1, move1.getMidPoint());        //not a jump
        Position resultPos = move2.getMidPoint();
        assertEquals(1,resultPos.getRow());
        assertEquals(1,resultPos.getCell());
    }


    /**
     * Test if toString is a valid string
     */
    @Test
    public void testToString() {
        Position start = new Position(0, 0);
        Position end = new Position(1, 1);
        Move move = new Move(start, end);

        String moveString1 = "Move from " + move.getStart().toString() + " to " + move.getEnd().toString();
        String moveString2 = move.toString();

        assertEquals(moveString1, moveString2);
    }
}
