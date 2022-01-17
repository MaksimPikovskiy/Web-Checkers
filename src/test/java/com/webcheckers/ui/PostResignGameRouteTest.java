package com.webcheckers.ui;

import static com.webcheckers.ui.WebServer.HOME_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

/**
 * This test class tests the functionality of the
 * PostResignGameRoute to ensure that multiple different cases are
 * properly handled
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 */
@Tag(("UI-Tier"))
class PostResignGameRouteTest {
    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component, so we only need one.
     * The {@link PlayerLobby} component is thoroughly tested, so
     * we can use it safely as a "friendly" dependency.
     */
    private PostResignGameRoute CuT;

    // Friendly object
    /**
     * PlayerLobby object to mock
     */
    private PlayerLobby playerLobby;

    // Attributes, which hold mock objects
    /**
     * request object to use in handle method
     */
    private Request request;
    /**
     * mock session for the handle/init methods of the route
     */
    private Session session;
    /**
     * response object to utilize within the handle mock
     */
    private Response response;
    /**
     * GameManager object for mocking
     */
    private GameManager gameManager;

    /**
     * This method is the setup for each test method.
     * It mocks certain classes to ensure valid inputs and outputs
     */
    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        response = mock(Response.class);

        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        playerLobby = mock(PlayerLobby.class);
        gameManager = mock(GameManager.class);

        CuT = new PostResignGameRoute(gameManager);
    }

    /**
     * This method verifies that when the game is null, the response is a redirect to the
     * HOME_URL
     */
    @Test
    public void testNoGame(){
        Player player = mock(Player.class);
        when(gameManager.getGame(player)).thenReturn(null);

        try {
            Object obj = CuT.handle(request, response);
            assertNull(obj);
        } catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }
        
        verify(response).redirect(HOME_URL);
    }

    /**
     * This test ensures that the player is a null object
     */
    @Test
    public void testNullPlayer(){
        when(request.queryParams(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(null);

        try{
            CuT.handle(request,response);
        } catch(Exception e){
            //expected
        }

        verify(response).redirect(HOME_URL);
    }

    /**
     * This method tests when game is not null and resign returns true
     */
    @Test
    public void testCanResign(){      //this one does game != null and resign = true
        Player player = mock(Player.class);
        Player player2 = mock(Player.class);
        CheckersGame game = new CheckersGame(player, player2, 1, null);
        playerLobby.addPlayer("player");

        when(request.queryParams(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn("player");
        when(gameManager.getGame(playerLobby.getPlayer("player"))).thenReturn(game);
        when(gameManager.resign(playerLobby.getPlayer("player"))).thenReturn(true);

        Gson gson = new Gson();
        Message msg = Message.info("resigned");
        Object ret = null;
        try{
            ret = CuT.handle(request,response);
        }catch(Exception e) {
            fail("Route threw exception: " + e.toString());
        }

        assertNotNull(ret);
        assertEquals(gson.toJson(msg), ret);
    }

    /**
     * This method tests when game is not null and
     * GameManager.resign returns false
     */
    @Test
    public void testUnableToResign(){    //this one does game != and gameManager.resign = false
        Player player = mock(Player.class);
        Player player2 = mock(Player.class);
        CheckersGame game = new CheckersGame(player, player2, 1, null);
        playerLobby.addPlayer("player");

        when(request.queryParams(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn("player");
        when(gameManager.getGame(playerLobby.getPlayer("player"))).thenReturn(game);

        Gson gson = new Gson();
        Message msg = Message.error("Unable to resign, other player has already resigned");

        Object ret = null;
        try{
            ret = CuT.handle(request,response);
        }catch(Exception e) {
            fail("Route threw exception: " + e.toString());
        }

        assertNotNull(ret);
        assertEquals(gson.toJson(msg), ret);
    }
}