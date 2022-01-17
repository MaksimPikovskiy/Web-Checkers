package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.AIPlayer;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import java.util.logging.Logger;

import spark.*;
import com.google.gson.Gson;

/**
 * This class is enacted after the user or player submits their turn.
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 */
public class PostSubmitTurnRoute implements Route {
    /**
     * The private Logger object that is utilized when the server creates a console log of an action or invocation of a Route.
     */
    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());
    /**
     * GameManager object used in the route
     */
    private GameManager gameManager;

    /**
     *The constructor for the route, utilizes a GameManager object.
     * @param gameManager GameManager object used for the constructor
     */
    public PostSubmitTurnRoute(final GameManager gameManager) {
        this.gameManager = gameManager;
        LOG.config("PostValidateMoveRoute is initialized.");
    }
    /**
     * This method handles the request from the user and the response given.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return rendered HTML template for the sign in page.
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        final Player currentPlayer = httpSession.attribute(GetHomeRoute.PLAYER_SESSION_KEY);
        CheckersGame game = gameManager.getGame(currentPlayer);
        LOG.finer("PostValidateMoveRoute is invoked.");
        Gson gson = new Gson();


        if(game != null) {
            Message validMessage;
            String result = game.submit();
            if(result == null){
                validMessage = Message.info("Turn Submitted!");
            }
            else{
                validMessage = Message.error(result);
            }
            if(game.getWhitePlayer() instanceof AIPlayer) {
                gameManager.makeAIMove(currentPlayer);

            }
            return gson.toJson(validMessage);
        }


        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
