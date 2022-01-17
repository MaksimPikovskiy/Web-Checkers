package com.webcheckers.ui.spectator;

import static com.webcheckers.ui.WebServer.HOME_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.webcheckers.appl.GameManager;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.model.UserPlayer;
import com.webcheckers.ui.GetHomeRoute;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

/**
 * The unit test suite for the {@linkplain PostSpectateCheckTurnRoute} component.
 *
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
@Tag("UI-Tier")
class PostSpectateCheckTurnRouteTest {

    /**
     * The component-under-test (CuT).
     */
    private PostSpectateCheckTurnRoute CuT;


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

        CuT = new PostSpectateCheckTurnRoute(gameManager);
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
     * Test that when LAST_TURN_KEY does not exist in the http session, true message is passed.
     */
    @Test
    public void testNoLastTurnKey(){
        Player p1 = mock(UserPlayer.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(p1);
        when(session.attribute(PostSpectateCheckTurnRoute.MOVE_COUNT_KEY)).thenReturn(null);

        CheckersGame game = mock(CheckersGame.class);
        when(gameManager.getGame(p1)).thenReturn(game);
        when(game.getMoveCount()).thenReturn(0);
        Object validMessage = null;

        try{
            validMessage = CuT.handle(request,response);
        } catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        verify(session).attribute(PostSpectateCheckTurnRoute.MOVE_COUNT_KEY,"0");
        assertNotNull(validMessage);
        assertEquals("{\"text\":\"true\",\"type\":\"INFO\"}", validMessage);
        
    }

    /**
     * Test that when the move count increments, true message is passed
     */
    @Test
    public void testTurnCountChanged(){
        Player p1 = mock(UserPlayer.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(p1);
        when(session.attribute(PostSpectateCheckTurnRoute.MOVE_COUNT_KEY)).thenReturn("0");

        CheckersGame game = mock(CheckersGame.class);
        when(gameManager.getGame(p1)).thenReturn(game);
        when(game.getMoveCount()).thenReturn(1);
        Object validMessage = null;

        try{
            validMessage = CuT.handle(request,response);
        } catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }
        verify(session,atLeastOnce()).attribute(PostSpectateCheckTurnRoute.MOVE_COUNT_KEY,"1");
        assertNotNull(validMessage);
        assertEquals("{\"text\":\"true\",\"type\":\"INFO\"}", validMessage);
    }

    /**
     * Test that when the move count does not change, false message is passed.
     */
    @Test
    public void testTurnCountNoChanged(){
        Player p1 = mock(UserPlayer.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(p1);
        when(session.attribute(PostSpectateCheckTurnRoute.MOVE_COUNT_KEY)).thenReturn("1");

        CheckersGame game = mock(CheckersGame.class);
        when(gameManager.getGame(p1)).thenReturn(game);
        when(game.getMoveCount()).thenReturn(1);
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





