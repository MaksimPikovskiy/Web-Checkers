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
 * This class handles the back up move route.
 * There is a constructor as well as a handle method for the request and response.
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
public class PostBackUpMoveRoute implements Route {
    /**
     * Logger object used for the PostBackUpMoveRoute class
     */
    private static final Logger LOG = Logger.getLogger(PostBackUpMoveRoute.class.getName());
    /**
     * GameManager object used in the post back up class route
     */
    private GameManager gameManager;

    /**
     * This method is the constructor for the PostBackUpMoveRoute
     *
     * @return none as it is a constructor
     */
    public PostBackUpMoveRoute(final GameManager gameManager) {
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager is required");
        LOG.config("PostBackUpMoveRoute is initialized.");
    }

    /**
     * This method handles any request made by the user. The method will
     * take a request and handle the response object as well.
     *
     * @param request Request object utilized within the handle method
     * @param response Response object that is utilized within the handle method
     *
     * @return gson.toJson if the game is not null and null if the game is null
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        final Player currentPlayer = httpSession.attribute(GetHomeRoute.PLAYER_SESSION_KEY);
        CheckersGame game = gameManager.getGame(currentPlayer);
        
        LOG.finer("PostBackUpMoveRoute is invoked.");
        Gson gson = new Gson();

        if(game != null) {
            Message validMessage;
            String result = game.undoMove();
            if(result != null){
                validMessage = Message.info(result);
            }
            else{
                validMessage = Message.error("No more moves to undo");
            }
            return gson.toJson(validMessage);
        }

        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
