package pt.isel.ls

import org.http4k.routing.RoutingHttpHandler
import pt.isel.ls.server.buildRoutes
import pt.isel.ls.services.GameManagement
import pt.isel.ls.services.PlayerManagement
import pt.isel.ls.services.SessionManagement
import pt.isel.ls.storage.GameStorage
import pt.isel.ls.storage.PlayerStorage
import pt.isel.ls.storage.SessionStorage
import pt.isel.ls.webApi.GameHandler
import pt.isel.ls.webApi.PlayerHandler
import pt.isel.ls.webApi.SessionHandler

/**
 * Builds the routing HTTP handler for the application services.
 *
 * @param envName the environment variable name
 * @return the routing HTTP handler for the application services
 * @see RoutingHttpHandler
 */
internal fun routingHttpHandler(envName: String): RoutingHttpHandler {
    val playerStorage = PlayerStorage(envName)
    val gameStorage = GameStorage(envName)
    val sessionStorage = SessionStorage(envName)

    val playerServices = PlayerManagement(playerStorage)
    val gameServices = GameManagement(gameStorage)
    val sessionServices = SessionManagement(sessionStorage, playerStorage)

    val playerHandler = PlayerHandler(playerServices)
    val gameHandler = GameHandler(gameServices, playerServices)
    val sessionHandler = SessionHandler(sessionServices, playerServices)

    return buildRoutes(playerHandler, gameHandler, sessionHandler)
}
