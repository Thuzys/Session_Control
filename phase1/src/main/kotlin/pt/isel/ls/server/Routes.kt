package pt.isel.ls.server

import org.http4k.core.Method
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.webApi.PlayerHandlerInterface

const val PLAYER_ROUTE = "/players"

/**
 * Builds the routes for the application services
 *
 * @param playerHandler the handler for the app
 * @return the routes for the application services
 */
fun buildRoutes(playerHandler: PlayerHandlerInterface) =
    routes(
        PLAYER_ROUTE bind Method.GET to playerHandler::getPlayer,
        PLAYER_ROUTE bind Method.POST to playerHandler::createPlayer,
    )
