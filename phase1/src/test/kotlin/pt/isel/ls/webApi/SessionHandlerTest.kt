package pt.isel.ls.webApi

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import pt.isel.ls.services.SessionManagementStunt
import pt.isel.ls.services.SessionManagementStunt.createSession
import kotlin.test.assertEquals

private const val DUMMY_ROUTE = "/dummyRoute"

class SessionHandlerTest {
    private fun actionOfASessionArrangement(act: (SessionHandlerInterface) -> Unit) =
        // arrangement
        SessionHandler(SessionManagementStunt)
            .let(act)

    @Test
    fun `bad request status creating a session due to lack of parameters`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.POST, DUMMY_ROUTE)
            val response = handler.createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `created status creating a session successfully`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.POST, DUMMY_ROUTE).body("{gid: 1, date: 2024-03-16T12:30, capacity: 10}")
            val response = handler.createSession(request)
            assertEquals(Status.CREATED, response.status)
        }
    }

    @Test
    fun `message creating a session successfully`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.POST, DUMMY_ROUTE).body("{gid: 1, date: 2024-03-16T12:30, capacity: 10}")
            val response = handler.createSession(request)
            assertEquals("Session created with ID: 1 successfully.", response.bodyString())
        }
    }

    @Test
    fun `bad request status creating a session due to missing gid`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.POST, DUMMY_ROUTE).body("{date: 2024-03-16T12:30:00, capacity: 10}")
            val response = handler.createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `bad request status creating a session due to missing date`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.POST, DUMMY_ROUTE).body("{gid: 1, capacity: 10}")
            val response = handler.createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `bad request status creating a session due to missing capacity`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.POST, DUMMY_ROUTE).body("{gid: 1, date: 2024-03-16T12:30:00}")
            val response = handler.createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `bad request status creating a session due to invalid date format`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.POST, DUMMY_ROUTE).body("{gid: 1, date: invalid, capacity: 10}")
            val response = handler.createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `bad request status getting a session due to missing sid`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, DUMMY_ROUTE)
            val response = handler.getSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `not found status getting a session`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "$DUMMY_ROUTE?sid=123")
            val response = handler.getSession(request)
            assertEquals(Status.NOT_FOUND, response.status)
        }
    }

    @Test
    fun `found status getting a session`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "$DUMMY_ROUTE?sid=1")
            val response = handler.getSession(request)
            assertEquals(Status.FOUND, response.status)
        }
    }

    @Test
    fun `message of getting a session successfully`() {
        val sid = "1"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "/$DUMMY_ROUTE?sid=$sid")
            val response = handler.getSession(request)
            assertEquals(
                expected =
                    "{\"sid\":1,\"capacity\":1,\"gid\":1,\"date\":\"2024-03-10T12:30\"," +
                        "\"players\":[{\"pid\":1,\"name\":\"test1\"," +
                        "\"email\":{\"email\":\"default@mail.com\"},\"token\":\"${SessionManagementStunt.playerToken}\"}]}",
                actual = response.bodyString(),
            )
        }
    }

    @Test
    fun `bad request status adding a player to a session due to missing parameters`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.POST, DUMMY_ROUTE)
            val response = handler.addPlayerToSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `not modified status adding player to session due to player not found`() {
        val playerId = "1"
        val sessionId = "50000"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.POST, DUMMY_ROUTE).body("{player: $playerId, session: $sessionId}")
            val response = handler.addPlayerToSession(request)
            assertEquals(Status.NOT_MODIFIED, response.status)
        }
    }

    @Test
    fun `OK status adding a player to a session`() {
        val playerId = "1"
        val sessionId = "1"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.POST, "/sessionTest").body("{player: $playerId, session: $sessionId}")
            val response = handler.addPlayerToSession(request)
            assertEquals(Status.OK, response.status)
        }
    }

    @Test
    fun `found status getting sessions with valid parameters`() {
        val gid = "1"
        val state = "close"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "/sessions?gid=$gid&state=$state")
            val response = handler.getSessions(request)
            assertEquals(Status.FOUND, response.status)
        }
    }

    @Test
    fun `bad request status getting sessions due to missing gid`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "/sessionTest")
            val response = handler.getSessions(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `internal server error status getting sessions `() {
        val gid = "3"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "/sessionTest?gid=$gid")
            val response = handler.getSessions(request)
            assertEquals(Status.INTERNAL_SERVER_ERROR, response.status)
        }
    }

    @Test
    fun `not found status getting sessions due to no sessions satisfy details provided`() {
        val gid = "400"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "/sessionTest?gid=$gid")
            val response = handler.getSessions(request)
            assertEquals(Status.NOT_FOUND, response.status)
        }
    }

    @Test
    fun `message of getting sessions successfully`() {
        val sid = "1"
        val state = "close"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "//sessionTest?gid=$sid&state=$state")
            val response = handler.getSessions(request)
            assertEquals(
                expected =
                    "[{\"sid\":1,\"capacity\":1,\"gid\":1,\"date\":\"2024-03-10T12:30\"," +
                        "\"players\":[{\"pid\":1,\"name\":\"test1\",\"email\":{\"email\":\"default@mail.com\"}," +
                        "\"token\":\"${SessionManagementStunt.playerToken}\"}]}," +
                        "{\"sid\":2,\"capacity\":2,\"gid\":1,\"date\":\"2024-03-10T12:30\"," +
                        "\"players\":[" +
                        "{\"pid\":1,\"name\":\"test1\",\"email\":{\"email\":\"default@mail.com\"}," +
                        "\"token\":\"${SessionManagementStunt.playerToken}\"}," +
                        "{\"pid\":2,\"name\":\"test2\",\"email\":{\"email\":\"default@mail.com\"}," +
                        "\"token\":\"${SessionManagementStunt.playerToken}\"}]}]",
                actual = response.bodyString(),
            )
        }
    }
}
