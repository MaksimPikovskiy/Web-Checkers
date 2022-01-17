package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import spark.*;

/**
 * UI controller for GET game route. Responsible for rendering the board for the player requesting it
 * 
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
public class GetGameRoute implements Route {
    /**
     * Logger object that utilizes getLogger with the GetGameRoute class
     */
    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

    /**
     * The possible View modes of the checkers board
     */
    public enum ViewMode {
        PLAY, 
        SPECTATOR, 
        REPLAY
    }

    /**
     * String object for the title attribute within the ftl file
     */
    public static final String ATTR_TITLE = "title";
    /**
     * String object for the currentUser attribute within the ftl file
     */
    public static final String ATTR_CURRENT_USER = "currentUser";
    /**
     * String object for the viewMode attribute within the ftl file
     */
    public static final String ATTR_VIEW_MODE = "viewMode";

    public static final String ATTR_MODE_OPTIONS = "modeOptionsAsJSON";
    /**
     * String object for the red player attribute in the ftl file
     */
    public static final String ATTR_RED_PLAYER = "redPlayer";
    /**
     * String attribute used for the white player attribute in the ftl file
     */
    public static final String ATTR_WHITE_PLAYER = "whitePlayer";
    /**
     * active color attribute utilized within the ftl file for the game
     */
    public static final String ATTR_ACTIVE_COLOR = "activeColor";
    /**
     * String attribute for the board used within the ftl file
     */
    public static final String ATTR_BOARD = "board";
    /**
     * the message attribute used in the ftl file
     */
    public static final String ATTR_MESSAGE = "message";

    //tile of the game page
    /**
     * The string for the title attribute fo the ftl file
     */
    public static final String TITLE = "Game View";

    //UI view model file name
    /**
     * String for the ftl file name.
     */
    public static final String GAME_VIEW = "game.ftl";

    //
    // Attributes
    //
    /**
     * TemplateEngine object for the route class
     */
    private TemplateEngine templateEngine;
    /**
     * GameManager object for this route class
     */
    private GameManager gameManager;

    /**
     * The constructor for the {@code GET /game} route handler.
     * 
     * @param templateEngine The {@link TemplateEngine} for the application
     * @param gameManager The {@link GameManager} for the application
     * @throws NullPointerException when the {@code templateEngine} or {@code gameManager} parameter is null
     */
    GetGameRoute(final TemplateEngine templateEngine,GameManager gameManager){
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
        LOG.finer("GetGameRoute is invoked.");
        final Session httpSession = request.session();
        //get the player object
        final Player currentPlayer = httpSession.attribute(GetHomeRoute.PLAYER_SESSION_KEY);


        if(currentPlayer != null){
            CheckersGame game = gameManager.getGame(currentPlayer);
            if(game != null){  // check the player is actually in a game
                //setup vm for viewing the game board
                Map<String, Object> vm = new HashMap<>();
                vm.put(ATTR_TITLE, TITLE);
                vm.put(ATTR_CURRENT_USER, currentPlayer);
                vm.put(ATTR_VIEW_MODE, ViewMode.PLAY);


                final Map<String, Object> modeOptions = new HashMap<>(2);
                Gson gson = new Gson();
                String gameEnded = game.hasGameEnded();
                if(gameEnded!= null) {
                    modeOptions.put("isGameOver", true);
                    modeOptions.put("gameOverMessage", gameEnded);
                    vm.put(ATTR_MODE_OPTIONS, gson.toJson(modeOptions));
                }

                vm.put(ATTR_RED_PLAYER, game.getRedPlayer());
                vm.put(ATTR_WHITE_PLAYER, game.getWhitePlayer());
                vm.put(ATTR_ACTIVE_COLOR, game.currentTurn());

                vm.put(ATTR_BOARD, currentPlayer.getBoardView());

                return templateEngine.render(new ModelAndView(vm , GAME_VIEW));
            }
        }

        //redirect users to homepage if they are not signed in or not in a game
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
