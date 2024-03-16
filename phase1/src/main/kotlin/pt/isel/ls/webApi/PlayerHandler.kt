package pt.isel.ls.webApi

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.services.PlayerServices
import pt.isel.ls.utils.getParameter
import pt.isel.ls.utils.makeResponse
import pt.isel.ls.utils.readBody
import pt.isel.ls.utils.tryResponse

/**
 * Handles player-related HTTP requests.
 *
 * This class provides methods for creating a new player and retrieving player details.
 * Each method corresponds to a specific HTTP request and returns an HTTP response.
 */
class PlayerHandler(private val playerManagement: PlayerServices) : PlayerHandlerInterface {
    override fun createPlayer(request: Request): Response {
        val body = readBody(request)
        val name = body["name"]
        val email = body["email"]
        return if (name == null || email == null) {
            makeResponse(Status.BAD_REQUEST, "Bad Request")
        } else {
            tryResponse(Status.INTERNAL_SERVER_ERROR, "Internal Server Error") {
                val (id, token) = playerManagement.createPlayer(name, email)
                makeResponse(Status.CREATED, "Player created with id $id and token $token.")
            }
        }
    }

    override fun getPlayer(request: Request): Response {
        val pid =
            getParameter(
                request = request,
                parameter = "pid",
            )?.toUIntOrNull() ?: return makeResponse(Status.BAD_REQUEST, "Bad Request")
        return tryResponse(Status.NOT_FOUND, "Player not found.") {
            val player = playerManagement.getPlayerDetails(pid)
            makeResponse(Status.FOUND, Json.encodeToString(player))
        }
    }
}
