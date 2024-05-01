package pt.isel.ls.server

import org.http4k.core.Method
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.webApi.GameHandlerInterface
import pt.isel.ls.webApi.PlayerHandlerInterface
import pt.isel.ls.webApi.SessionHandlerInterface

const val PLAYER_ROUTE = "/players"
const val GAME_ROUTE = "/games"
const val SESSION_ROUTE = "/sessions"
const val PLAYER_ID_ROUTE = "$PLAYER_ROUTE/{pid}"
const val GAME_ID_ROUTE = "$GAME_ROUTE/{gid}"
const val SESSION_ID_ROUTE = "$SESSION_ROUTE/{sid}"
const val SESSION_PLAYER_ROUTE = "$SESSION_ID_ROUTE/{pid}"
// const val SESSION_PLAYER_ID_ROUTE = "$SESSION_ID_ROUTE/player"

/**
 * Builds the routes for the application services
 *
 * @param playerHandler the handler for the app
 * @return the routes for the application services
 */
fun buildRoutes(
    playerHandler: PlayerHandlerInterface,
    gameHandler: GameHandlerInterface,
    sessionHandler: SessionHandlerInterface,
) = routes(
    PLAYER_ID_ROUTE bind Method.GET to playerHandler::getPlayer,
    PLAYER_ROUTE bind Method.POST to playerHandler::createPlayer,
    GAME_ID_ROUTE bind Method.GET to gameHandler::getGameDetails,
    GAME_ROUTE bind Method.POST to gameHandler::createGame,
    GAME_ROUTE bind Method.GET to gameHandler::getGameByDevAndGenres,
    SESSION_PLAYER_ROUTE bind Method.PUT to sessionHandler::addPlayerToSession,
    SESSION_ID_ROUTE bind Method.GET to sessionHandler::getSession,
    SESSION_ROUTE bind Method.POST to sessionHandler::createSession,
    SESSION_ROUTE bind Method.GET to sessionHandler::getSessions,
    SESSION_ID_ROUTE bind Method.POST to sessionHandler::updateCapacityOrDate,
    SESSION_PLAYER_ROUTE bind Method.DELETE to sessionHandler::removePlayerFromSession,
    SESSION_ID_ROUTE bind Method.DELETE to sessionHandler::deleteSession,
)
