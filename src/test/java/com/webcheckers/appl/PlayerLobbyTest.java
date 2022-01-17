package com.webcheckers.appl;

import com.webcheckers.model.Player;
import com.webcheckers.model.UserPlayer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit test suite for the {@linkplain PlayerLobby} component.
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
@Tag("Application-tier")
class PlayerLobbyTest {

    /**
     * Test the creation of {@linkplain PlayerLobby}.
     */
    @Test
    public void testPlayerLobby() {
        PlayerLobby playerLobby = new PlayerLobby();
        assertNotNull(playerLobby, "Instance is null");
    }

    /**
     * Test check for valid names.
     */
    @Test
    public void testValidName() {
        PlayerLobby playerLobby = new PlayerLobby();
        String validName1 = "Max";
        String validName2 = "   Max  ";
        String validName3 = "   Max   Pikovskiy   ";
        String validName4 = " ___M4x__ Pik0vsk1y";


        boolean test1 = playerLobby.isValid(validName1);
        assertTrue(test1, validName1 + " should be a valid name.");

        boolean test2 = playerLobby.isValid(validName2);
        assertTrue(test2, validName2 + " should be a valid name.");

        boolean test3 = playerLobby.isValid(validName3);
        assertTrue(test3, validName3 + " should be a valid name.");

        boolean test4 = playerLobby.isValid(validName4);
        assertTrue(test4, validName4 + " should be a valid name.");
    }

    /**
     * Test check for invalid names.
     */
    @Test
    public void testInvalidName() {
        PlayerLobby playerLobby = new PlayerLobby();
        String invalidName1 = "    ";
        String invalidName2 = "!*$Max";
        String invalidName3 = "!*$";
        String invalidName4 = "________";

        boolean test1 = playerLobby.isValid(invalidName1);
        assertFalse(test1, invalidName1 + " should be an invalid name.");

        boolean test2 = playerLobby.isValid(invalidName2);
        assertFalse(test2, invalidName2 + " should be an invalid name.");

        boolean test3 = playerLobby.isValid(invalidName3);
        assertFalse(test3, invalidName3 + " should be an invalid name.");

        boolean test4 = playerLobby.isValid(invalidName4);
        assertFalse(test4, invalidName4 + " should be an invalid name.");
    }

    /**
     * Test check for taken names.
     */
    @Test
    public void testIsTaken() {
        PlayerLobby playerLobby = new PlayerLobby();
        String name = "Max";
        String sameName = "Max";
        String similarName =  "mAX";
        String differentName = "Jack";

        boolean test1 = playerLobby.isTaken(name);
        assertFalse(test1, name + " should be not be taken.");

        playerLobby.addPlayer(name);

        boolean test2 = playerLobby.isTaken(sameName);
        assertTrue(test2, sameName + " should be taken.");

        boolean test3 = playerLobby.isTaken(similarName);
        assertTrue(test3, similarName + " should be taken.");

        boolean test4 = playerLobby.isTaken(differentName);
        assertFalse(test4, differentName + " should be not be taken.");
    }

    /**
     * Test the addition of players to {@linkplain PlayerLobby}.
     */
    @Test
    public void testAddPlayer() {
        PlayerLobby playerLobby = new PlayerLobby();
        String name1 = "Max";
        String name2 = "Jack";
        String name3 = "mAx";
        String name4 = "!*$Max";

        assertEquals(playerLobby.addPlayer(name1), PlayerLobby.NameValidation.VALID,
                "player " + name1 + " should be valid.");

        assertEquals(playerLobby.addPlayer(name2), PlayerLobby.NameValidation.VALID,
                "player " + name2 + " should be valid");

        assertEquals(playerLobby.addPlayer(name3), PlayerLobby.NameValidation.TAKEN,
                "player " + name3 + " should be taken");

        assertEquals(playerLobby.addPlayer(name4), PlayerLobby.NameValidation.INVALID,
                "player " + name4 + " should be invalid");

    }

    /**
     * Test the removal of players to {@linkplain PlayerLobby}.
     */
    @Test
    public void testRemovePlayer() {
        PlayerLobby playerLobby = new PlayerLobby();
        String name1 = "Max";
        String name2 = "Jack";
        playerLobby.addPlayer(name1);
        playerLobby.addPlayer(name2);

        playerLobby.removePlayer(name1);

        Collection<String> players = playerLobby.getPlayerNames();

        boolean test1 = players.contains(name1);
        assertFalse(test1, name1 + " should have been removed.");

        boolean test2 = players.contains(name2);
        assertTrue(test2, name2 + " should have not been removed.");
    }

    /**
     * Test the retrieval of player count of {@linkplain PlayerLobby}.
     */
    @Test
    public void testGetPlayerCount() {
        PlayerLobby playerLobby = new PlayerLobby();
        playerLobby.addPlayer("Max");
        playerLobby.addPlayer("Jack");

        Player player1 = new UserPlayer("Max");
        Player player2 = new UserPlayer("Jack");

        Map<String, Player> pL= new HashMap<>();
        pL.put("Max", player1);
        pL.put("Jack", player2);

        assertEquals(pL.size(), playerLobby.getPlayerCount(),
                "Player count should be 2.");

        playerLobby.addPlayer("Will");
        Player player3 = new UserPlayer("Will");
        pL.put("Will", player3);

        assertEquals(pL.size(), playerLobby.getPlayerCount(),
                "Player count should be 3");

        playerLobby.removePlayer("Max");
        pL.remove("Max");

        assertEquals(pL.size(), playerLobby.getPlayerCount(),
                "Player count should be returned to 2 after removing a player.");
    }

    /**
     * Test the retrieval of a single player in {@linkplain PlayerLobby}.
     */
    @Test
    public void testGetPlayer() {
        PlayerLobby playerLobby = new PlayerLobby();
        String name1 = "Max";
        String name2 = "Jack";
        playerLobby.addPlayer(name1);
        playerLobby.addPlayer(name2);

        Player player1 = new UserPlayer(name1);
        Player player2 = new UserPlayer(name2);

        boolean test1 = playerLobby.getPlayer(name1).getName() == player1.getName();
        assertTrue(test1, name1 + "should be in the player lobby.");

        boolean test2 = playerLobby.getPlayer(name2).getName() == player2.getName();
        assertTrue(test2, name2 + "should be in the player lobby.");

        String name3 = "Will";
        boolean test3 = playerLobby.getPlayer(name3) == null;
        assertTrue(test3, name3 + "should not be in the player lobby.");
    }

    /**
     * Test the retrieval of names of all players in {@linkplain PlayerLobby}.
     */
    @Test
    public void testGetPlayerNames() {
        PlayerLobby playerLobby = new PlayerLobby();
        String name1 = "Max";
        String name2 = "Jack";
        String name3 = "Will";
        playerLobby.addPlayer(name1);
        playerLobby.addPlayer(name2);
        playerLobby.addPlayer(name3);

        Collection<String> players = new HashSet<>();
        players.add("Max");
        players.add("Jack");
        players.add("Will");

        assertEquals(players, playerLobby.getPlayerNames(),
                "player lobby should contain [Max, Will, Jack]");

        players.add("Matt");

        assertNotEquals(players, playerLobby.getPlayerNames(),
                "player lobby should still contain [Max, Will, Jack]");
    }

    /**
     * Test the retrieval of current players in {@linkplain PlayerLobby}.
     */
    @Test
    public void testGetCurrentPlayers() {
        PlayerLobby playerLobby = new PlayerLobby();
        String name1 = "Max";
        String name2 = "Jack";
        String name3 = "Will";
        playerLobby.addPlayer(name1);
        playerLobby.addPlayer(name2);
        playerLobby.addPlayer(name3);

        Collection<Player> players = playerLobby.getCurrentPlayers();

        boolean test1 = players.contains(playerLobby.getPlayer(name1));
        assertTrue(test1, name1 + "should be in the player lobby.");

        boolean test2 = players.contains(playerLobby.getPlayer(name2));
        assertTrue(test2, name2 + "should be in the player lobby.");

        boolean test3 = players.contains(playerLobby.getPlayer(name3));
        assertTrue(test3, name3 + "should be in the player lobby.");
    }
}