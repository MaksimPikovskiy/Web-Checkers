package com.webcheckers.ui;

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
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.webcheckers.ui.WebServer.HOME_URL;


/**
 * This class will test the {@linkplain GetGameRoute} class and all of its methods.
 *
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
@Tag("UI-Tier")
public class GetGameRouteTest {

    private GetGameRoute CuT;

    private GameManager gameManager;

    // Attributes, which hold mock objects
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private TemplateEngineTester tester;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        tester = new TemplateEngineTester();
        engine = mock(TemplateEngine.class);
        when(engine.render( Mockito.any())).thenAnswer(tester.makeAnswer());

        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        response = mock(Response.class);
        
        gameManager = mock(GameManager.class);

        CuT = new GetGameRoute(engine,gameManager);
    }

    /**
     * Test the getGameRoute if there is no active player session.
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
        verify(response).redirect(HOME_URL);
    }

    /**
     * Test the getGameRoute if there is an active player session, but no game object
     */
    @Test
    public void testNoGame(){
        Player player = mock(Player.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(gameManager.getGame(player)).thenReturn(null);

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
     * Test the getGameRoute if there is an active player session and a game 
     */
    @Test
    public void testHasGame(){
        Player player = mock(Player.class);
        CheckersGame game = mock(CheckersGame.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(gameManager.getGame(player)).thenReturn(game);

        try{
            CuT.handle(request,response);
        }catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }
        
        tester.assertViewModelIsaMap();
        tester.assertViewModelExists();
        tester.assertViewName("game.ftl");
        verify(engine).render(any(ModelAndView.class));
    }

    /**
     * Test the getGameRoute if there is an active player session and a game when the game has ended
     */
    @Test
    public void testGameOver(){
        Player player = mock(Player.class);
        CheckersGame game = mock(CheckersGame.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(gameManager.getGame(player)).thenReturn(game);
        when(game.hasGameEnded()).thenReturn("Player has resigned");

        try{
            CuT.handle(request,response);
        }catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        Gson gson = new Gson();
        final Map<String, Object> modeOptions = new HashMap<>(2);
        modeOptions.put("isGameOver", true);
        modeOptions.put("gameOverMessage", "Player has resigned");
        
        tester.assertViewModelIsaMap();
        tester.assertViewModelExists();
        tester.assertViewName("game.ftl");
        verify(engine).render(any(ModelAndView.class));
        tester.assertViewModelAttribute("modeOptionsAsJSON", gson.toJson(modeOptions));
    }
}
