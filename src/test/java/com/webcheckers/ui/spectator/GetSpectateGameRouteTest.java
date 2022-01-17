package com.webcheckers.ui.spectator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import spark.*;

import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.ui.GetGameRoute;
import com.webcheckers.ui.GetHomeRoute;
import com.webcheckers.ui.TemplateEngineTester;
import com.webcheckers.util.Message;

import static com.webcheckers.ui.WebServer.HOME_URL;


/**
 * This class will test the {@linkplain GetSpectateGameRoute} class and all of its methods.
 *
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
@Tag("UI-Tier")
public class GetSpectateGameRouteTest {

    private GetSpectateGameRoute CuT;

    private GameManager gameManager;

    // Attributes, which hold mock objects
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private TemplateEngineTester tester;

    private final String messageParam = "?message=";

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        tester = new TemplateEngineTester();
        engine = mock(TemplateEngine.class);
        when(engine.render(Mockito.any())).thenAnswer(tester.makeAnswer());

        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        response = mock(Response.class);
        
        gameManager = mock(GameManager.class);

        CuT = new GetSpectateGameRoute(engine,gameManager);
    }

    /**
     * Test the GetSpectateGameRoute if there is no active player session.
     */
    @Test
    public void testNoPlayerSession(){
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(null);

        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            fail("Route threw exception: " + e.toString());
        }

        verify(response).redirect(HOME_URL);
    }

    /**
     * Test the GetSpectateGameRoute if there is an active player session, but no game object
     */
    @Test
    public void testNoGame(){
        Player player = mock(Player.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(request.queryParams("gameID")).thenReturn(null);
        when(gameManager.getGame(player,0)).thenReturn(null);

        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            fail("Route threw exception: " + e.toString());
        }

        verify(response).redirect(HOME_URL+messageParam+GetSpectateGameRoute.INVALID_GAME_MSG);
    }

    /**
     * Test the GetSpectateGameRoute if there is an active player session and a game ID
     */
    @Test
    public void testHasGame(){
        Player player = mock(Player.class);
        CheckersGame game = mock(CheckersGame.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(request.queryParams("gameID")).thenReturn("0");
        when(gameManager.getGame(player,0)).thenReturn(game);
        

        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            fail("Route threw exception: " + e.toString());
        }
        
        tester.assertViewModelExists();
        tester.assertViewModelIsaMap();
        tester.assertViewName("game.ftl");
        verify(engine).render(any(ModelAndView.class));
    }

    /**
     * Test the GetSpectateGameRoute if there is an active player session and a game when the game has ended
     */
    @Test
    public void testGameOver(){
        String gameOverMSG = "Player has resigned";

        Player player = mock(Player.class);
        CheckersGame game = mock(CheckersGame.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(request.queryParams("gameID")).thenReturn("0");
        when(gameManager.getGame(player,0)).thenReturn(game);
        when(game.hasGameEnded()).thenReturn(gameOverMSG);
        

        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            fail("Route threw exception: " + e.toString());
        }

        tester.assertViewModelExists();
        tester.assertViewModelIsaMap();
        tester.assertViewName("game.ftl");
        verify(engine).render(any(ModelAndView.class));
        tester.assertViewModelAttribute(GetGameRoute.ATTR_MESSAGE, Message.info(gameOverMSG));
    }
}
