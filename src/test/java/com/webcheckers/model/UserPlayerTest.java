package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * The unit test suite for the {@linkplain UserPlayer} component.
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
@Tag("Model-Tier")
class UserPlayerTest {

    /**
     * Test the creation of {@linkplain UserPlayer}.
     */
    @Test
    public void testPlayerUser() {
        UserPlayer player1 = new UserPlayer("Max");
        assertNotNull(player1, "Player \"Max\" should not be null.");

        Player player2 = new UserPlayer("Jack");
        assertNotNull(player2, "Player \"Jack\" should not be null.");
    }

    /**
     * Test the setting of {@linkplain BoardView} for {@linkplain UserPlayer}.
     */
    @Test
    public void testSetBoardView() {
        BoardView board = mock(BoardView.class);

        UserPlayer player1 = new UserPlayer("Max");
        player1.setBoardView(board);
        assertNotNull(player1.getBoardView(), "Player \"Max\" board view should not be null.");

        Player player2 = new UserPlayer("Jack");
        player2.setBoardView(board);
        assertNotNull(player2.getBoardView(), "Player \"Jack\" board view should not be null.");
    }

    /**
     * Test the retrieval of {@linkplain BoardView} for {@linkplain UserPlayer}.
     */
    @Test
    public void testGetBoardView() {
        BoardView board = mock(BoardView.class);

        UserPlayer player1 = new UserPlayer("Max");
        player1.setBoardView(board);
        assertNotNull(player1.getBoardView(), "Player \"Max\" board view should not be null.");

        Player player2 = new UserPlayer("Jack");
        assertNull(player2.getBoardView(), "Player \"Jack\" board view should be null.");
        player2.setBoardView(board);
        assertNotNull(player2.getBoardView(), "Player \"Jack\" board view should not be null.");
    }

    /**
     * Test the retrieval of the state of the game in which the {@linkplain UserPlayer} is in.
     */
    @Test
    public void testGetGameOver() {
        UserPlayer player1 = new UserPlayer("Max");
        assertFalse(player1.getGameOver(), "Player \"Max\" game should not be over.");

        Player player2 = new UserPlayer("Jack");
        assertFalse(player2.getGameOver(), "Player \"Jack\" should not be over.");
        player2.setGameOver(true);
        assertTrue(player2.getGameOver(), "Player \"Jack\" should be over.");
    }

    /**
     * Test the setting of the state of the game in which the {@linkplain UserPlayer} is in.
     */
    @Test
    public void testSetGameOver() {
        UserPlayer player1 = new UserPlayer("Max");
        player1.setGameOver(true);
        assertTrue(player1.getGameOver(), "Player \"Max\" game should be over.");

        Player player2 = new UserPlayer("Jack");
        player2.setGameOver(true);
        assertTrue(player2.getGameOver(), "Player \"Jack\" game should be over.");
    }

    /**
     * Test check of whether the {@linkplain UserPlayer} is in the game.
     */
    @Test
    public void testIsInGame() {
        BoardView board = mock(BoardView.class);

        UserPlayer player1 = new UserPlayer("Max");
        player1.setBoardView(board);
        assertTrue(player1.isInGame(), "Player \"Max\" should be in game.");

        UserPlayer player2 = new UserPlayer("Jack");
        assertFalse(player2.isInGame(), "Player \"Jack\" should be not in game.");
    }

    /**
     * Test the retrieval of the name of {@linkplain UserPlayer}.
     */
    @Test
    public void testGetName() {
        String player1Name = "Max";
        UserPlayer player1 = new UserPlayer(player1Name);
        assertEquals(player1Name, player1.getName(), "Player name should be \"Max\".");

        String player2Name = "Jack";
        Player player2 = new UserPlayer(player2Name);
        assertEquals(player2Name, player2.getName(), "Player name should be \"Jack\".");
    }

    /**
     * Test the setting of the name of {@linkplain UserPlayer}.
     */
    @Test
    public void testSetName() {
        String player1Name = "Max";
        UserPlayer player1 = new UserPlayer(player1Name);
        assertEquals(player1Name, player1.getName(), "Player name should be \"Max\".");
        String newPlayer1Name = "Will";
        player1.setName(newPlayer1Name);
        assertEquals(newPlayer1Name, player1.getName(), "Player name should be \"Will\".");

        String player2Name = "Jack";
        UserPlayer player2 = new UserPlayer(player2Name);
        assertEquals(player2Name, player2.getName(), "Player name should be \"Jack\".");
        String newPlayer2Name = "Matt";
        player2.setName(newPlayer2Name);
        assertEquals(newPlayer2Name, player2.getName(), "Player name should be \"Matt\".");
    }

    /**
     * Test generation of the hash code of the {@linkplain UserPlayer}.
     */
    @Test
    public void testHashCode() {
        String player1Name = "Max";
        int hashP1Name = 31 * player1Name.hashCode();

        UserPlayer player1 = new UserPlayer(player1Name);
        assertEquals(hashP1Name, player1.hashCode(), "Player \"Max\" hashcode should be the same.");

        String player2Name = "JAck";
        int hashP2Name = 31 * player2Name.hashCode();

        Player player2 = new UserPlayer(player2Name);
        assertEquals(hashP2Name, player2.hashCode(), "Player \"Jack\" hashcode should be the same.");
    }

    /**
     * Test check if {@linkplain UserPlayer} is equal to another {@linkplain Object},
     * supposedly {@linkplain UserPlayer}.
     */
    @Test
    public void testEquals() {
        UserPlayer player1 = new UserPlayer("Max");
        Player player2 = new UserPlayer("Jack");
        Player player3 = new UserPlayer("Max");
        Player nullPlayer = null;
        String diffClass = "Max";


        boolean equals1 = player1.equals(player2);
        boolean equals2 = player1.equals(player3);
        boolean equals3 = player1.equals(nullPlayer);
        boolean equals4 = player1.equals(diffClass);
        boolean equals5 = player1.equals(player1);

        assertFalse(equals1, "Player1 \"Max\" and Player2 \"Jack\" should not be the same.");

        assertTrue(equals2, "Player1 \"Max\" and Player3 \"Max\" should be the same.");

        assertFalse(equals3, "Player1 \"Max\" and Player null should not be the same.");

        assertFalse(equals4, "Player1 \"Max\" and String \"Max\" should not be the same.");

        assertTrue(equals5, "Player1 \"Max\" should be the same as itself.");

    }
}