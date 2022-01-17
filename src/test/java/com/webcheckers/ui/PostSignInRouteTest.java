package com.webcheckers.ui;

import static com.webcheckers.ui.PostSignInRoute.PLAYER_NAME_ATTR;
import static com.webcheckers.ui.WebServer.HOME_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.webcheckers.appl.PlayerLobby;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;
import org.mockito.Mockito;

/**
 * The unit test suite for the {@linkplain PostSignInRoute} component.
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
@Tag("UI-Tier")
class PostSignInRouteTest {

    private static final String validName = "Max";
    private static final String invalidName1 = "!*$Max";
    private static final String invalidName2 = "     ";
    private static final String takenName = "mAX";

    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component, so we only need one.
     * The {@link PlayerLobby} component is thoroughly tested, so
     * we can use it safely as a "friendly" dependency.
     */
    private PostSignInRoute CuT;

    // Friendly object
    private PlayerLobby playerLobby;

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
        when(engine.render(Mockito.any())).thenAnswer(tester.makeAnswer());

        request = mock(Request.class);
        response = mock(Response.class);

        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        playerLobby = new PlayerLobby();

        CuT = new PostSignInRoute(playerLobby, engine);
    }

    /**
     * Test that addPlayer switch case action handled valid name inputted by a user.
     */
    @Test
    public void validNameSignIn() {
        when(request.queryParams(PLAYER_NAME_ATTR)).thenReturn(validName);

        try {
            CuT.handle(request, response);
        }
        catch(Exception e) {
            fail();
        }

        assertNotNull(playerLobby.getPlayer(validName));

        verify(session).attribute(GetHomeRoute.PLAYER_SESSION_KEY, playerLobby.getPlayer(validName));

        tester.assertViewModelExists();
        tester.assertViewModelIsaMap();

        tester.assertViewModelAttribute("title", GetSignInRoute.TITLE);

        tester.assertViewName("home.ftl");

        verify(response).redirect(HOME_URL);
    }

    /**
     * Test that addPlayer switch case action handled invalid name inputted by a user.
     * First test having non-alphanumeric characters and second having only white spaces.
     */
    @Test
    public void invalidNameSignIn() {
        when(request.queryParams(PLAYER_NAME_ATTR)).thenReturn(invalidName1);

        try {
            CuT.handle(request, response);
        }
        catch(Exception e) {
            fail();
        }

        assertNull(playerLobby.getPlayer(invalidName1));

        tester.assertViewModelExists();
        tester.assertViewModelIsaMap();

        tester.assertViewModelAttribute("title", GetSignInRoute.TITLE);

        tester.assertViewName("sign-in.ftl");

        when(request.queryParams(PLAYER_NAME_ATTR)).thenReturn(invalidName2);

        try {
            CuT.handle(request, response);
        }
        catch(Exception e) {
            fail();
        }

        assertNull(playerLobby.getPlayer(invalidName2));

        tester.assertViewModelExists();
        tester.assertViewModelIsaMap();

        tester.assertViewModelAttribute("title", GetSignInRoute.TITLE);

        tester.assertViewName("sign-in.ftl");
    }

    /**
     * Test that addPlayer switch case action handled taken name inputted by a user.
     */
    @Test
    public void takenNameSignIn() {
        when(request.queryParams(PLAYER_NAME_ATTR)).thenReturn(validName);

        try {
            CuT.handle(request, response);
        }
        catch(Exception e) {
            fail();
        }

        when(request.queryParams(PLAYER_NAME_ATTR)).thenReturn(takenName);

        try {
            CuT.handle(request, response);
        }
        catch(Exception e) {
            fail();
        }

        assertNull(playerLobby.getPlayer(takenName));

        tester.assertViewModelExists();
        tester.assertViewModelIsaMap();

        tester.assertViewModelAttribute("title", GetSignInRoute.TITLE);

        tester.assertViewName("sign-in.ftl");
    }
}