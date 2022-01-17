package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.model.UserPlayer;
import com.webcheckers.util.Message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

/**
 * This class will test the backup functionality
 * of the checkers game, going through the UI-tier
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 */
@Tag("UI-Tier")
class PostBackUpMoveRouteTest {
    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component, so we only need one.
     * The {@link PlayerLobby} component is thoroughly tested, so
     * we can use it safely as a "friendly" dependency.
     */
    private PostBackUpMoveRoute CuT;

    // Attributes, which hold mock objects
    /**
     * private request object to use for handle testing
     */
    private Request request;
    /**
     * private session object to use for handle testing
     */
    private Session session;
    /**
     * private response object to use for handle method testing
     */
    private Response response;
    /**
     * mock game manager
     */
    private GameManager gameManager;
    /**
     * mock player object
     */
    private Player player;

    /**
     * this method must be called before all tests, it sets up the entire class for testing
     */
    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        response = mock(Response.class);

        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        gameManager = mock(GameManager.class);

        CuT = new PostBackUpMoveRoute(gameManager);
    }

    /**
     * this method tests when a checkers game is created and a valid game is returned in getGame
     */
    @Test
    public void testCanNotBackUp(){
        Player p1 = new UserPlayer("Bill");
        Player p2 = new UserPlayer("Mary");

        CheckersGame game= new CheckersGame(p1, p2, 1, null);
        when(request.session()).thenReturn(session);
        when(gameManager.getGame(player)).thenReturn(game);

        Gson gson = new Gson();
        Message msg = Message.error("No more moves to undo");

        Object string = null;
        try{
            string = CuT.handle(request,response);
        } catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        assertNotNull(string);
        assertEquals(gson.toJson(msg), string);
    }

    /**
     * This method tests when the getGame is null or null game.
     */
    @Test
    public void testNoGame(){
        when(request.session()).thenReturn(session);
        when(gameManager.getGame(player)).thenReturn(null);

        Object ret = null;
        try{
            ret = CuT.handle(request,response);
        }catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }
        
        assertNull(ret);
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * this method tests an undo move within the game call and how handle handles it.
     */
    @Test
    public void testCanBackUp(){
        CheckersGame game = mock(CheckersGame.class);
        when(request.session()).thenReturn(session);
        when(gameManager.getGame(player)).thenReturn(game);
        when(game.undoMove()).thenReturn("Undid Move");
        
        Gson gson = new Gson();
        Message msg = Message.info("Undid Move");

        Object string = null;
        try{
            string = CuT.handle(request,response);
        } catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        assertNotNull(string);
        assertEquals(gson.toJson(msg), string);
    }
}