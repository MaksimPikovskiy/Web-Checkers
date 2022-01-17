package com.webcheckers.ui.spectator;

import com.webcheckers.ui.WebServer;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.ui.GetGameRoute;
import com.webcheckers.ui.GetGameRoute.*;
import com.webcheckers.ui.GetHomeRoute;
import com.webcheckers.util.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.*;

/**
 * UI controller for GET spectate game route. Responsible for rendering the board for the spectator requesting it
 * 
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 */
public class GetSpectateGameRoute implements Route {
    /**
     * The private Logger object that is utilized when the server creates a console log of an action or invocation of a Route.
     */
    private static final Logger LOG = Logger.getLogger(GetSpectateGameRoute.class.getName());

    /**
     * the public String object that is utilized when the spectator attempts to spectate a game that has already ended,
     * and thus cannot be spectated.
     */
    public static final String INVALID_GAME_MSG = "Unable to spectate! The game has already ended.";

    //
    // Attributes
    //
    /**
     * The private TemplateEngine object that manages client-side graphics.
     */
    private final TemplateEngine templateEngine;

    /**
     * The private GameManager object that manages the ongoing games on the WebServer.
     */
    private final GameManager gameManager;

    /**
     * The constructor for the {@code GET /game} route handler.
     *
     * @param templateEngine The {@link TemplateEngine} for the application
     * @param gameManager The {@link GameManager} for the application
     * @throws NullPointerException when the {@code templateEngine} or {@code gameManager} parameter is null
     */
    public GetSpectateGameRoute(final TemplateEngine templateEngine, GameManager gameManager){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager is required");
    }

    /**
     * Invoked when a GET request is made to '/game'
     *
     * @param request  The request object providing information about the HTTP request
     * @param response The response object providing functionality for modifying the response
     * @return The HTML content of the game or null if redirected back to home page
     * @throws Exception implementation can choose to throw exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("GetSpectateGame is invoked.");
        final Session httpSession = request.session();
        //get the player object
        final Player currentPlayer = httpSession.attribute(GetHomeRoute.PLAYER_SESSION_KEY);

        if(currentPlayer != null) {
            CheckersGame game = null;
            try {
                int gameID = Integer.parseInt(request.queryParams("gameID"));
                game = gameManager.getGame(currentPlayer, gameID); // need duplicate method of getGame with gameID string param
            } catch (NumberFormatException e) {
                //keep game = null
            }
            if(game != null){  // check the player is actually in a game
                //setup vm for viewing the game board
                Map<String, Object> vm = new HashMap<>();
                vm.put(GetGameRoute.ATTR_TITLE, GetGameRoute.TITLE);
                vm.put(GetGameRoute.ATTR_CURRENT_USER, currentPlayer);
                vm.put(GetGameRoute.ATTR_VIEW_MODE, ViewMode.SPECTATOR);

                String hasGameEnded = game.hasGameEnded();
                if (hasGameEnded != null) {
                    vm.put(GetGameRoute.ATTR_MESSAGE, Message.info(hasGameEnded));
                }

                vm.put(GetGameRoute.ATTR_RED_PLAYER, game.getRedPlayer());
                vm.put(GetGameRoute.ATTR_WHITE_PLAYER, game.getWhitePlayer());
                vm.put(GetGameRoute.ATTR_ACTIVE_COLOR, game.currentTurn());

                vm.put(GetGameRoute.ATTR_BOARD, currentPlayer.getBoardView());

                return templateEngine.render(new ModelAndView(vm , GetGameRoute.GAME_VIEW));
            } else { // gameID invalid
                //redirect user to homepage with invalid gameID message
                response.redirect(WebServer.HOME_URL + "?message=" + INVALID_GAME_MSG);
                return null;
            }
        }

        //redirect users to homepage if they are not signed in or not in a game
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
