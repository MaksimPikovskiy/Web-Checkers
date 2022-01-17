package com.webcheckers.ui;

import static com.webcheckers.ui.WebServer.HOME_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.webcheckers.appl.GameManager;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.model.UserPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

/**
 * The unit test suite for the {@linkplain PostCheckTurnRoute} component.
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
@Tag("UI-Tier")
class PostCheckTurnRouteTest {

    /**
     * The component-under-test (CuT).
     */
    private PostCheckTurnRoute CuT;


    // Attributes, which hold mock objects
    private Request request;
    private Session session;
    private Response response;
    private GameManager gameManager;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        response = mock(Response.class);

        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        gameManager = mock(GameManager.class);

        CuT = new PostCheckTurnRoute(gameManager);
    }

    /**
     * Test that null {@linkplain Player} redirects to HOME_URL.
     */
    @Test
    public void testNullPlayer(){
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(null);

        Object ret = null;
        try{
            ret = CuT.handle(request,response);
        }catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }
        
        assertNull(ret);
        verify(response).redirect(HOME_URL);
    }

    /**
     * Test that null {@linkplain CheckersGame} redirects to HOME_URL.
     */
    @Test
    public void testGameNull(){
        Player p1 = mock(UserPlayer.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(p1);

        when(gameManager.getGame(p1)).thenReturn(null);

        Object ret = null;
        try{
            ret = CuT.handle(request,response);
        }catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }
        
        assertNull(ret);
        verify(response).redirect(HOME_URL);
    }

    /**
     * Test that when game has ended, true valid message is passed.
     */
    @Test
    public void testGameEnded(){
        Player p1 = mock(UserPlayer.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(p1);

        CheckersGame game = mock(CheckersGame.class);
        when(gameManager.getGame(p1)).thenReturn(game);
        String mockGameEndMSG = "Game didn't end.";
        when(game.hasGameEnded()).thenReturn(mockGameEndMSG);

        Object validMessage = null;

        try{
            validMessage = CuT.handle(request,response);
        } catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        assertNotNull(validMessage);
        assertEquals("{\"text\":\"true\",\"type\":\"INFO\"}", validMessage);
        
    }

    /**
     * Test that when it is red turn, true valid message is passed.
     */
    @Test
    public void testRedTurn(){
        Player p1 = mock(UserPlayer.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(p1);

        CheckersGame game = mock(CheckersGame.class);
        when(gameManager.getGame(p1)).thenReturn(game);
        when(game.hasGameEnded()).thenReturn(null);
        when(game.getRedPlayer()).thenReturn(p1);
        when(game.currentTurn()).thenReturn(Piece.Color.RED);

        Object validMessage = null;

        try{
            validMessage = CuT.handle(request,response);
        } catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        assertNotNull(validMessage);
        assertEquals("{\"text\":\"true\",\"type\":\"INFO\"}", validMessage);
        
    }

    /**
     * Test that when it is white turn, true valid message is passed.
     */
    @Test
    public void testWhiteTurn(){
        Player p1 = mock(UserPlayer.class);
        Player p2 = mock(UserPlayer.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(p2);

        CheckersGame game = mock(CheckersGame.class);
        when(gameManager.getGame(p2)).thenReturn(game);
        when(game.hasGameEnded()).thenReturn(null);
        when(game.getRedPlayer()).thenReturn(p1);
        when(game.currentTurn()).thenReturn(Piece.Color.WHITE);
        when(game.getWhitePlayer()).thenReturn(p2);

        Object validMessage = null;

        try{
            validMessage = CuT.handle(request,response);
        } catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        assertNotNull(validMessage);
        assertEquals("{\"text\":\"true\",\"type\":\"INFO\"}", validMessage);
        
    }

    /**
     * Test that when it is red player but white turn, false valid message is passed.
     */
    @Test
    public void testRedPlayerWhiteTurn(){
        Player p1 = mock(UserPlayer.class);
        Player p2 = mock(UserPlayer.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(p1);

        CheckersGame game = mock(CheckersGame.class);
        when(gameManager.getGame(p1)).thenReturn(game);
        when(game.hasGameEnded()).thenReturn(null);
        when(game.getRedPlayer()).thenReturn(p1);
        when(game.currentTurn()).thenReturn(Piece.Color.WHITE);
        when(game.getWhitePlayer()).thenReturn(p2);
        when(game.currentTurn()).thenReturn(Piece.Color.WHITE);

        Object validMessage = null;

        try{
            validMessage = CuT.handle(request,response);
        } catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        assertNotNull(validMessage);
        assertEquals("{\"text\":\"false\",\"type\":\"INFO\"}", validMessage);
        
    }

    /**
     * Test that when it is white player but red turn, false valid message is passed.
     */
    @Test
    public void testWhitePlayerRedTurn(){
        Player p1 = mock(UserPlayer.class);
        Player p2 = mock(UserPlayer.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(p2);

        CheckersGame game = mock(CheckersGame.class);
        when(gameManager.getGame(p2)).thenReturn(game);
        when(game.hasGameEnded()).thenReturn(null);
        when(game.getRedPlayer()).thenReturn(p1);
        when(game.currentTurn()).thenReturn(Piece.Color.RED);
        when(game.getWhitePlayer()).thenReturn(p2);
        when(game.currentTurn()).thenReturn(Piece.Color.RED);

        Object validMessage = null;

        try{
            validMessage = CuT.handle(request,response);
        } catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        assertNotNull(validMessage);
        assertEquals("{\"text\":\"false\",\"type\":\"INFO\"}", validMessage);
        
    }
}





