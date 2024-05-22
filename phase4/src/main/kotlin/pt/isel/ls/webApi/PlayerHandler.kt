package pt.isel.ls.webApi

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.services.PlayerServices
import pt.isel.ls.webApi.response.badResponse
import pt.isel.ls.webApi.response.createdResponse
import pt.isel.ls.webApi.response.foundResponse
import pt.isel.ls.webApi.response.okResponse
import pt.isel.ls.webApi.response.tryResponse
import pt.isel.ls.webApi.response.unauthorizedResponse

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
            badResponse("insufficient parameters")
        } else {
            tryResponse(Status.BAD_REQUEST, "Unable to create player.") {
                name as String
                email as String
                password as String
                val nameParam = name to userName
                val emailPassParam = email to password
                val authentication = playerManagement.createPlayer(nameParam, emailPassParam)
                createdResponse(Json.encodeToString(authentication))
            }
        }
    }

    override fun getPlayer(request: Request): Response {
        unauthorizedAccess(request, playerManagement)?.let { return unauthorizedResponse(it) }
        val pid = request.toPidOrNull() ?: return badResponse("pid not found or invalid")
        return tryResponse(Status.NOT_FOUND, "Player not found.") {
            val player = playerManagement.getPlayerDetails(pid)
            foundResponse(Json.encodeToString(player))
        }
    }

    override fun getPlayerBy(request: Request): Response {
        unauthorizedAccess(request, playerManagement)?.let { return unauthorizedResponse(it) }
        val userName = request.readQuery("username")
        return if (userName == null) {
            badResponse("insufficient parameters")
        } else {
            tryResponse(Status.NOT_FOUND, "Player not found.") {
                val player = playerManagement.getPlayerDetailsBy(userName)
                foundResponse(Json.encodeToString(player))
            }
        }
    }

    override fun login(request: Request): Response {
        val body = readBody(request)
        val username = body["username"]
        val password = body["password"]
        val params = arrayOf(username, password)
        return if (params.any { it == null }) {
            badResponse("insufficient parameters")
        } else {
            tryResponse(Status.BAD_REQUEST, "Unable to login player.") {
                username as String
                password as String
                val playerInfo = playerManagement.login(username, password)
                okResponse(Json.encodeToString(playerInfo))
            }
        }
    }

    override fun logout(request: Request): Response {
        unauthorizedAccess(request, playerManagement)?.let { return unauthorizedResponse(it) }
        val token = request.toTokenOrNull()
        checkNotNull(token) { "token not found or invalid." }
        return tryResponse(Status.BAD_REQUEST, "Unable to logout player.") {
            playerManagement.logout(token)
            okResponse(createJsonRspMessage("Player logged out successfully."))
        }
    }
}
