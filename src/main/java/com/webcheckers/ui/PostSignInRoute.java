package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;


/**
 * The {@code POST /signin} route handler.
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class PostSignInRoute implements Route {
    /**
     * The private Logger object that is utilized when the server creates a console log of an action or invocation of a Route.
     */
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    // Constants
    /**
     * The String title of the .ftl to use when the sign in is valid.
     */
    static final String VALID_ROUTE = "home.ftl";

    /**
     * The String title of the .ftl to use when the sign in is invalid.
     */
    static final String INVALID_ROUTE = "sign-in.ftl";

    /**
     * The String title of the key for the player's name in the Session
     */
    static final String PLAYER_NAME_ATTR = "uname";

    // Attributes
    /**
     * The private TemplateEngine object that manages client-side graphics.
     */
    private final TemplateEngine templateEngine;

    /**
     * The private PlayerLobby object that manages the players the online players.
     */
    private final PlayerLobby playerLobby;

    /**
     * The constructor for the {@code POST /signin} route handler.
     *
     * @param playerLobby    {@link PlayerLobby} that contains all players
     * @param templateEngine template engine to use for rendering HTML page
     *
     * @throws NullPointerException when the {@code playerLobby} or {@code templateEngine} parameter is null
     */
    PostSignInRoute(PlayerLobby playerLobby, TemplateEngine templateEngine) {
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;

        LOG.config("PostSignInRoute is initialized.");
    }

    /**
     * Handle the request for a user to sign in.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return rendered HTML template for the sign in page.
     * @throws NoSuchElementException when a super invalid name is entered.
     */
    @Override
    public String handle(Request request, Response response) {
        LOG.finer("PostSignInRoute is invoked");

        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();
        boolean isValid = false;

        vm.put("title", GetSignInRoute.TITLE);

        // retrieve the game object
        final Session session = request.session();

        // retrieve request parameter
        String uName = request.queryParams(PLAYER_NAME_ATTR);
        uName = uName.trim().replaceAll(" +", " ");

        ModelAndView mv;
        switch (playerLobby.addPlayer(uName)) {
            case INVALID:
                mv = error(vm, PlayerLobby.NAME_INVALID_MSG);
                break;

            case TAKEN:
                mv = error(vm, PlayerLobby.NAME_TAKEN_MSG);
                break;

            case VALID:
                mv = valid(vm);
                session.attribute(GetHomeRoute.PLAYER_SESSION_KEY, playerLobby.getPlayer(uName));
                isValid = true;
                LOG.fine("Valid username entered; user, " + uName + ", signed in.");
                break;

            default:
                throw new NoSuchElementException("ERROR: Invalid name. HOW?");
        }

        if(isValid){
            response.redirect(WebServer.HOME_URL);
        }

        vm.put(GetHomeRoute.ATTR_PLAYER_SIGNIN, false);
        vm.put("onlyOnePlayer", true);
        return templateEngine.render(mv);
    }

    //
    // Private methods
    //

    /**
     * Create a ModelAndView object for an erroneous sign-in.
     * @param vm the view model.
     * @param message the message related to the sign in.
     * @return a ModelAndView object.
     */
    private ModelAndView error(final Map<String, Object> vm, final String message) {
        Message msg = Message.info(message);
        vm.put("numberPlayersOnline",Integer.toString(playerLobby.getPlayerCount()));
        vm.put("message", msg);
        return new ModelAndView(vm, INVALID_ROUTE);
}

    /**
     * Create a ModelAndView object for a valid login.
     * @param vm the view model.
     * @return a ModelAndView object.
     */
    private ModelAndView valid(final Map<String, Object> vm){
        vm.put("numberPlayersOnline",Integer.toString(playerLobby.getPlayerCount()));
        Message msg = Message.info("Valid Name");
        vm.put("message", msg);
        return new ModelAndView(vm, VALID_ROUTE);
    }
}