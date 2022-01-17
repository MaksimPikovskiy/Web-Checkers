package com.webcheckers.ui.spectator;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Player;
import com.webcheckers.ui.GetHomeRoute;
import com.webcheckers.ui.WebServer;

import java.util.Objects;
import java.util.logging.Logger;

import spark.Request;
import spark.Route;
import spark.Session;
import spark.Response;

/**
 * This route is invoked when the spectator wants to stop watching a game, and needs to be removed from the game.
 * The session must also be cleaned up.
 *
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 */
public class GetSpectateStopWatchingRoute implements Route {
    /**
     * The private Logger object that is utilized when the server creates a console log of an action or invocation of a Route.
     */
    private static final Logger LOG = Logger.getLogger(GetSpectateStopWatchingRoute.class.getName());

    /**
     * The private GameManager object that manages the ongoing games on the WebServer.
     */
    private GameManager gameManager;

    /**
     * Construct the GetSpectateStopWatchingRoute, with a reference to the gameManager.
     * @param gameManager the gameManager.
     */
    public GetSpectateStopWatchingRoute(GameManager gameManager) {
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager is required");
    }

    /**
     * Handle the request for the spectator to stop watching a game. Remove them from the CheckersGame instance, and
     * clean up the backend.
     * @param request the http request
     * @param response the http response
     * @return null
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        final Player currentPlayer = httpSession.attribute(GetHomeRoute.PLAYER_SESSION_KEY);
        LOG.fine("GetSpectateStopWatchingRoute is invoked.");

        // Remove LAST_TURN session attribute.
        httpSession.removeAttribute(PostSpectateCheckTurnRoute.MOVE_COUNT_KEY);

        // Remove the spectator from the game they are watching.
        if (currentPlayer != null) {
            gameManager.quit(currentPlayer);
        }

        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
