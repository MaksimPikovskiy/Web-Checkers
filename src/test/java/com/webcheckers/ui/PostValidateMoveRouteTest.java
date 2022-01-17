package com.webcheckers.ui;

import static com.webcheckers.ui.WebServer.HOME_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.webcheckers.appl.GameManager;

import com.webcheckers.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import spark.*;

/**
 * The unit test suite for the {@linkplain PostValidateMoveRoute} component.
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
@Tag("UI-Tier")
class PostValidateMoveRouteTest {

    /**
     * The component-under-test (CuT).
     */
    private PostValidateMoveRoute CuT;


    // Attributes, which hold mock objects
    private Request request;
    private Session session;
    private Response response;
    private GameManager gameManager;
    private TemplateEngine engine;
    private TemplateEngineTester tester;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup(){
        tester = new TemplateEngineTester();
        engine = mock(TemplateEngine.class);
        when(engine.render(Mockito.any())).thenAnswer(tester.makeAnswer());

        request = mock(Request.class);
        response = mock(Response.class);

        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        gameManager = mock(GameManager.class);

        CuT = new PostValidateMoveRoute(gameManager);
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
        }catch(Exception e) {
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
        }catch(Exception e) {
            fail("Route threw exception: " + e.toString());
        }

        assertNull(ret);
        verify(response).redirect(HOME_URL);
    }

    /**
     * Test that when valid move is made, true valid INFO message is passed.
     */
    @Test
    public void testValidMove(){
        Player p1 = mock(UserPlayer.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(p1);

        CheckersGame game = mock(CheckersGame.class);
        when(gameManager.getGame(p1)).thenReturn(game);

        when(request.queryParams("actionData")).thenReturn("{\"start\":{\"row\":5,\"cell\":2},\"end\":{\"row\":4,\"cell\":3}}");

        when(game.validateMove(any())).thenReturn(null);

        Object validMessage = null;
        try{
            validMessage = CuT.handle(request,response);
        } catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        assertNotNull(validMessage);
        assertEquals("{\"text\":\"Valid Move!\",\"type\":\"INFO\"}", validMessage);
    }

    /**
     * Test that when invalid move, true valid ERROR message is passed.
     */
    @Test
    public void testInvalidMove(){
        Player p1 = mock(UserPlayer.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(p1);

        CheckersGame game = mock(CheckersGame.class);
        when(gameManager.getGame(p1)).thenReturn(game);

        String validMoveMSG = "Invalid move!";

        when(request.queryParams("actionData")).thenReturn("{\"start\":{\"row\":5,\"cell\":4},\"end\":{\"row\":3,\"cell\":6}}");

        when(game.validateMove(any())).thenReturn(validMoveMSG);

        Object validMessage = null;
        try{
            validMessage = CuT.handle(request,response);
        } catch(Exception e){
            fail("Route threw exception: " + e.toString());
        }

        assertNotNull(validMessage);
        assertEquals("{\"text\":\"Invalid move!\",\"type\":\"ERROR\"}", validMessage);   
    }
}





