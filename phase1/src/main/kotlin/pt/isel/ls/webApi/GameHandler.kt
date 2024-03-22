package pt.isel.ls.webApi

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.services.GameServices

class GameHandler(private val gameManagement: GameServices) : GameHandlerInterface {
    override fun createGame(request: Request): Response {
        val body = readBody(request)
        val name = body["name"]
        val dev = body["dev"]
        val genres = body["genres"] ?.let { processGenres(it) }

        return if (name == null || dev == null || genres == null) {
            makeResponse(Status.BAD_REQUEST, "Bad Request")
        } else {
            tryResponse(Status.INTERNAL_SERVER_ERROR, "Internal Server Error") {
                val gid = gameManagement.createGame(name, dev, genres)
                makeResponse(Status.CREATED, "Game created with id $gid.")
            }
        }
    }

    override fun getGameDetails(request: Request): Response {
        val gid =
            getParameter(
                request = request,
                parameter = "gid",
            )?.toUIntOrNull()

        val offset =
            getParameter(
                request = request,
                parameter = "offset",
            )?.toUIntOrNull()

        val limit =
            getParameter(
                request = request,
                parameter = "limit",
            )?.toUIntOrNull()

        return if (gid == null || offset == null || limit == null) {
            makeResponse(Status.BAD_REQUEST, "Bad Request")
        } else {
            tryResponse(Status.NOT_FOUND, "Game not found.") {
                val game = gameManagement.getGameDetails(gid, offset, limit)
                makeResponse(Status.FOUND, game.toString())
            }
        }
    }

    override fun getGameByDevAndGenres(request: Request): Response {
        val offset =
            getParameter(
                request = request,
                parameter = "offset",
            )?.toUIntOrNull()

        val limit =
            getParameter(
                request = request,
                parameter = "limit",
            )?.toUIntOrNull()

        val dev =
            getParameter(
                request = request,
                parameter = "dev",
            )

        val genres =
            getParameter(
                request = request,
                parameter = "genres",
            )?.let { processGenres(it) }

        return if (offset == null || limit == null || dev == null || genres == null) {
            makeResponse(Status.BAD_REQUEST, "Bad Request")
        } else {
            tryResponse(Status.NOT_FOUND, "Game not found.") {
                val games = gameManagement.getGameByDevAndGenres(offset, limit, dev, genres)
                makeResponse(Status.FOUND, games.toString())
            }
        }
    }

    /**
     * Processes the genres string and returns a collection of genres.
     *
     * @param genres The string containing the genres.
     * @return The collection of genres.
     */
    private fun processGenres(genres: String): Collection<String> = genres.split(",")
}
