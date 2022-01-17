package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import java.util.Objects;
import java.util.logging.Logger;

import spark.*;
/**
 * The Sign-Out {@code POST /signout} route handler for the web app.
 * It will sign out currently player and remove them from the player lobby object.
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class PostSignOutRoute implements Route {
    /**
     * The private Logger object that is utilized when the server creates a console log of an action or invocation of a Route.
     */
    private static final Logger LOG = Logger.getLogger(PostSignOutRoute.class.getName());

    // Messages for User Interface
    /**
     * The Message object for a successful sign out.
     */
    public static final Message SIGNOUT_MSG = Message.info("You have signed out! Have a nice day :)");

    // Constants
    /**
     * The private PlayerLobby object that manages the players the online players.
     */
    private final PlayerLobby playerLobby;

    /**
     * The private GameManager object that manages the ongoing games on the WebServer.
     */
    private final GameManager gameManager;

    /**
     * The constructor for the {@code POST /signin} route handler.
     *
     * @param playerLobby {@link PlayerLobby} that contains all players
     * @param templateEngine the HTML template rendering engine
     *
     * @throws NullPointerException when the {@code playerLobby} or {@code templateEngine} parameter is null
     */
    PostSignOutRoute(PlayerLobby playerLobby, final GameManager gameManager) {
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager must not be null");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");

        LOG.config("PostSignOutRoute is initialized.");
    }

    /**
     * Render the WebCheckers Sign-Out page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Sign-Out page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostSignOutRoute is invoked.");

        final Session session = request.session();

        Player player = session.attribute(GetHomeRoute.PLAYER_SESSION_KEY);
        if(player != null){
            gameManager.resign(player);
            playerLobby.removePlayer(player.getName());
            LOG.fine("User, " + player.getName() + ", signed out.");
        }

        session.removeAttribute(GetHomeRoute.PLAYER_SESSION_KEY);

        response.redirect(WebServer.HOME_URL);
        return null;

    }
}

