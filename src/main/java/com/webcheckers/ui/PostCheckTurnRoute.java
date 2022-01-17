package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import spark.*;

/**
 * This method handles the checking of a turn by a player.
 * This route will handle the request and response for the checking of a given turn
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
public class PostCheckTurnRoute implements Route {
    /**
     * Logger object used for the getLogger method in the PostCheckTurnRoute class
     */
    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());

    /**
     * GameManager object used within the PostCheckTurnRoute
     */
    private GameManager gameManager;

    /**
     * The constructor method for the PostCheckTurnRoute class
     *
     * @param gameManager GameManager object used for the construction of the
     * PostCheckTurnRoute object
     */
    public PostCheckTurnRoute(final GameManager gameManager) {
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager is required");
        LOG.config("PostValidateMoveRoute is initialized.");
    }

    /**
     * This handle method takes a request made by user and handles the response
     *
     * @param request Request object made by user to be handled by this method
     * @param response Response object utilized within the handle method
     *
     * @return gson.toJson object if the current player and game are not null, null otherwise
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        final Player currentPlayer = httpSession.attribute(GetHomeRoute.PLAYER_SESSION_KEY);
        LOG.fine("PostCheckTurnRoute is invoked.");
        Gson gson = new Gson();

        if (currentPlayer != null) {
            CheckersGame game = gameManager.getGame(currentPlayer);
            if(game != null) {
                LOG.fine(String.format("Player %s is checking turn. Current color %s", currentPlayer, game.currentTurn()));
                Message validMessage = Message.info("false");
                if(game.hasGameEnded() != null) {
                    validMessage = Message.info("true");
                } else if (game.getRedPlayer().equals(currentPlayer)
                        && game.currentTurn() == Piece.Color.RED) {
                    validMessage = Message.info("true");
                } else if (game.getWhitePlayer().equals(currentPlayer)
                        && game.currentTurn() == Piece.Color.WHITE) {
                    validMessage = Message.info("true");
                }

                return gson.toJson(validMessage);
            }
        }
        response.redirect(WebServer.HOME_URL);
        return null;

    }
}
