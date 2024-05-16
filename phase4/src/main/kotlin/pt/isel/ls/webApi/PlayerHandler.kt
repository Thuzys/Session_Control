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
        val userName = body["username"]
        val email = body["email"]
        val password = body["password"]
        val params = arrayOf(name, email, password)
        return if (params.any { it == null }) {
            badRequestResponse("insufficient parameters")
        } else {
            tryResponse(
                errorStatus = Status.BAD_REQUEST,
                errorMsg = "Unable to create player.",
            ) {
                name as String
                email as String
                password as String
                val nameParam = name to userName
                val emailPassParam = email to password
                val (id, token) = playerManagement.createPlayer(nameParam, emailPassParam)
                makeResponse(
                    Status.CREATED,
                    createJsonRspMessage(
                        message = "Player created with id $id and token $token.",
                        id = id,
                    ),
                )
            }
        }
    }

    override fun getPlayer(request: Request): Response {
        unauthorizedAccess(request, playerManagement)?.let { return unauthorizedResponse(it) }
        val pid = request.toPidOrNull() ?: return badRequestResponse("pid not found or invalid")
        return tryResponse(
            errorStatus = Status.NOT_FOUND,
            errorMsg = "Player not found.",
        ) {
            val player = playerManagement.getPlayerDetails(pid)
            makeResponse(Status.FOUND, Json.encodeToString(player))
        }
    }

    override fun getPlayerBy(request: Request): Response {
        unauthorizedAccess(request, playerManagement)?.let { return unauthorizedResponse(it) }
        val userName = request.readQuery("username")
        return if (userName == null) {
            badRequestResponse("insufficient parameters")
        } else {
            tryResponse(
                errorStatus = Status.NOT_FOUND,
                errorMsg = "Player not found.",
            ) {
                val player = playerManagement.getPlayerDetailsBy(userName)
                makeResponse(Status.FOUND, Json.encodeToString(player))
            }
        }
    }
}
