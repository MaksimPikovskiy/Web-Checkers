package com.webcheckers.ui.spectator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import spark.*;

import static org.mockito.Mockito.*;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Player;
import com.webcheckers.ui.GetHomeRoute;

import static com.webcheckers.ui.WebServer.HOME_URL;


/**
 * This class will test the {@linkplain GetSpectateStopWatchingRoute} class and all of its methods.
 *
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
@Tag("UI-Tier")
public class GetSpectateStopWatchingRouteTest {

    private GetSpectateStopWatchingRoute CuT;

    private GameManager gameManager;

    // Attributes, which hold mock objects
    private Request request;
    private Session session;
    private Response response;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        response = mock(Response.class);
        
        gameManager = mock(GameManager.class);

        CuT = new GetSpectateStopWatchingRoute(gameManager);
    }

    /**
     * Test the GetSpectateStopWatchingRoute if there is no active player session.
     */
    @Test
    public void testNoPlayerSession(){
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(null);

        Object ret = null;
        try{
            ret = CuT.handle(request,response);
        }catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }
        
        assertNull(ret);

        verify(session).removeAttribute(PostSpectateCheckTurnRoute.MOVE_COUNT_KEY);
        verify(gameManager,never()).quit(null);
        verify(response).redirect(HOME_URL);
    }

    /**
     * Test the GetSpectateStopWatchingRoute with an active player session.
     */
    @Test
    public void testPlayerSession(){
        Player player = mock(Player.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);

        Object ret = null;
        try{
            ret = CuT.handle(request,response);
        }catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }
        
        assertNull(ret);
        
        verify(session).removeAttribute(PostSpectateCheckTurnRoute.MOVE_COUNT_KEY);
        verify(gameManager).quit(player);
        verify(response).redirect(HOME_URL);
    }
}
