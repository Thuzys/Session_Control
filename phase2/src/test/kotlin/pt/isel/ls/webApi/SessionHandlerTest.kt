package pt.isel.ls.webApi

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import pt.isel.ls.services.PlayerManagementStunt
import pt.isel.ls.services.SessionManagementStunt
import java.util.UUID
import kotlin.test.assertEquals

private const val DUMMY_ROUTE = "/dummyRoute"

class SessionHandlerTest {
    private fun actionOfASessionArrangement(act: (SessionHandlerInterface) -> Unit) =
        // arrangement
        SessionHandler(SessionManagementStunt, PlayerManagementStunt)
            .let(act)

    @Test
    fun `bad request status creating a session due to lack of parameters`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.POST,
                    "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}",
                )
            val response = handler.createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `created status creating a session successfully`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.POST,
                    "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}",
                ).body("{\"gid\": \"1\", \"date\": \"2024-03-16T12:30\", \"capacity\": \"10\"}")
            val response = handler.createSession(request)
            assertEquals(Status.CREATED, response.status)
        }
    }

    @Test
    fun `message creating a session successfully`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.POST,
                    "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}",
                ).body("{\"gid\": \"1\", \"date\": \"2024-03-16T12:30\", \"capacity\": \"10\"}")
            val response = handler.createSession(request)
            assertEquals("Session created with ID: 1 successfully.", response.bodyString())
        }
    }

    @Test
    fun `bad request status creating a session due to missing gid`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.POST,
                    "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}",
                ).body("{\"date\": \"2024-03-16T12:30:00\", \"capacity\": \"10\"}")
            val response = handler.createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `bad request status creating a session due to missing date`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.POST,
                    "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}",
                ).body("{gid: 1, capacity: 10}")
            val response = handler.createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `bad request status creating a session due to missing capacity`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.POST,
                    "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}",
                ).body("{gid: 1, date: 2024-03-16T12:30:00}")
            val response = handler.createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `bad request status creating a session due to invalid date format`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.POST,
                    "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}",
                ).body("{gid: 1, date: invalid, capacity: 10}")
            val response = handler.createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `bad request status getting a session due to missing sid`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}")
            val response = handler.getSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `not found status getting a session`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "$DUMMY_ROUTE?sid=123&token=${PlayerManagementStunt.playerToken}")
            val response = handler.getSession(request)
            assertEquals(Status.NOT_FOUND, response.status)
        }
    }

    @Test
    fun `found status getting a session`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "$DUMMY_ROUTE?sid=1&token=${PlayerManagementStunt.playerToken}")
            val response = handler.getSession(request)
            assertEquals(Status.FOUND, response.status)
        }
    }

    @Test
    fun `message of getting a session successfully`() {
        val sid = "1"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "/$DUMMY_ROUTE?sid=$sid&token=${PlayerManagementStunt.playerToken}")
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
            val request =
                Request(
                    Method.POST,
                    "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}",
                )
            val response = handler.addPlayerToSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `not modified status adding player to session due to player not found`() {
        val playerId = "1"
        val sessionId = "50000"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.POST,
                    "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}",
                ).body("{\"player\": \"$playerId\", \"session\": \"$sessionId\"}")
            val response = handler.addPlayerToSession(request)
            assertEquals(Status.NOT_MODIFIED, response.status)
        }
    }

    @Test
    fun `OK status adding a player to a session`() {
        val playerId = "1"
        val sessionId = "1"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.POST,
                    "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}",
                ).body("{\"player\": \"$playerId\", \"session\": \"$sessionId\"}")
            val response = handler.addPlayerToSession(request)
            assertEquals(Status.OK, response.status)
        }
    }

    @Test
    fun `found status getting sessions with valid parameters`() {
        val gid = "1"
        val state = "close"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.GET,
                    "$DUMMY_ROUTE?gid=$gid&state=$state&token=${PlayerManagementStunt.playerToken}",
                )
            val response = handler.getSessions(request)
            assertEquals(Status.FOUND, response.status)
        }
    }

    @Test
    fun `bad request status getting sessions due to missing gid`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}")
            val response = handler.getSessions(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `internal server error status getting sessions `() {
        val gid = "3"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "$DUMMY_ROUTE?gid=$gid&token=${PlayerManagementStunt.playerToken}")
            val response = handler.getSessions(request)
            assertEquals(Status.INTERNAL_SERVER_ERROR, response.status)
        }
    }

    @Test
    fun `not found status getting sessions due to no sessions satisfy details provided`() {
        val gid = "400"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, "$DUMMY_ROUTE?gid=$gid&token=${PlayerManagementStunt.playerToken}")
            val response = handler.getSessions(request)
            assertEquals(Status.NOT_FOUND, response.status)
        }
    }

    @Test
    fun `message of getting sessions successfully`() {
        val sid = "1"
        val state = "close"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.GET,
                    "$DUMMY_ROUTE?gid=$sid&state=$state&token=${PlayerManagementStunt.playerToken}",
                )
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

    @Test
    fun `unauthorized status due lack of token during create session request`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(Method.POST, DUMMY_ROUTE)
                    .body("{\"gid\": \"1\", \"date\": \"2024-03-16T12:30\", \"capacity\": \"10\"}")
            val response = handler.createSession(request)
            assertEquals(Status.UNAUTHORIZED, response.status)
        }

    @Test
    fun `unauthorized message due invalid token during create session request`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.GET,
                    "$DUMMY_ROUTE?token=${UUID.randomUUID()}",
                ).body("{\"gid\": \"1\", \"date\": \"2024-03-16T12:30\", \"capacity\": \"10\"}")
            val response = handler.createSession(request)
            assertEquals("Unauthorized, invalid token.", response.bodyString())
        }

    @Test
    fun `unauthorized message due lack of token during create session request`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, DUMMY_ROUTE)
            val response = handler.createSession(request)
            assertEquals("Unauthorized, token not provided.", response.bodyString())
        }

    @Test
    fun `unauthorized status due lack of token during add player request`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val playerId = "1"
            val sessionId = "1"
            val request =
                Request(Method.POST, DUMMY_ROUTE)
                    .body("{\"player\": \"$playerId\", \"session\": \"$sessionId\"}")
            val response = handler.createSession(request)
            assertEquals(Status.UNAUTHORIZED, response.status)
        }

    @Test
    fun `unauthorized message due invalid token during add player request`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val playerId = "1"
            val sessionId = "1"
            val request =
                Request(
                    Method.GET,
                    "$DUMMY_ROUTE?token=${UUID.randomUUID()}",
                ).body("{\"player\": \"$playerId\", \"session\": \"$sessionId\"}")
            val response = handler.createSession(request)
            assertEquals("Unauthorized, invalid token.", response.bodyString())
        }

    @Test
    fun `unauthorized message due lack of token during add player request`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, DUMMY_ROUTE)
            val response = handler.createSession(request)
            assertEquals("Unauthorized, token not provided.", response.bodyString())
        }

    @Test
    fun `unauthorized status due lack of token during get session request`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, DUMMY_ROUTE)
            val response = handler.getSession(request)
            assertEquals(Status.UNAUTHORIZED, response.status)
        }

    @Test
    fun `unauthorized status due to lack of token during delete session request`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.DELETE, DUMMY_ROUTE)
            val response = handler.deleteSession(request)
            assertEquals(Status.UNAUTHORIZED, response.status)
        }
    }

    @Test
    fun `unsuccessfully delete session due to session id not found`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            // ARRANGE
            val request =
                Request(Method.DELETE, "$DUMMY_ROUTE?&token=${PlayerManagementStunt.playerToken}")
                    .body("{\"sid\": \"50000\"")

            // ACT
            val response = handler.deleteSession(request)

            // ASSERT
            assertEquals(Status.NOT_MODIFIED, response.status)
        }
    }

    @Test
    fun `successfully delete of a player from a session`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            // ARRANGE
            val request =
                Request(Method.DELETE, "$DUMMY_ROUTE?&token=${PlayerManagementStunt.playerToken}")
                    .body("{\"player\": \"1\", \"session\": \"1\"}")

            // ACT
            val response = handler.removePlayerFromSession(request)

            // ASSERT
            assertEquals(Status.OK, response.status)
        }
    }

    @Test
    fun `unsuccessfully delete of a player due to missing parameters`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            // ARRANGE
            val request = Request(Method.DELETE, "$DUMMY_ROUTE?&token=${PlayerManagementStunt.playerToken}")

            // ACT
            val response = handler.removePlayerFromSession(request)

            // ASSERT
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `unsuccessfully delete of a player due to nonexistent pid and sid`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            // ARRANGE
            val request =
                Request(Method.DELETE, "$DUMMY_ROUTE?&token=${PlayerManagementStunt.playerToken}")
                    .body("{\"player\": \"3\", \"session\": \"9\"}")

            // ACT
            val response = handler.removePlayerFromSession(request)

            // ASSERT
            assertEquals(Status.NOT_MODIFIED, response.status)
        }
    }
}
