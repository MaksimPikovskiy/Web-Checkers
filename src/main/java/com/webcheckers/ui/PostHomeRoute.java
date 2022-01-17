package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.*;

/**
 * UI controller for POST home route. Responsible for sending the player to a new game when 
 * they challenge the selected player or display an error message on the home page
 * 
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
public class PostHomeRoute implements Route{
    /**
     * Logger object used for the PostHomeRoute class
     */
    private static final Logger LOG = Logger.getLogger(PostHomeRoute.class.getName());
    /**
     * String object used for the ftl home file
     */
    private static final String START_ERROR = "%s is already in a game! Please challenge someone else.";
    /**
     * String spectator error used in the ftl home file
     */
    private static final String SPECTATE_ERROR = "Unable to spectate game, The game has already ended";
    /**
     * String for spectator mode
     */
    private static final String SPECTATE_MODE = "spectate";
    /**
     * TemplateEngine object used for the PostHomeRoute object
     */
    private TemplateEngine templateEngine;
    /**
     * PlayerLobby object used within the PostHomeRoute class
     */
    private PlayerLobby playerLobby;
    /**
     * GameManager object used for this class
     */
    private GameManager gameManager;

    /**
     * The constructor for the {@code GET /game} route handler.
     * 
     * @param templateEngine The {@link TemplateEngine} for the application
     * @param playerLobby The {@link PlayerLobby} for the application
     * @param gameManager The {@link GameManager} for the application
     * @throws NullPointerException when the {@code templateEngine}, {@code playerLobby},
     *  or {@code gameManager} parameter is null
     */
    public PostHomeRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby,
                         final GameManager gameManager) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required");
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager is required");
        LOG.config("GetHomeRoute is initialized.");
    }
    /**
     * Invoked when a POST request is made to '/home'
     *
     * @param request  The request object providing information about the HTTP request
     * @param response The response object providing functionality for modifying the response
     * @return The content to be set in the response
     * @throws Exception implementation can choose to throw exception
     *
     * @return template engine.render new MV object if currentPlayer == null,
     * null otherwise.
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostHomeRoute is invoked.");
        final Session httpSession = request.session();
        final Player currentPlayer = httpSession.attribute(GetHomeRoute.PLAYER_SESSION_KEY);
        final String opponentName = request.queryParams("player");
        final String modeAction = request.queryParams("mode");
        
        //setup vm map for home page if challenge request is invalid or somehow not signed in
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.ATTR_TITLE, GetHomeRoute.TITLE);
        vm.put(GetHomeRoute.ATTR_MESSAGE, GetHomeRoute.WELCOME_MSG);  //show default welcome message
        vm.put(GetHomeRoute.ATTR_PLAYER_SIGNIN, false);  //player sign in condition is inherently false
        vm.put(GetHomeRoute.ATTR_NUM_PLAYERS, this.playerLobby.getPlayerCount()); // tells user how many players online
        vm.put(GetHomeRoute.ATTR_ONE_PLAYER, this.playerLobby.getPlayerCount() == 1); //test if there is only 1 player online

        //checks that the user requesting to challenge is signed in
        if(currentPlayer != null){
            LOG.fine(String.format("%s challenges %s",currentPlayer.getName(),opponentName));

            vm.put(GetHomeRoute.ATTR_CURRENT_USER, currentPlayer);
            vm.put(GetHomeRoute.ATTR_PLAYER_SIGNIN, true);
            vm.put(GetHomeRoute.ATTR_PLAYER_LIST, playerLobby.getCurrentPlayers());
            vm.put(GetHomeRoute.ATTR_GAME_LIST, gameManager.getActiveGames());

            if(SPECTATE_MODE.equals(modeAction)){
                final String gameID = request.queryParams("gameID");
                if(gameID !=null){
                    response.redirect(WebServer.SPECTATE_GAME_URL + "?gameID="+gameID);
                    return null;
                }else{
                    vm.put(GetHomeRoute.ATTR_MESSAGE, Message.error(SPECTATE_ERROR));
                }
            }else{
                //Try to make a new checkers game against an opponent
                CheckersGame game = gameManager.createGame(currentPlayer, opponentName);
                // send the user to the game if the opponent is not in a game
                if(game!=null){
                    response.redirect(WebServer.GAME_URL);
                    return null;
                }else{
                    //display an error message if the opponent is already in a game
                    vm.put(GetHomeRoute.ATTR_MESSAGE, Message.error(String.format(START_ERROR, opponentName)));
                }
            }
        }

        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
    }
}
