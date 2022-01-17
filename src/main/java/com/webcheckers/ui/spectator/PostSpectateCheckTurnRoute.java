package com.webcheckers.ui.spectator;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.ui.GetHomeRoute;
import com.webcheckers.ui.WebServer;
import com.webcheckers.util.Message;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import spark.*;

/**
 * This route is invoked when the spectator checks whose turn it is in the game they are spectating.
 *
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 */
public class PostSpectateCheckTurnRoute implements Route {
    /**
     * The private Logger object that is utilized when the server creates a console log of an action or invocation of a Route.
     */
    private static final Logger LOG = Logger.getLogger(PostSpectateCheckTurnRoute.class.getName());

    /**
     * The private GameManager object that manages the ongoing games on the WebServer.
     */
    private GameManager gameManager;

    /**
     * This public String object is a key in the httpSession connected to the move count of the game. This value is
     * how it is determined if a player has made a turn.
     */
    public static final String MOVE_COUNT_KEY = "moveCount";

    /**
     * Construct the PostSpectateCheckTurnRoute with a reference to the gameManager.
     */
    public PostSpectateCheckTurnRoute(final GameManager gameManager) {
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager is required");
        LOG.config("PostValidateMoveRoute is initialized.");
    }

    /**
     * Handle the request to check whose turn it is.
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        final Player currentPlayer = httpSession.attribute(GetHomeRoute.PLAYER_SESSION_KEY);
        String lastMoveCount = httpSession.attribute(MOVE_COUNT_KEY);    // This value is stored as a String so that it
                                                                        // can contain either a valid number or
                                                                        // null, which indicates that the spectator's
                                                                        // screen has never been refreshed.
        LOG.fine("PostSpectateCheckTurnRoute is invoked.");
        Gson gson = new Gson();

        if (currentPlayer != null) {
            CheckersGame game = gameManager.getGame(currentPlayer);
            if(game != null) {
                String currentMoveCount = String.valueOf(game.getMoveCount());
                Message validMessage = Message.info("false");
                LOG.fine(String.format("Spectator %s is checking turn. Current turn is %s", currentPlayer, currentMoveCount));

                // If the turn has incremented, or this is the first turn the spectator is seeing, set the LAST_TURN session
                // attribute and return "true"
                if (lastMoveCount == null || !lastMoveCount.equals(currentMoveCount)) {
                    httpSession.attribute(MOVE_COUNT_KEY, currentMoveCount);
                    validMessage = Message.info("true");
                }

                return gson.toJson(validMessage);
            }

        }
        response.redirect(WebServer.HOME_URL);
        return null;

    }
}
