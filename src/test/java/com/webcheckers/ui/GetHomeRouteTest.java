package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;

import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

/**
 *This class tests the object class of
 * GetHomeRoute and then handle method within.
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
@Tag(("UI-Tier"))
class GetHomeRouteTest {
    /**
     * GetHomeRoute object for testing
     */
    private GetHomeRoute CuT;

    // Friendly object
    /**
     * PlayerLobby object for the testing
     */
    private PlayerLobby playerLobby;

    // Attributes, which hold mock objects
    /**
     * Request object for mocking
     */
    private Request request;
    /**
     * session for mocking in the test class
     */
    private Session session;
    /**
     * Response object for the mock testing
     */
    private Response response;
    /**
     * Template Engine object for mock
     */
    private TemplateEngine engine;
    /**
     * Tester object used for testing template engine
     */
    private TemplateEngineTester testHelper;
    /**
     * game manager object used in testing for route
     */
    private GameManager gameManager;

    /**
     * the test method to use before every test class for initialization
     */
    @BeforeEach
    public void setup(){
        testHelper = new TemplateEngineTester();
        engine = mock(TemplateEngine.class);
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        request = mock(Request.class);
        response = mock(Response.class);

        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        playerLobby = mock(PlayerLobby.class);
        gameManager = mock(GameManager.class);

        CuT = new GetHomeRoute(engine, playerLobby, gameManager);
    }

    /**
     * This test method will check that the title of the view model will be the
     * GetHomeRoute.TITLE and that the view model exists and is a map.
     */
    @Test
    public void test1(){
        Player player = mock(Player.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);

        Object obj = CuT.handle(request, response);

        assertNull(obj);

        testHelper.assertViewModelAttribute("title", GetHomeRoute.TITLE);  //"title" has sign in's title
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelExists();
        testHelper.assertViewName("home.ftl");
    }

    /**
     * This method tests when the player object is null and thus the proper response
     * for the view model must be created
     */
    @Test
    public void testNullPlayer(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();

        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(null);
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        verify(engine).render(any(ModelAndView.class));

        testHelper.assertViewModelAttribute("title", GetHomeRoute.TITLE);  //"title" has sign in's title
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelExists();
        testHelper.assertViewName("home.ftl");
    }

    /**
     * This method tests when the getGameOver and isInGame are both false so that the
     * proper handle occurs
     */
    @Test
    public void testNoGame(){
        Player player = mock(Player.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(player.getGameOver()).thenReturn(false);
        when(player.isInGame()).thenReturn(false);
        when(playerLobby.getPlayerCount()).thenReturn(3);

        CuT.handle(request, response);

        testHelper.assertViewModelAttribute("playerList", playerLobby.getCurrentPlayers());  //"title" has sign in's title
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelExists();

    }

    /**
     * This method tests when the there is a message parameter in the url. Both getGameOver and isInGame are false.
     */
    @Test
    public void testMessageParam(){
        Player player = mock(Player.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(player.getGameOver()).thenReturn(false);
        when(player.isInGame()).thenReturn(false);
        String message = "Error message";
        when(request.queryParams("message")).thenReturn(message);
        when(playerLobby.getPlayerCount()).thenReturn(1);

        CuT.handle(request, response);

        testHelper.assertViewModelAttribute("playerList", null); //current player count in vm
        testHelper.assertViewModelAttribute(GetHomeRoute.ATTR_MESSAGE, Message.error(message)); //current player count in vm
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelExists();

    }

    /**
     * This method tests that the getGameOver method for the player and game manager can quit
     * the given player in the GetHomeRoute
     */
    @Test
    public void testGameOverTrue(){
        Player player = mock(Player.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(player.getGameOver()).thenReturn(true);

        CuT.handle(request, response);

        verify(player).getGameOver();
        verify(gameManager).quit(player);

    }

    /**
     * This method will tests if getGameOver returns false and is in game is true, that
     * the proper conditionals are handled
     */
    @Test
    public void testInGame(){
        Player player = mock(Player.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(player.getGameOver()).thenReturn(false);
        when(player.isInGame()).thenReturn(true);

        Object ret = null;
        try{
            ret = CuT.handle(request,response);
        }catch(Exception e) {
            fail("Route threw exception: " + e.toString());
        }

        assertNull(ret);
        verify(response).redirect(WebServer.GAME_URL);
    }
}