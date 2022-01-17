package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Move;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import spark.*;
/**
 * This Route is utilized when a move is requested to be validated.
 */
public class PostValidateMoveRoute implements Route{
    /**
     * The private Logger object that is utilized when the server creates a console log of an action or invocation of a Route.
     */
    private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());

    /**
     * The private GameManager object that manages the ongoing games on the WebServer.
     */
    private GameManager gameManager;

    /**
     * This constructor for the PostValidateMoveRoute. This method sets the route's TemplateEngine and GameManager, as
     * well as logging its invocation to the server console.
     * @param templateEngine the HTML template rendering engine
     * @param gameManager the GameManager object for this route to use
     */
    public PostValidateMoveRoute(final GameManager gameManager) {
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager is required");
        LOG.config("PostValidateMoveRoute is initialized.");
    }

    /**
     * This method handles a request from a client to validate a move.
     * @param request the http request
     * @param response the http response
     * @return the String JSON representation of the validity message.
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        //get the player object
        final Player currentPlayer = httpSession.attribute(GetHomeRoute.PLAYER_SESSION_KEY);
        CheckersGame game = gameManager.getGame(currentPlayer);

        LOG.finer("PostValidateMoveRoute is invoked.");
        
        if(game != null) {
            Gson gson = new Gson();
            Move actionData = gson.fromJson(request.queryParams("actionData"), Move.class);
            LOG.fine(actionData.toString());

            Message validMessage;
            String result = game.validateMove(actionData);
            if(result == null){
                validMessage = Message.info("Valid Move!");
            }
            else{
                validMessage = Message.error(result);
            }
            return gson.toJson(validMessage);
        }

        response.redirect(WebServer.HOME_URL);
        return null;
    }

}
