package pt.isel.ls.webApi

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.domain.toSessionState
import pt.isel.ls.services.PlayerServices
import pt.isel.ls.services.SessionServices
import pt.isel.ls.webApi.response.badResponse
import pt.isel.ls.webApi.response.createdResponse
import pt.isel.ls.webApi.response.foundResponse
import pt.isel.ls.webApi.response.okResponse
import pt.isel.ls.webApi.response.tryResponse
import pt.isel.ls.webApi.response.unauthorizedResponse

/**
 * Handles session-related HTTP requests.
 *
 * This class provides methods for creating a new session, retrieving session details, retrieving sessions
 * by specified criteria and adding a player to a session.
 *
 * Each method corresponds to a specific HTTP request and returns an HTTP response.
 */
class SessionHandler(
    private val sessionManagement: SessionServices,
    private val playerManagement: PlayerServices,
) : SessionHandlerInterface {
    override fun createSession(request: Request): Response {
        unauthorizedAccess(request, playerManagement)?.let { return unauthorizedResponse(it) }
        val body = readBody(request)
        val gid = body["gid"]?.toUIntOrNull()
        val gameName = body["gameName"]
        val owner = body["owner"]?.toUIntOrNull()
        val date = dateVerification(body["date"])
        val ownerName = body["ownerName"]
        val capacity = body["capacity"]?.toUIntOrNull()
        val params = arrayOf(gid, date, capacity)
        return if (params.any { it == null }) {
            badResponse(invalidParamsRspCreateSession(gid, date, capacity))
        } else {
            tryResponse(Status.BAD_REQUEST, "Unable to create session.") {
                checkNotNull(date) { "Date is null" }
                checkNotNull(capacity) { "Capacity is null" }
                val sessionOwner = Pair(owner, ownerName)
                val gameInfo = Pair(gid, gameName)
                val sid = sessionManagement.createSession(gameInfo, date, capacity, sessionOwner)
                createdResponse(createJsonRspMessage(message = "Session created with ID: $sid successfully.", id = sid))
            }
        }
    }

    override fun getSession(request: Request): Response {
        unauthorizedAccess(request, playerManagement)?.let { return unauthorizedResponse(it) }
        val sid =
            request.toSidOrNull() ?: return badResponse("Missing or invalid sid")
        val limit = request.query("limit")?.toUIntOrNull()
        val offset = request.query("offset")?.toUIntOrNull()
        return tryResponse(Status.NOT_FOUND, "Session not found.") {
            val session = sessionManagement.sessionDetails(sid, limit, offset)
            foundResponse(Json.encodeToString(session))
        }
    }

    override fun getSessions(request: Request): Response {
        unauthorizedAccess(request, playerManagement)?.let { return unauthorizedResponse(it) }
        val gid = request.query("gid")?.toUIntOrNull()
        val date = dateVerification(request.query("date"))
        val userName = request.query("username")
        val gameName = request.query("gameName")
        val state = request.query("state").toSessionState()
        val pid = request.query("pid")?.toUIntOrNull()
        val offset = request.query("offset")?.toUIntOrNull()
        val limit = request.query("limit")?.toUIntOrNull()
        val array = arrayOf(gid, date, state, pid, userName, gameName)
        if (array.all { it == null }) {
            val msg =
                """
                Missing parameters.
                Please provide at least one of the following:
                'gid', 'date', 'state', 'pid', 'userName'
                """.trimIndent()
            return badResponse(msg)
        }
        return tryResponse(Status.NOT_FOUND, "An error occurred while retrieving sessions.") {
            val gameInfo = Pair(gid, gameName)
            val playerInfo = Pair(pid, userName)
            val sessionsInfo = sessionManagement.getSessions(gameInfo, date, state, playerInfo, offset, limit)
            foundResponse(Json.encodeToString(sessionsInfo))
        }
    }

    override fun addPlayerToSession(request: Request): Response {
        unauthorizedAccess(request, playerManagement)?.let { return unauthorizedResponse(it) }
        val player = request.toPidOrNull()
        val session = request.toSidOrNull()
        return if (player == null || session == null) {
            val msg = "Invalid or Missing parameters. Please provide 'player' and 'session' as valid values"
            badResponse(msg)
        } else {
            tryResponse(Status.BAD_REQUEST, "Error adding Player $player to the Session $session") {
                sessionManagement.addPlayer(player, session)
                okResponse(createJsonRspMessage("Player $player added to Session $session successfully."))
            }
        }
    }

    override fun updateCapacityOrDate(request: Request): Response {
        unauthorizedAccess(request, playerManagement)?.let { return unauthorizedResponse(it) }
        val body = readBody(request)
        val sid = request.toSidOrNull()
        val pid = body["pid"]?.toUIntOrNull()
        val capacity = body["capacity"]?.toUIntOrNull()
        val date = dateVerification(body["date"])
        if (pid == null || sid == null) {
            return badResponse("Invalid/Missing 'sid' or 'pid'. Session not modified")
        }
        if (capacity == null && date == null) {
            return badResponse("capacity and date not provided. Session not modified")
        }
        return tryResponse(
            Status.CONFLICT,
            "Error updating session $sid. Check if $sid is valid",
        ) {
            val authentication = Pair(pid, sid)
            sessionManagement.updateCapacityOrDate(authentication, capacity, date)
            return okResponse(createJsonRspMessage("Session $sid updated successfully"))
        }
    }

    override fun removePlayerFromSession(request: Request): Response {
        unauthorizedAccess(request, playerManagement)?.let { return unauthorizedResponse(it) }
        val player = request.toPidOrNull()
        val session = request.toSidOrNull()
        val token = request.toTokenOrNull()
        val args = arrayOf(player, session, token)
        return if (args.any { it == null }) {
            val msg = "Invalid or Missing parameters. Please provide 'player' and 'session' as valid values"
            badResponse(msg)
        } else {
            tryResponse(Status.FORBIDDEN, "Error removing Player $player from the Session $session.") {
                checkNotNull(player) { "Player is null" }
                checkNotNull(session) { "Session is null" }
                checkNotNull(token) { "Token is null" }
                sessionManagement.removePlayer(player, session, token.toString())
                val msg = "Player $player removed from Session $session successfully."
                return okResponse(createJsonRspMessage(msg))
            }
        }
    }

    override fun deleteSession(request: Request): Response {
        unauthorizedAccess(request, playerManagement)?.let { return unauthorizedResponse(it) }
        val sid = request.toSidOrNull()
        return if (sid == null) {
            badResponse("Invalid or Missing 'sid'")
        } else {
            tryResponse(Status.FORBIDDEN, "Error deleting Session $sid.") {
                sessionManagement.deleteSession(sid)
                return okResponse(createJsonRspMessage("Session $sid deleted successfully."))
            }
        }
    }

    override fun getPlayerFromSession(request: Request): Response {
        unauthorizedAccess(request, playerManagement)?.let { return unauthorizedResponse(it) }
        val player = request.toPidOrNull()
        val session = request.toSidOrNull()
        return if (player == null || session == null) {
            val msg = "Invalid or Missing parameters. Please provide 'player' and 'session' as valid values"
            badResponse(msg)
        } else {
            tryResponse(Status.NOT_FOUND, "Error retrieving Player $player from Session $session.") {
                val foundPlayer = sessionManagement.getPlayerFromSession(player, session)
                return foundResponse(Json.encodeToString(foundPlayer))
            }
        }
    }
}
