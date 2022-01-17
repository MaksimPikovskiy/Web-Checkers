package com.webcheckers.ui;

import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This class will test if the title and player objects for the sign-in GET route are nominal.
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 */
@Tag("UI-tier")
class GetSignInRouteTest {
    /**
     * CuT object used for testing the GetSignInRoute class
     */
    private GetSignInRoute CuT;
    /**
     * template engine for testing
     */
    private TemplateEngine templateEngine;
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
     * this class is a setup before each test
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        templateEngine = mock(TemplateEngine.class);
        response = mock(Response.class);

        CuT = new GetSignInRoute(templateEngine);
    }

    /**
     * test that a new sign in page will be created if none exist
     */
    @Test
    public void newSignIn() {
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewModelAttribute("title", GetSignInRoute.TITLE);  //"title" has sign in's title

        testHelper.assertViewName("sign-in.ftl");
    }

    /**
     * This tests if a valid given new player object is passed
     * to the PLAYER_SESSION_KEY.
     */
    @Test
    public void validPlayerSession1() {
        player = mock(Player.class);
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);

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
     * checks title of sign-in page after a valid player input
     */
    @Test
    public void titleCheckSession2() {
        player = mock(Player.class);
        String title = "Sign-In";
        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(player);
        assertEquals(GetSignInRoute.TITLE, title, "Valid Sign-In title");

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
     * this method checks to make sure that a ModelandView class are
     * made when the player is null (should occur).
     */
    @Test
    public void modelviewCheckSession() {
        player = mock(Player.class);

        when(session.attribute(GetHomeRoute.PLAYER_SESSION_KEY)).thenReturn(null);

        CuT.handle(request, response);
        verify(templateEngine).render(any(ModelAndView.class));
    }
}