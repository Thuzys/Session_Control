package pt.isel.ls.webApi

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.domain.toSessionState
import pt.isel.ls.services.PlayerServices
import pt.isel.ls.services.SessionServices

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
        unauthorizedAccess(request, playerManagement)
            ?.let { return makeResponse(Status.UNAUTHORIZED, "Unauthorized, $it.") }
        val body = readBody(request)
        val gid = body["gid"]?.toUIntOrNull()
        val date = dateVerification(body["date"])
        val capacity = body["capacity"]?.toUIntOrNull()
        return if (gid == null || date == null || capacity == null) {
            makeResponse(
                Status.BAD_REQUEST,
                "Missing or invalid parameters. Please provide 'gid', 'date', and 'capacity' as valid values.",
            )
        } else {
            tryResponse(Status.INTERNAL_SERVER_ERROR, "Internal server error.") {
                val sid = sessionManagement.createSession(gid, date, capacity)
                makeResponse(Status.CREATED, "Session created with ID: $sid successfully.")
            }
        }
    }

    override fun getSession(request: Request): Response {
        unauthorizedAccess(request, playerManagement)
            ?.let { return makeResponse(Status.UNAUTHORIZED, "Unauthorized, $it.") }
        val sid = request.query("sid")?.toUInt() ?: return makeResponse(Status.BAD_REQUEST, "Missing or invalid sid.")
        return tryResponse(Status.NOT_FOUND, "Session not found.") {
            val session = sessionManagement.getSessionDetails(sid)
            makeResponse(Status.FOUND, Json.encodeToString(session))
        }
    }

    override fun getSessions(request: Request): Response {
        unauthorizedAccess(request, playerManagement)
            ?.let { return makeResponse(Status.UNAUTHORIZED, "Unauthorized, $it.") }
        val gid = request.query("gid")?.toUIntOrNull()
        val date = dateVerification(request.query("date"))
        val state = request.query("state").toSessionState()
        val playerId = request.query("pid")?.toUIntOrNull()
        val offset = request.query("offset")?.toUIntOrNull()
        val limit = request.query("limit")?.toUIntOrNull()
        if (gid == null && date == null && state == null && playerId == null && offset == null && limit == null) {
            return makeResponse(
                Status.BAD_REQUEST,
                "Missing or invalid parameters. Please provide at" +
                    " least one of the following: 'gid', 'date', 'state', 'pid', 'offset', 'limit'.",
            )
        }
        return tryResponse(Status.INTERNAL_SERVER_ERROR, "Internal Server Error.") {
            val sessions = sessionManagement.getSessions(gid, date, state, playerId, offset, limit)
            return if (sessions.isEmpty()) {
                makeResponse(
                    Status.NOT_FOUND,
                    "No sessions found with the specified details.",
                )
            } else {
                makeResponse(Status.FOUND, Json.encodeToString(sessions))
            }
        }
    }

    override fun addPlayerToSession(request: Request): Response {
        unauthorizedAccess(request, playerManagement)
            ?.let { return makeResponse(Status.UNAUTHORIZED, "Unauthorized, $it.") }
        val body = readBody(request)
        val player = body["pid"]?.toUIntOrNull()
        val session = body["sid"]?.toUIntOrNull()
        return if (player == null || session == null) {
            makeResponse(
                Status.BAD_REQUEST,
                "Invalid or Missing parameters. Please provide 'player' and 'session' as valid values.",
            )
        } else {
            tryResponse(Status.INTERNAL_SERVER_ERROR, "Error adding Player $player to the Session $session") {
                sessionManagement.addPlayer(player, session)
                return makeResponse(Status.OK, "Player $player added to Session $session successfully.")
            }
        }
    }

    override fun updateCapacityOrDate(request: Request): Response {
        unauthorizedAccess(request, playerManagement)
            ?.let { return makeResponse(Status.UNAUTHORIZED, "Unauthorized, $it.") }
        val body = readBody(request)
        val sid = body["sid"]?.toUIntOrNull()
        val capacity = body["capacity"]?.toUIntOrNull()
        val date = dateVerification(body["date"])
        return when {
            (capacity == null && date == null) ->
                makeResponse(
                    Status.BAD_REQUEST,
                    "capacity and date not provided. Session not modified",
                )

            (sid == null) ->
                makeResponse(
                    Status.BAD_REQUEST,
                    "Invalid or Missing parameters. Please provide sid",
                )

            else ->
                tryResponse(Status.NOT_MODIFIED, "Error updating session $sid. Check if $sid is valid") {
                    sessionManagement.updateCapacityOrDate(sid, capacity, date)
                    return makeResponse(Status.OK, "Session $sid updated successfully")
                }
        }
    }

    override fun removePlayerFromSession(request: Request): Response {
        unauthorizedAccess(request, playerManagement)
            ?.let { return makeResponse(Status.UNAUTHORIZED, "Unauthorized, $it.") }
        val body = readBody(request)
        val player = body["pid"]?.toUIntOrNull()
        val session = body["sid"]?.toUIntOrNull()
        return if (player == null || session == null) {
            makeResponse(
                Status.BAD_REQUEST,
                "Invalid or Missing parameters. Please provide 'player' and 'session' as valid values.",
            )
        } else {
            tryResponse(Status.NOT_MODIFIED, "Error removing Player $player from the Session $session.") {
                sessionManagement.removePlayer(player, session)
                return makeResponse(Status.OK, "Player $player removed from Session $session successfully.")
            }
        }
    }

    override fun deleteSession(request: Request): Response {
        unauthorizedAccess(request, playerManagement)
            ?.let { return makeResponse(Status.UNAUTHORIZED, "Unauthorized, $it.") }

        val body = readBody(request)
        val sid = body["sid"]?.toUIntOrNull()

        return if (sid == null) {
            makeResponse(Status.BAD_REQUEST, "Invalid or Missing Session Identifier.")
        } else {
            tryResponse(Status.NOT_MODIFIED, "Error deleting Session $sid.") {
                sessionManagement.deleteSession(sid)
                return makeResponse(Status.OK, "Session $sid deleted successfully.")
            }
        }
    }

//    override fun getSessionsByPlayer(request: Request): Response {
//        unauthorizedAccess(request, playerManagement)
//            ?.let { return makeResponse(Status.UNAUTHORIZED, "Unauthorized, $it") }
//        val pid = request.query("pid")?.toUIntOrNull()
//        return if (pid == null) {
//            makeResponse(Status.BAD_REQUEST, "Invalid or Missing Player Identifier.")
//        } else {
//            tryResponse(Status.INTERNAL_SERVER_ERROR, "Internal Server Error.") {
//                val sessions = sessionManagement.getSessionsByPlayer(pid)
//                return if (sessions.isEmpty()) {
//                    makeResponse(
//                        Status.NOT_FOUND,
//                        "No sessions found for player with ID $pid.",
//                    )
//                } else {
//                    makeResponse(Status.FOUND, Json.encodeToString(sessions))
//                }
//            }
//        }
//    }
}
