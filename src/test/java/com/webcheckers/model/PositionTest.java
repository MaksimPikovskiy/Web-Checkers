package com.webcheckers.model;

import com.webcheckers.ui.PostSignInRoute;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit test suite for the {@linkplain PostSignInRoute} component.
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
@Tag("Model-Tier")
class PositionTest {

    /**
     * Test the creation of {@linkplain Position}.
     */
    @Test
    public void testPosition(){
        Position CuT = new Position(1,1);
        assertNotNull(CuT);

        Position Pos = new Position(0,34);
        assertNotNull(Pos);
    }

    /**
     * Test retrieval of row that a {@linkplain Position} is in.
     */
    @Test
    public void testGetRow(){
        Position CuT = new Position(1,1);
        assertEquals(1,CuT.getRow());
        assertNotEquals(1.1,CuT.getRow());
    }

    /**
     * Test retrieval of cell (column) that a {@linkplain Position} is in.
     */
    @Test
    public void testGetCell(){
        Position CuT = new Position(1,4);
        assertEquals(4,CuT.getCell());
        assertNotEquals(4.0,CuT.getCell());
    }

    /**
     * Test check for {@linkplain Position} being on the board or not.
     */
    @Test
    public void testIsOnBoard() {
        Position CuT1 = new Position(0,0);
        assertTrue(CuT1.isOnBoard());

        Position CuT2 = new Position(7,0);
        assertTrue(CuT2.isOnBoard());

        Position CuT3 = new Position(8,3);
        assertFalse(CuT3.isOnBoard());

        Position CuT4 = new Position(4,8);
        assertFalse(CuT4.isOnBoard());

        Position CuT5 = new Position(-1,5);
        assertFalse(CuT5.isOnBoard());

        Position CuT6 = new Position(0,-1);
        assertFalse(CuT6.isOnBoard());

        Position CuT7 = new Position(8,-1);
        assertFalse(CuT7.isOnBoard());

        Position CuT8 = new Position(-1,8);
        assertFalse(CuT8.isOnBoard());
    }

    /**
     * Test toString print out.
     */
    @Test
    public void testToString(){
        Position CuT = new Position(4, 9);
        assertNotNull(CuT.toString());
        assertEquals("4,9",CuT.toString());
    }

    /**
     * Test check if {@linkplain Position} is equal to another {@linkplain Object},
     * supposedly {@linkplain Position}.
     */
    @Test
    public void testEquals() {
        Position position1 = new Position(1, 0);
        Position position2 = new Position(5,6);
        Position position3 = new Position(1, 0);
        Position nullPosition = null;
        String diffClass = "Max";


        boolean equals1 = position1.equals(position2);
        boolean equals2 = position1.equals(position3);
        boolean equals3 = position1.equals(nullPosition);
        boolean equals4 = position1.equals(diffClass);
        boolean equals5 = position1.equals(position1);

        assertFalse(equals1, "Position1 [1,0] and Position2 [5,6] should not be the same.");

        assertTrue(equals2, "Position1 [1,0]  and Position3 [1,0] should be the same.");

        assertFalse(equals3, "Position1 [1,0] and Position null should not be the same.");

        assertFalse(equals4, "Position1 [1,0] and String \"Max\" should not be the same.");

        assertTrue(equals5, "Position1 [1,0] should be the same as itself.");

    }
}