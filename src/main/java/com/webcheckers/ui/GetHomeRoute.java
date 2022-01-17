package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.*;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 */
public class GetHomeRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    // Messages for User Interface
    public static final Message WELCOME_MSG = Message.info("Welcome to the World of Online Checkers.");

    // Constant attributes for view model
    /**
     * String object for the title attribute within the ftl file
     */
    public static final String ATTR_TITLE = "title";
    /**
     * the message attribute used in the ftl file
     */
    public static final String ATTR_MESSAGE = "message";
    /**
     * String attribute for the playerSignIn display on the Home route ftl
     */
    public static final String ATTR_PLAYER_SIGNIN = "playerSignIn";
    /**
     * String attribute for the number of current players online used in the home ftl file
     */
    public static final String ATTR_NUM_PLAYERS = "numberPlayersOnline";
    /**
     * String attribute for the currentUser used in the ftl
     */
    public static final String ATTR_CURRENT_USER = "currentUser";
    /**
     * String attribute for the playerList used in the ftl
     */
    public static final String ATTR_PLAYER_LIST = "playerList";
    /**
     * String attribute gameList used in the home ftl
     */
    public static final String ATTR_GAME_LIST = "gameList";
    /**
     * String attribute used only if one player exists for a special message
     */
    public static final String ATTR_ONE_PLAYER = "onlyOnePlayer";

    // session object keys
    /**
     * String attribute for the player session key used in the ftl
     */
    public static final String PLAYER_SESSION_KEY = "Player";
    /**
     * String attribute used for the watchdog timeout
     */
    public static final String TIMEOUT_SESSION_KEY = "timeoutWatchdog";

    // view file
    /**
     * The String that represents the home.ftl file name
     */
    public static final String VIEW_NAME = "home.ftl";

    // Constant Strings
    /**
     * Title String for the title of the ftl
     */
    public static final String TITLE = "Welcome!";

    //
    // Attributes
    //
    /**
     * TemplateEngine object used in the ftl file
     */
    private final TemplateEngine templateEngine;
    /**
     * PlayerLobby used for the home ftl
     */
    private final PlayerLobby playerLobby;
    /**
     * GameManager object used in the home route class
     */
    private final GameManager gameManager;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP
     * requests.
     *
     * @param templateEngine the HTML template rendering engine
     * @param playerLobby    {@link PlayerLobby} that contains all players
     *
     * @throws NullPointerException when the {@code playerLobby} or
     *                              {@code templateEngine} parameter is null
     */
    public GetHomeRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby, final GameManager gameManager) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required");
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager is required");

        LOG.config("GetHomeRoute is initialized.");
    }

    /**
     * Render the WebCheckers Home page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     *
     * @return the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetHomeRoute is invoked.");
        final Session httpSession = request.session();
        final Player currentPlayer = httpSession.attribute(PLAYER_SESSION_KEY);

        String message = request.queryParams("message");

        Map<String, Object> vm = new HashMap<>();
        vm.put(ATTR_TITLE, TITLE);
        if (message != null) {
            vm.put(ATTR_MESSAGE, Message.error(message));  // show param error message
        } else {
            vm.put(ATTR_MESSAGE, WELCOME_MSG);  //show default welcome message
        }
        vm.put(ATTR_PLAYER_SIGNIN, false);  //player sign in condition is inherently false
        vm.put(ATTR_NUM_PLAYERS, this.playerLobby.getPlayerCount()); // tells user how many players online
        vm.put(ATTR_ONE_PLAYER, this.playerLobby.getPlayerCount() == 1); //test if there is only 1 player online

        if (currentPlayer != null) {
            if(currentPlayer.getGameOver()){
                gameManager.quit(currentPlayer);
            }else if(currentPlayer.isInGame()){
                response.redirect(WebServer.GAME_URL);
                return null;
            }

            vm.put(ATTR_CURRENT_USER, currentPlayer);
            vm.put(ATTR_PLAYER_SIGNIN, true);

            // if multiple players exist then list each
            if (this.playerLobby.getPlayerCount() > 1) {
                // put player list in ftl file
                vm.put(ATTR_PLAYER_LIST, playerLobby.getCurrentPlayers());
            }
            vm.put(ATTR_GAME_LIST, gameManager.getActiveGames());
        }
        
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
