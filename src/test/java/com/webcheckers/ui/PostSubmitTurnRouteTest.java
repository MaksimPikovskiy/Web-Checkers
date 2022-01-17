package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.AIPlayer;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

/**
 * This class will test the PostSubmitTurnRoute
 * to ensure that the submitted turn is handled properly
 * within the UI section of the program
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
@Tag("UI-tier")
class PostSubmitTurnRouteTest {
    /**
     * CuT object used for testing the PostSubmitTurnRoute class
     */
    private PostSubmitTurnRoute CuT;
    /**
     * session used for mock
     */
    private Session session;
    /**
     * request used in testing
     */
    private Request request;
    /**
     * response object used in mock
     */
    private Response response;
    /**
     * playerLobby object for mock
     */
    private PlayerLobby playerLobby;

    private GameManager gameManager;
    /**
     * this class is a setup before each test
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        gameManager = mock(GameManager.class);
        playerLobby = mock(PlayerLobby.class);
        CuT = new PostSubmitTurnRoute(gameManager);
    }

    /**
     * this method will test when the player given is null
     */
    @Test
    public void testNoPlayerRedirect(){
        when(request.queryParams(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(null);
        
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
     * this method will test when player is not null and valid game
     * is returned from game manager and submit is null
     */
    @Test
    public void testSubmitWithNoMovesMade(){
        CheckersGame game = mock(CheckersGame.class);
        playerLobby.addPlayer("player");
        Gson gson = new Gson();
        Message msg = Message.info("Turn Submitted!");

        when(request.queryParams(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn("player");
        when(gameManager.getGame(playerLobby.getPlayer("player"))).thenReturn(game);
        when(game.submit()).thenReturn(null);

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
     * this method tests when the player is not null and submit is not null
     */
    @Test
    public void testValidSubmit(){
        CheckersGame game = mock(CheckersGame.class);
        playerLobby.addPlayer("player");
        Gson gson = new Gson();
        Message msg = Message.error("error");

        when(request.queryParams(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn("player");
        when(gameManager.getGame(playerLobby.getPlayer("player"))).thenReturn(game);
        when(game.submit()).thenReturn("error");

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
     * this method tests when the player is not null and submit is not null
     */
    @Test
    public void testValidSubmitWithAIPlayer() {
        AIPlayer aiPlayer = mock(AIPlayer.class);

        CheckersGame game = mock(CheckersGame.class);
        playerLobby.addPlayer("player");
        Gson gson = new Gson();
        Message msg = Message.error("error");

        when(request.queryParams(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn("player");
        when(gameManager.getGame(playerLobby.getPlayer("player"))).thenReturn(game);
        when(game.submit()).thenReturn("error");
        when(game.getWhitePlayer()).thenReturn(aiPlayer);

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