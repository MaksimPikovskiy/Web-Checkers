package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

/**
 *This test class tests all aspects of the PostSignOutRoute
 *
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 */
@Tag("UI-tier")
public class PostSignOutRouteTest {
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
     * player object for mock
     */
    private Player player;
    /**
     * playerLobby object for mock
     */
    private PlayerLobby playerLobby;
    /**
     * GameManager object for mock
     */
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
        playerLobby = mock(PlayerLobby.class);
        gameManager = mock(GameManager.class);
        player = mock(Player.class);
    }

    /**
     * This method will test if a valid sign out will
     * redirect to the home url
     */
    @Test
    public void test_sign_out() {
        PostSignOutRoute CuT = new PostSignOutRoute(playerLobby,gameManager);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(null);

        Object ret = null;
        try{
            ret = CuT.handle(request,response);
        }catch(Exception e) {
            fail("Route threw exception: " + e.toString());
        }

        assertNull(ret);
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * This method will test when the player is not null that the playerlobby removes the player object
     */
    @Test
    public void test_player_not_null() {
        PostSignOutRoute CuT = new PostSignOutRoute(playerLobby,gameManager);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);

        Object ret = null;
        try{
            ret = CuT.handle(request,response);
        }catch(Exception e) {
            fail("Route threw exception: " + e.toString());
        }

        verify(playerLobby).removePlayer(player.getName());
        assertNull(ret);
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * This method tests if the player is null that the proper redirect occurs.
     */
    @Test
    public void test_player_session_key_removed() {
        PostSignOutRoute CuT = new PostSignOutRoute(playerLobby,gameManager);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(null);

        Object ret = null;
        try{
            ret = CuT.handle(request,response);
        }catch(Exception e) {
            fail("Route threw exception: " + e.toString());
        }

        verify(session).removeAttribute(GetHomeRoute.PLAYER_SESSION_KEY);
        assertNull(ret);
        verify(response).redirect(WebServer.HOME_URL);
    }
}
