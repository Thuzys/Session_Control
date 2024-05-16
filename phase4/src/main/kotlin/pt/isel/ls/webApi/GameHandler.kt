package pt.isel.ls.webApi

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.services.GameServices
import pt.isel.ls.services.PlayerServices

/**
 * Class responsible for handling the requests related to the games.
 */
class GameHandler(
    private val gameManagement: GameServices,
    private val playerServices: PlayerServices,
) : GameHandlerInterface {
    override fun createGame(request: Request): Response {
        unauthorizedAccess(request, playerServices)?.let { return unauthorizedResponse(it) }
        val body = readBody(request)
        val name = body["name"]
        val dev = body["dev"]
        val genres = body["genres"] ?.let { processGenres(it) }
        val array = arrayOf(name, dev, genres)
        return if (array.any { it == null }) {
            badRequestResponse("Missing arguments: name:$name, dev:$dev, genres:$genres")
        } else {
            tryResponse(
                Status.BAD_REQUEST,
                "Invalid arguments: name:$name, dev:$dev, genres:$genres.",
            ) {
                name as String
                dev as String
                genres as List<String>
                val gid = gameManagement.createGame(name, dev, genres)
                makeResponse(
                    Status.CREATED,
                    createJsonRspMessage(
                        message = "Game created with id $gid.",
                        id = gid,
                    ),
                )
            }
        }
    }

    override fun getGameDetails(request: Request): Response {
        unauthorizedAccess(request, playerServices)?.let { return unauthorizedResponse(it) }
        val gid = request.toGidOrNull()

        return if (gid == null) {
            badRequestResponse("Invalid arguments: gid must be provided")
        } else {
            tryResponse(Status.NOT_FOUND, "Game not found.") {
                val game = gameManagement.getGameDetails(gid)
                makeResponse(Status.FOUND, Json.encodeToString(game))
            }
        }
    }

    override fun getGames(request: Request): Response {
        unauthorizedAccess(request, playerServices)?.let { return unauthorizedResponse(it) }
        val offset = request.query("offset")?.toUIntOrNull()
        val limit = request.query("limit")?.toUIntOrNull()
        val dev = request.query("dev")
        val genres = request.query("genres") ?.let { processGenres(it) }
        val name = request.query("name")
        val array = arrayOf(dev, genres, name)
        return if (array.all { it == null }) {
            badRequestResponse(
                "Invalid arguments: at least one of the following must be provided: dev, genres, name",
            )
        } else {
            tryResponse(Status.NOT_FOUND, "Game not found.") {
                val games = gameManagement.getGames(dev, genres, name, offset, limit)
                makeResponse(Status.FOUND, Json.encodeToString(games))
            }
        }
    }
}
