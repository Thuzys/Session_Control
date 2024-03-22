package pt.isel.ls.webApi

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.domain.toSessionState
import pt.isel.ls.services.SessionServices

/**
 * Handles player-related HTTP requests.
 *
 * This class provides methods for creating a new session, retrieving session details, retrieving sessions
 * by specified criteria and adding a player to a session.
 *
 * Each method corresponds to a specific HTTP request and returns an HTTP response.
 */
class SessionHandler(private val sessionServices: SessionServices) : SessionHandlerInterface {
    override fun createSession(request: Request): Response {
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
                val sid = sessionServices.createSession(gid, date, capacity)
                makeResponse(Status.CREATED, "Session created with ID: $sid successfully.")
            }
        }
    }

    override fun getSession(request: Request): Response {
        val sid = getParameter(request, "sid")?.toUInt() ?: return makeResponse(Status.BAD_REQUEST, "Missing or invalid sid.")
        return tryResponse(Status.NOT_FOUND, "Session not found.") {
            val session = sessionServices.getSessionDetails(sid)
            makeResponse(Status.FOUND, Json.encodeToString(session))
        }
    }

    override fun getSessions(request: Request): Response {
        val gid = getParameter(request, "gid")?.toUIntOrNull()
        val date = dateVerification(getParameter(request, "date"))
        val state = getParameter(request, "state").toSessionState()
        val playerId = getParameter(request, "playerId")?.toUIntOrNull()
        val offset = getParameter(request, "offset")?.toUIntOrNull()
        val limit = getParameter(request, "limit")?.toUIntOrNull()
        return if (gid == null) {
            makeResponse(Status.BAD_REQUEST, "Invalid or Missing Game Identifier.")
        } else {
            tryResponse(Status.INTERNAL_SERVER_ERROR, "Internal Server Error.") {
                val sessions = sessionServices.getSessions(gid, date, state, playerId, offset, limit)
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
    }

    override fun addPlayerToSession(request: Request): Response {
        val body = readBody(request)
        val player = body["player"]?.toUIntOrNull()
        val session = body["session"]?.toUIntOrNull()
        return if (player == null || session == null) {
            makeResponse(
                Status.BAD_REQUEST,
                "Invalid or Missing parameters. Please provide 'player' and 'session' as valid values.",
            )
        } else {
            tryResponse(Status.NOT_MODIFIED, "Error adding Player $player to the Session $session.") {
                sessionServices.addPlayer(player, session)
                return makeResponse(Status.OK, "Player $player added to Session $session successfully.")
            }
        }
    }
}
