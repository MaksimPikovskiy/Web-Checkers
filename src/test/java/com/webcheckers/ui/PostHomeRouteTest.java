package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This class will test the PostHomeRoute class for handling where the
 * program should go based on the current user and valid user input
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
@Tag("UI-Tier")
class PostHomeRouteTest {
    /**
     * private object for PostHomeRoute mocking
     */
    private PostHomeRoute CuT;

    // Friendly object
    /**
     * private mock of a playerLobby
     */
    private PlayerLobby playerLobby;

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
     * mock template engine
     */
    private TemplateEngine engine;
    /**
     * template engine tester for method testing
     */
    private TemplateEngineTester testHelper;
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
        testHelper = new TemplateEngineTester();
        engine = mock(TemplateEngine.class);
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        request = mock(Request.class);
        response = mock(Response.class);

        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        playerLobby = mock(PlayerLobby.class);
        gameManager = mock(GameManager.class);

        CuT = new PostHomeRoute(engine, playerLobby, gameManager);
    }

    /**
     * This method tests when current player is null
     */
    @Test
    public void testNullPlayer(){
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(null);
        when(request.queryParams("player")).thenReturn(null);   //both players null
        when(playerLobby.getPlayerCount()).thenReturn(1);
        Object output = null;

        try {
            output = CuT.handle(request, response);
        } catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        assertNull(output);

        testHelper.assertViewModelAttribute("title", GetHomeRoute.TITLE);  //"title" has sign in's title
        testHelper.assertViewModelAttribute(GetHomeRoute.ATTR_MESSAGE, GetHomeRoute.WELCOME_MSG); //current player count in vm
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelExists();
        testHelper.assertViewName("home.ftl");

    }

    /**
     * This method tests when a player challenges another player not in a game
     */
    @Test
    public void testChallengeValidPlayer(){
        player = mock(Player.class);
        CheckersGame game = mock(CheckersGame.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(request.queryParams("player")).thenReturn("player2");   
        when(request.queryParams("mode")).thenReturn(null); 
        when(playerLobby.getPlayerCount()).thenReturn(2);
        when(gameManager.createGame(player, "player2")).thenReturn(game);

        try{
            CuT.handle(request,response);
        }catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        verify(response).redirect(WebServer.GAME_URL);
    }

    /**
     * This method tests when a player challenges another player already in a game
     */
    @Test
    public void testChallengeInValidPlayer(){
        player = mock(Player.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(request.queryParams("player")).thenReturn("player2");   
        when(request.queryParams("mode")).thenReturn(null); 
        when(gameManager.createGame(player, "player2")).thenReturn(null);

        try{
            CuT.handle(request,response);
        }catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        final String expectedString = "player2 is already in a game! Please challenge someone else.";
        testHelper.assertViewModelAttribute(GetHomeRoute.ATTR_MESSAGE, Message.error(expectedString)); //current player count in vm
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelExists();
        testHelper.assertViewName("home.ftl");
    }

    /**
     * This method tests when a player spectates an invalid game
     */
    @Test
    public void testSpectateInValidGame(){
        player = mock(Player.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(request.queryParams("player")).thenReturn("player2");   
        when(request.queryParams("mode")).thenReturn("spectate"); 
        when(request.queryParams("gameID")).thenReturn(null); 
        when(gameManager.createGame(player, "player2")).thenReturn(null);

        try{
            CuT.handle(request,response);
        }catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        final String expectedString = "Unable to spectate game, The game has already ended";
        testHelper.assertViewModelAttribute(GetHomeRoute.ATTR_MESSAGE, Message.error(expectedString)); //current player count in vm
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelExists();
        testHelper.assertViewName("home.ftl");
    }

    /**
     * This method tests when a player spectates an valid game
     */
    @Test
    public void testSpectateValidGame(){
        player = mock(Player.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        when(request.queryParams("player")).thenReturn("player2");   
        when(request.queryParams("mode")).thenReturn("spectate"); 
        when(request.queryParams("gameID")).thenReturn("1"); 
        when(gameManager.createGame(player, "player2")).thenReturn(null);

        try{
            CuT.handle(request,response);
        }catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        final String spectateURL = WebServer.SPECTATE_GAME_URL + "?gameID=1";
        verify(response).redirect(spectateURL);
    }
}