package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.ui.spectator.*;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import spark.TemplateEngine;
import static spark.Spark.*;

/**
 * The server that initializes the set of HTTP request handlers.
 * This defines the <em>web application interface</em> for this
 * WebCheckers application.
 *
 * <p>
 * There are multiple ways in which you can have the client issue a
 * request and the application generate responses to requests. If your team is
 * not careful when designing your approach, you can quickly create a mess
 * where no one can remember how a particular request is issued or the response
 * gets generated. Aim for consistency in your approach for similar
 * activities or requests.
 * </p>
 *
 * <p>Design choices for how the client makes a request include:
 * <ul>
 *     <li>Request URL</li>
 *     <li>HTTP verb for request (GET, POST, PUT, DELETE and so on)</li>
 *     <li><em>Optional:</em> Inclusion of request parameters</li>
 * </ul>
 * </p>
 *
 * <p>Design choices for generating a response to a request include:
 * <ul>
 *     <li>View templates with conditional elements</li>
 *     <li>Use different view templates based on results of executing the client request</li>
 *     <li>Redirecting to a different application URL</li>
 * </ul>
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class WebServer {
  /**
   * The private Logger object that is utilized when the server creates a console log of an action or invocation of a Route.
   */
  private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

  //
  // Constants
  //

  /**
   * The public String URL pattern utilized when requesting the Home page.
   */
  public static final String HOME_URL = "/";

  /**
   * The public String URL pattern utilized when a user signs in.
   */
  public static final String SIGNIN_URL = "/signin";

  /**
   * The public String URL pattern utilized when a user signs a user out.
   */
  public static final String SIGNOUT_URL = "/signout";

  /**
   * The public String URL pattern utilized when the game page is requested.
   */
  public static final String GAME_URL = "/game";

  /**
   * The public String URL pattern utilized when a move validation is requested.
   */
  public static final String VALIDATE_MOVE_URL = "/validateMove";

  /**
   * The public String URL pattern utilized when a valid turn is submitted.
   */
  public static final String SUBMIT_TURN_URL  = "/submitTurn";

  /**
   * The public String URL pattern utilized when a user requests to back up an un-submitted move.
   */
  public static final String BACKUP_MOVE_URL = "/backupMove";

  /**
   * The public String URL pattern utilized when a user requests to resign their game.
   */
  public static final String RESIGN_GAME_URL  = "/resignGame";

  /**
   * The public String URL pattern utilized when a player checks whose turn it is in the game they are playing.
   */
  public static final String CHECK_TURN_URL  = "/checkTurn";

  /**
   * The public String URL pattern utilized when a spectator spectates a game.
   */
  public static final String SPECTATE_GAME_URL = "/spectator/game";

  /**
   * The public String URL pattern utilized when a spectator requests to stop watching a game.
   */
  public static final String SPECTATE_STOP_WATCHING_URL = "/spectator/stopWatching";

  /**
   * The public String URL pattern utilized when a spectator checks whose turn it is in the game they are spectating.
   */
  public static final String SPECTATE_CHECK_TURN_URL = "/spectator/checkTurn";

  //
  // Attributes
  //

  /**
   * The private TemplateEngine object that manages client-side graphics.
   */
  private final TemplateEngine templateEngine;

  /**
   * The private PlayerLobby object that manages the players online on the WebServer.
   */
  private final PlayerLobby playerLobby;

  /**
   * The Gson object that converts Java objects to their JSON representation.
   */
  private final Gson gson;

  /**
   * The private GameManager object that manages the ongoing games on the WebServer.
   */
  private final GameManager gameManager;

  //
  // Constructor
  //

  /**
   * The constructor for the Web Server.
   *
   * @param templateEngine
   *    The default {@link TemplateEngine} to render page-level HTML views.
   * @param gson
   *    The Google JSON parser object used to render Ajax responses.
   *
   * @throws NullPointerException
   *    If any of the parameters are {@code null}.
   */
  public WebServer(final TemplateEngine templateEngine, final Gson gson, final PlayerLobby playerLobby,
                   final GameManager gameManager) {
    // validation
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    Objects.requireNonNull(gson, "gson must not be null");
    Objects.requireNonNull(playerLobby, "playerLobby must not be null");
    Objects.requireNonNull(gameManager, "gameManager must not be null");
    //
    this.templateEngine = templateEngine;
    this.gson = gson;
    this.playerLobby = playerLobby;
    this.gameManager = gameManager;
  }

  //
  // Public methods
  //

  /**
   * Initialize all of the HTTP routes that make up this web application.
   *
   * <p>
   * Initialization of the web server includes defining the location for static
   * files, and defining all routes for processing client requests. The method
   * returns after the web server finishes its initialization.
   * </p>
   */
  public void initialize() {

    // Configuration to serve static files
    staticFileLocation("/public");

    //// Setting any route (or filter) in Spark triggers initialization of the
    //// embedded Jetty web server.

    //// A route is set for a request verb by specifying the path for the
    //// request, and the function callback (request, response) -> {} to
    //// process the request. The order that the routes are defined is
    //// important. The first route (request-path combination) that matches
    //// is the one which is invoked. Additional documentation is at
    //// http://sparkjava.com/documentation.html and in Spark tutorials.

    //// Each route (processing function) will check if the request is valid
    //// from the client that made the request. If it is valid, the route
    //// will extract the relevant data from the request and pass it to the
    //// application object delegated with executing the request. When the
    //// delegate completes execution of the request, the route will create
    //// the parameter map that the response template needs. The data will
    //// either be in the value the delegate returns to the route after
    //// executing the request, or the route will query other application
    //// objects for the data needed.

    //// FreeMarker defines the HTML response using templates. Additional
    //// documentation is at
    //// http://freemarker.org/docs/dgui_quickstart_template.html.
    //// The Spark FreeMarkerEngine lets you pass variable values to the
    //// template via a map. Additional information is in online
    //// tutorials such as
    //// http://benjamindparrish.azurewebsites.net/adding-freemarker-to-java-spark/.

    //// These route definitions are examples. You will define the routes
    //// that are appropriate for the HTTP client interface that you define.
    //// Create separate Route classes to handle each route; this keeps your
    //// code clean; using small classes.

    // Shows the Checkers game Home page.
    get(HOME_URL, new GetHomeRoute(templateEngine, playerLobby, gameManager));
    post(HOME_URL, new PostHomeRoute(templateEngine, playerLobby, gameManager));

    // Shows the Checkers game Signin page.
    get(SIGNIN_URL, new GetSignInRoute(templateEngine));
    post(SIGNIN_URL, new PostSignInRoute(playerLobby, templateEngine));
    
    // Shows the Checkers game signout page.
    post(SIGNOUT_URL, new PostSignOutRoute(playerLobby, gameManager));

    // Shows the Checkers game page.
    get(GAME_URL, new GetGameRoute(templateEngine, gameManager));

    //post validate move
    post(VALIDATE_MOVE_URL, new PostValidateMoveRoute(gameManager));

    //post submit valid turn
    post(SUBMIT_TURN_URL, new PostSubmitTurnRoute(gameManager));

    //post backup move
    post(BACKUP_MOVE_URL, new PostBackUpMoveRoute(gameManager));

    //post resign from a game
    post(RESIGN_GAME_URL, new PostResignGameRoute(gameManager));

    //post check turn route
    post(CHECK_TURN_URL, new PostCheckTurnRoute(gameManager));

    //get spectate game route
    get(SPECTATE_GAME_URL, new GetSpectateGameRoute(templateEngine, gameManager));

    //get spectate stop watching route
    get(SPECTATE_STOP_WATCHING_URL, new GetSpectateStopWatchingRoute(gameManager));

    //post spectate check turn route
    post(SPECTATE_CHECK_TURN_URL, new PostSpectateCheckTurnRoute(gameManager));

    //post(SIGNOUT_URL...
    LOG.config("WebServer is initialized.");
  }

}