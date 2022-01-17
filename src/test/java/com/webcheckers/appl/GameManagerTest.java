package com.webcheckers.appl;

import com.webcheckers.model.AIPlayer;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.model.UserPlayer;
import com.webcheckers.model.Piece.Color;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;

import static org.mockito.Mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class tests all GameManager classes methods
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
@Tag("Application-tier")
class GameManagerTest {

    /**
     * Test the creation of a GameManager
     * object
     */
    @Test
    public void testGameManager(){
        PlayerLobby playerLobby = new PlayerLobby();
        GameManager gameManager = new GameManager(playerLobby);
        assertNotNull(gameManager);
    }

    /**
     * Test the creation of a GameManager game with two players.
     */
    @Test
    public void testCreateGame(){
        PlayerLobby playerLobby = new PlayerLobby();
        Player p1 = new UserPlayer("Jacob");
        Player p2 = new UserPlayer("Max");
        Player p3 = new UserPlayer("Jack");
        Player p4 = new UserPlayer("Jim");
        playerLobby.addPlayer("Jack");
        playerLobby.addPlayer("Max");

        GameManager gameManager = new GameManager(playerLobby);

        //test creating a game with a existing player name
        assertNotNull(gameManager.createGame(p1, "Max"));    //should get a new checkers game with p1 and p2
        assertNull(gameManager.createGame(p3, "Jacob"));
        assertNotNull(gameManager.createGame(p2,"Jack"));
        
        //test creating a game when already in a game returns the current game.
        CheckersGame game1  = gameManager.createGame(p1,"Max");
        CheckersGame game2 = gameManager.createGame(p1,"Jack");
        assertSame(game1, game2);

        //test if an opponent is in a game the game returns null
        assertNull(gameManager.createGame(p4, "Max"));
        assertNull(gameManager.createGame(p4, "Jack"));

        //test that a game with an AI player is created when the opponent is the AI identifier
        CheckersGame game3  = gameManager.createGame(p4, AIPlayer.UID);
        assertNotNull(game3);
        assertTrue(game3.getWhitePlayer() instanceof AIPlayer);
    }

    /**
     * This method tests getting a pre-created game from a Game Manager
     */
    @Test
    public void testGetGame(){
        PlayerLobby playerLobby = new PlayerLobby();
        playerLobby.addPlayer("Max");
        playerLobby.addPlayer("Jack");
        Player p1 = playerLobby.getPlayer("Max");
        Player p2 = playerLobby.getPlayer("Jack");
        GameManager gameManager = new GameManager(playerLobby);

        CheckersGame checkersGame = gameManager.createGame(p1, "Jack");
        assertNotNull(checkersGame);
        assertNull(gameManager.getGame(null));
        assertEquals(checkersGame, gameManager.getGame(p2));
        assertEquals(checkersGame, gameManager.getGame(p1));
    }

    /**
     * This method tests getting a pre-created game from a Game Manager based on a gameID
     */
    @Test
    public void testGetGameWithID(){
        PlayerLobby playerLobby = new PlayerLobby();

        playerLobby.addPlayer("test1");
        playerLobby.addPlayer("test2");
        playerLobby.addPlayer("test3");
        playerLobby.addPlayer("test4");

        Player p1 = playerLobby.getPlayer("test1");
        Player p3 = playerLobby.getPlayer("test3");
        Player p5 = mock(UserPlayer.class);
        GameManager gameManager = new GameManager(playerLobby);

        CheckersGame game1 = gameManager.createGame(p1, "test2"); //0
        CheckersGame game2 = gameManager.createGame(p3, "test4"); //1
        CheckersGame test;
        assertNotNull(game1);
        assertNotNull(game2);
        test = gameManager.getGame(p5,100);
        assertNull(test);

        test = gameManager.getGame(p5,0);
        assertSame(game1,test);
        verify(p5).setBoardView(game1.getRedPlayer().getBoardView());
        verify(p5).setGameOver(true);

        test = gameManager.getGame(p5,1);
        assertSame(game1,test);
    }

    /**
     * Test that getActiveGames() produces a properly formatted list of active games and IDs
     */
    @Test
    public void testGetActiveGames(){
        PlayerLobby playerLobby = new PlayerLobby();

        playerLobby.addPlayer("test1");
        playerLobby.addPlayer("test2");
        playerLobby.addPlayer("test3");
        playerLobby.addPlayer("test4");

        Player p1 = playerLobby.getPlayer("test1");
        Player p3 = playerLobby.getPlayer("test3");
        GameManager gameManager = new GameManager(playerLobby);

        Collection<String[]> expectedGameStrings = new ArrayList<String[]>();
        expectedGameStrings.add(new String[]{"test1 vs. test2", "0"});
        expectedGameStrings.add(new String[]{"test3 vs. test4", "1"});

        CheckersGame game1 = gameManager.createGame(p1, "test2"); //0
        CheckersGame game2 = gameManager.createGame(p3, "test4"); //1
        assertNotNull(game1);
        assertNotNull(game2);
        Collection<String[]> games = gameManager.getActiveGames();
        assertNotNull(games);
        for (String[] gameStrings : games) {
            boolean foundMatch = false;
            for (String[] expectedStrings : expectedGameStrings) {
                if(gameStrings[0].equals(expectedStrings[0])){
                    assertEquals(expectedStrings[1], gameStrings[1]);
                    foundMatch = true;
                }
            }
            assertTrue(foundMatch);
        }
    }

    /**
     * This method test makeAIMove for verifying the player in a game and calling the corresponding AI makeMove.
     * @throws NoSuchFieldException
     */
    @Test
    public void testMakeAIMove() throws NoSuchFieldException{
        PlayerLobby playerLobby= mock(PlayerLobby.class);
        Player p1 = mock(UserPlayer.class);
        AIPlayer ai = mock(AIPlayer.class);
        CheckersGame game = mock(CheckersGame.class);
        when(game.getWhitePlayer()).thenReturn(ai);
        when(game.currentTurn()).thenReturn(Color.RED);

        GameManager gameManager = new GameManager(playerLobby);
        //get a references to the CheckersGame board
        HashMap<Player, CheckersGame> playerGameMap = new HashMap<Player, CheckersGame>();
        playerGameMap.put(p1, game);
        playerGameMap.put(ai, game);
        FieldSetter.setField(gameManager, gameManager.getClass().getDeclaredField("playerGameMap"), playerGameMap);
        
        //no player specified
        gameManager.makeAIMove(null);
        verify(ai,never()).makeMove(game);

        //valid player, but RED turn.
        gameManager.makeAIMove(p1);
        verify(ai,never()).makeMove(game);

        //non AI player, WHITE turn.
        when(game.getWhitePlayer()).thenReturn(p1);
        when(game.currentTurn()).thenReturn(Color.WHITE);
        gameManager.makeAIMove(p1);
        verify(ai,never()).makeMove(game);

        //valid player, WHITE turn.
        when(game.getWhitePlayer()).thenReturn(ai);
        when(game.currentTurn()).thenReturn(Color.WHITE);
        gameManager.makeAIMove(p1);
        verify(ai,atLeastOnce()).makeMove(game);
    }

    /**
     * This method tests resigning a player from a game in GameManager
     */
    @Test
    public void testResign(){
        PlayerLobby playerLobby = new PlayerLobby();
        GameManager gameManager= new GameManager(playerLobby);

        Player p1 = new UserPlayer("Jack");

        playerLobby.addPlayer("Jack");
        playerLobby.addPlayer("Max");

        assertFalse(gameManager.resign(p1));

        gameManager.createGame(p1, "Max");
        assertTrue(gameManager.resign(p1));
    }

    /**
     * This method tests the quit function
     * of the GameManager
     */
    @Test
    public void testQuit(){
        PlayerLobby playerLobby = new PlayerLobby();
        GameManager gameManager= new GameManager(playerLobby);

        Player p1 = new UserPlayer("Jack");
        Player p2 = new UserPlayer("Max");
        playerLobby.addPlayer("Jack");
        playerLobby.addPlayer("Max");

        gameManager.createGame(p1, "Max");
        assertNotNull(p1.getBoardView());
        gameManager.quit(p1);
        assertNull(p1.getBoardView());
        assertNull(p2.getBoardView());
    }
}