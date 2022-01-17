package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.webcheckers.model.Piece.Type;
import com.webcheckers.model.Piece.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit test suite for the {@linkplain Piece} component.
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
@Tag("Model-Tier")
class PieceTest {

    /**
     * Multiple component-under-tests (CuT).
     */
    Piece CuT1;
    Piece CuT2;
    Piece CuT3;
    Piece CuT4;

    /**
     * Setup new CuT objects for each test with specific parameters.
     */
    @BeforeEach
    public void setup() {
        CuT1 = new Piece(Type.SINGLE, Color.RED);

        CuT2 = new Piece(Type.KING, Color.RED);

        CuT3 = new Piece(Type.SINGLE, Color.WHITE);

        CuT4 = new Piece(Type.KING, Color.WHITE);
    }

    /**
     * Test the creation of {@linkplain Piece}.
     */
    @Test
    public void testPiece() {
        assertNotNull(CuT1, "Piece should not be null");


        assertNotNull(CuT2, "Piece should not be null");


        assertNotNull(CuT3, "Piece should not be null");


        assertNotNull(CuT4, "Piece should not be null");
    }

    /**
     * Test the retrieval of type of {@linkplain Piece}.
     */
    @Test
    public void testGetType() {
        Type type1 = CuT1.getType();
        assertEquals(Type.SINGLE, type1, "Piece's type should be SINGLE.");

        Type type2 = CuT2.getType();
        assertEquals(Type.KING, type2, "Piece's type should be KING.");

        Type type3 = CuT3.getType();
        assertEquals(Type.SINGLE, type3, "Piece's type should be SINGLE.");

        Type type4 = CuT4.getType();
        assertEquals(Type.KING, type4, "Piece's type should be KING.");
    }

    /**
     * Test the ability to change {@linkplain Piece Piece's} type to Type.KING.
     */
    @Test
    public void testMakeKing() {
        CuT1.makeKing();
        Type type1 = CuT1.getType();
        assertEquals(Type.KING, type1, "Piece's type should be KING after being made one.");

        CuT2.makeKing();
        Type type2 = CuT2.getType();
        assertNotEquals(Type.SINGLE, type2, "Piece's type of KING should have not been reverted to SINGLE.");

        CuT3.makeKing();
        Type type3 = CuT3.getType();
        assertEquals(Type.KING, type3, "Piece's type should be KING after being made one.");

        CuT4.makeKing();
        Type type4 = CuT4.getType();
        assertNotEquals(Type.SINGLE, type4, "Piece's type of KING should have not been reverted to SINGLE.");
    }

    /**
     * Test the retrieval of color of {@linkplain Piece}.
     */
    @Test
    public void testGetColor() {
        Color color1 = CuT1.getColor();
        assertEquals(Color.RED, color1, "Piece's color should be RED.");

        Color color2 = CuT2.getColor();
        assertEquals(Color.RED, color2, "Piece's color should be RED.");

        Color color3 = CuT3.getColor();
        assertEquals(Color.WHITE, color3, "Piece's color should be WHITE.");

        Color color4 = CuT4.getColor();
        assertEquals(Color.WHITE, color4, "Piece's color should be WHITE.");
    }

    /**
     * Test toString of {@linkplain Piece}.
     */
    @Test
    public void testToString() {
        String toString1 = "Piece{myType=SINGLE,myColor=RED}";
        assertEquals(toString1, CuT1.toString(), "Piece's toString should be Piece{myType=SINGLE,myColor=RED}.");

        String toString2 = "Piece{myType=KING,myColor=RED}";
        assertEquals(toString2, CuT2.toString(), "Piece's toString should be Piece{myType=KING,myColor=RED}.");

        String toString3 = "Piece{myType=SINGLE,myColor=WHITE}";
        assertEquals(toString3, CuT3.toString(), "Piece's toString should be Piece{myType=SINGLE,myColor=WHITE}.");

        String toString4 = "Piece{myType=KING,myColor=WHITE}";
        assertEquals(toString4, CuT4.toString(), "Piece's toString should be Piece{myType=KING,myColor=WHITE}.");
    }
}