package pt.isel.ls.webApi

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.services.PlayerServices

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
            makeResponse(Status.BAD_REQUEST, "Bad Request, insufficient parameters.")
        } else {
            tryResponse(
                errorStatus = Status.INTERNAL_SERVER_ERROR,
                errorMsg = "Internal Server Error.",
            ) {
                val (id, token) = playerManagement.createPlayer(name, email)
                makeResponse(Status.CREATED, "Player created with id $id and token $token.")
            }
        }
    }

    override fun getPlayer(request: Request): Response {
        unauthorizedAccess(
            request,
            playerManagement,
        )?.let { return makeResponse(Status.UNAUTHORIZED, "Unauthorized, $it.") }
        val pid = request.toPidOrNull() ?: return makeResponse(Status.BAD_REQUEST, "Bad Request, pid not found.")
        return tryResponse(
            errorStatus = Status.NOT_FOUND,
            errorMsg = "Player not found.",
        ) {
            val player = playerManagement.getPlayerDetails(pid)
            makeResponse(Status.FOUND, Json.encodeToString(player))
        }
    }
}
