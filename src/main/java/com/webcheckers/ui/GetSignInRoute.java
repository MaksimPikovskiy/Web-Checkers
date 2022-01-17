package com.webcheckers.ui;

import com.webcheckers.model.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.*;

/**
 *The {@code GET /signin} route handler, aka sign-in page :)
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class GetSignInRoute implements Route {
    /**
     * The private Logger object that is utilized when the server creates a console log of an action or invocation of a Route.
     */
    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    /**
     * The string for the title attribute fo the ftl file
     */
    public static final String TITLE  = "Sign-In";

    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /signin} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     *
     * @throws NullPointerException when the {@code playerLobby} or {@code templateEngine} parameter is null
     */
    GetSignInRoute(final TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;

        LOG.config("getSignInRoute is initialized.");
    }

    /**
     * Render the WebCheckers Sign In page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Sign In page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSignInRoute is invoked.");

        final Session httpSession = request.session();
        final Player player = httpSession.attribute(GetHomeRoute.PLAYER_SESSION_KEY);

        if(player == null) {
            Map<String, Object> vm = new HashMap<>();
            vm.put("title", TITLE);

            // render the View
            return templateEngine.render(new ModelAndView(vm , "sign-in.ftl"));
        }
        else {
            response.redirect(WebServer.HOME_URL);
            return null;
        }

    }
}