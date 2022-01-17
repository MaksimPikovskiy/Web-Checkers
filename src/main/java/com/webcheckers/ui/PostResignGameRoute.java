package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
/**
 * This Route POSTs the webpage when a player attempts to resign.
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
public class PostResignGameRoute implements Route {
    /**
     * The private Logger object that is utilized when the server creates a console log of an action or invocation of a Route.
     */
    private static final Logger LOG = Logger.getLogger(PostResignGameRoute.class.getName());

    /**
     * The private GameManager object that manages the ongoing games on the WebServer.
     */
    private GameManager gameManager;

    /**
     * The constructor for PostResignGameRoute. Assigns the route's GameManager attribute. Logs initialization in the
     * server console.
     * @param gameManager the GameManager object
     */
    public PostResignGameRoute(final GameManager gameManager) {
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager is required");
        LOG.config("PostResignGameRoute is initialized.");
    }

    /**
     * Handle the request for the player to request to sign out.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the JSON representation of the resignation message.
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        final Player currentPlayer = httpSession.attribute(GetHomeRoute.PLAYER_SESSION_KEY);
        CheckersGame game = gameManager.getGame(currentPlayer);
        
        LOG.finer("PostResignGameRoute is invoked.");
        Gson gson = new Gson();
        if(game != null) {
            Message validMessage;
            if(gameManager.resign(currentPlayer)){
                validMessage = Message.info("resigned");
            } else{
                validMessage = Message.error("Unable to resign, other player has already resigned");
            }
            return gson.toJson(validMessage);
        }

        response.redirect(WebServer.HOME_URL);
        return null;
    }
}