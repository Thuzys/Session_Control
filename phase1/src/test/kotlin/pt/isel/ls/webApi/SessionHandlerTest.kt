package pt.isel.ls.webApi

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import pt.isel.ls.services.SessionManagementStunt
import kotlin.test.assertEquals

class SessionHandlerTest {
    private fun createSessionHandler(
        request: Request,
        code: SessionHandlerInterface.(request: Request) -> Unit,
    ) = SessionHandler(SessionManagementStunt).run { code(request) }

    @Test
    fun `bad request creating a session`() {
        createSessionHandler(Request(Method.POST, "/sessionTest")) { request: Request ->
            val response = createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `session created`() {
        createSessionHandler(
            request = Request(Method.POST, "/sessionTest").body("{gid: 1, date: 2024-03-16T12:30, capacity: 10}"),
        ) { request: Request ->
            val response = createSession(request)
            assertEquals(Status.CREATED, response.status)
        }
    }

    @Test
    fun `missing gid`() {
        createSessionHandler(
            request = Request(Method.POST, "/sessionTest").body("{date: 2024-03-16T12:30:00, capacity: 10}"),
        ) { request: Request ->
            val response = createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `missing date`() {
        createSessionHandler(
            request = Request(Method.POST, "/sessionTest").body("{gid: 1, capacity: 10}"),
        ) { request: Request ->
            val response = createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `missing capacity`() {
        createSessionHandler(
            request = Request(Method.POST, "/sessionTest").body("{gid: 1, date: 2024-03-16T12:30:00}"),
        ) { request: Request ->
            val response = createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `invalid date format`() {
        createSessionHandler(
            request = Request(Method.POST, "/sessionTest").body("{gid: 1, date: invalid, capacity: 10}"),
        ) { request: Request ->
            val response = createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `missing sid when getting session`() {
        createSessionHandler(Request(Method.GET, "/sessionTest")) { request: Request ->
            val response = getSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `missing parameters`() {
        createSessionHandler(Request(Method.POST, "/sessionTest")) { request: Request ->
            val response = addPlayerToSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `error adding player to session`() {
        val playerId = "1"
        val sessionId = "50000"
        createSessionHandler(
            Request(Method.POST, "/sessionTest").body("{player: $playerId, session: $sessionId}"),
        ) { request: Request ->
            val response = addPlayerToSession(request)
            assertEquals(Status.NOT_MODIFIED, response.status)
        }
    }

    @Test
    fun `player added successfully to session`() {
        val playerId = "1"
        val sessionId = "1"
        createSessionHandler(
            Request(Method.POST, "/addPlayerToSessionTest").body("{player: $playerId, session: $sessionId}"),
        ) { request: Request ->
            val response = addPlayerToSession(request)
            assertEquals(Status.OK, response.status)
        }
    }

    @Test
    fun `session found`() {
        val sid = "1"
        createSessionHandler(Request(Method.GET, "/sessionTest?sid=$sid")) { request: Request ->
            val response = getSession(request)
            assertEquals(Status.FOUND, response.status)
        }
    }

    @Test
    fun `session not found`() {
        val sid = "999"
        createSessionHandler(Request(Method.GET, "/sessionTest?sid=$sid")) { request: Request ->
            val response = getSession(request)
            assertEquals(Status.NOT_FOUND, response.status)
        }
    }

    @Test
    fun `test getSessions with valid parameters`() {
        val gid = "1"
        val state = "close"
        val getSessionsRequest = Request(Method.GET, "/sessions?gid=$gid&state=$state")
        createSessionHandler(getSessionsRequest) { request: Request ->
            val response = getSessions(request)
            assertEquals(Status.FOUND, response.status)
        }
    }

    @Test
    fun `getSessions with missing gid`() {
        createSessionHandler(Request(Method.GET, "/sessions")) { request: Request ->
            val response = getSessions(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `getSessions with internal server error`() {
        val gid = "3"
        createSessionHandler(Request(Method.GET, "/sessions?gid=$gid")) { request: Request ->
            val response = getSessions(request)
            assertEquals(Status.INTERNAL_SERVER_ERROR, response.status)
        }
    }

    @Test
    fun `getSessions not found`() {
        val gid = "400"
        createSessionHandler(Request(Method.GET, "/sessions?gid=$gid")) { request: Request ->
            val response = getSessions(request)
            assertEquals(Status.NOT_FOUND, response.status)
        }
    }
}
