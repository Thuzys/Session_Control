package pt.isel.ls.webApi

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.services.GameServices
import pt.isel.ls.services.PlayerServices
import pt.isel.ls.webApi.response.badResponse
import pt.isel.ls.webApi.response.createdResponse
import pt.isel.ls.webApi.response.foundResponse
import pt.isel.ls.webApi.response.tryResponse
import pt.isel.ls.webApi.response.unauthorizedResponse

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
        val params = arrayOf(name, dev, genres)

        return if (params.any { it == null }) {
            badResponse("Missing arguments: name:$name, dev:$dev, genres:$genres")
        } else {
            tryResponse(Status.BAD_REQUEST, "Invalid arguments: name:$name, dev:$dev, genres:$genres.") {
                checkNotNull(name) { "Name must be provided." }
                checkNotNull(dev) { "Dev must be provided." }
                checkNotNull(genres) { "Genres must be provided." }
                val gid = gameManagement.createGame(name, dev, genres)
                createdResponse(createJsonRspMessage(message = "Game created with id $gid.", id = gid))
            }
        }
    }

    override fun getGameDetails(request: Request): Response {
        unauthorizedAccess(request, playerServices)?.let { return unauthorizedResponse(it) }
        val gid = request.toGidOrNull()

        return if (gid == null) {
            badResponse("Invalid arguments: gid must be provided")
        } else {
            tryResponse(Status.NOT_FOUND, "Game not found.") {
                val game = gameManagement.getGameDetails(gid)
                foundResponse(Json.encodeToString(game))
            }
        }
    }

    override fun getGames(request: Request): Response {
        unauthorizedAccess(request, playerServices)?.let { return unauthorizedResponse(it) }

        val offset = request.query("offset")?.toUIntOrNull()
        val limit = request.query("limit")?.toUIntOrNull()
        val dev = request.query("dev")
        val genres = request.query("genres")?.let { processGenres(it) }
        val name = request.query("name")
        val params = arrayOf(dev, genres, name)

        return if (params.all { it == null }) {
            val msg = "Invalid arguments: at least one of the following must be provided: dev, genres, name"
            badResponse(msg)
        } else {
            tryResponse(Status.NOT_FOUND, "An error occurred while retrieving games.") {
                val games = gameManagement.getGames(dev, genres, name, offset, limit)
                foundResponse(Json.encodeToString(games))
            }
        }
    }

    override fun getAllGenres(request: Request): Response {
        return tryResponse(Status.NOT_FOUND, "Game not found.") {
            val genres = gameManagement.getAllGenres()
            foundResponse(Json.encodeToString(genres))
        }
    }
}
