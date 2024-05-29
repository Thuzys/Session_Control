package pt.isel.ls.webApi

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.routing.RoutedRequest
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
                    DUMMY_ROUTE,
                ).header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
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
                    DUMMY_ROUTE,
                )
                    .body(
                        "{\"gid\": \"1\",\"date\": \"2024-03-16\", " +
                            "\"capacity\": \"10\", \"owner\": \"1\", \"ownerName\": \"test1\"}",
                    )
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
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
                    DUMMY_ROUTE,
                )
                    .body(
                        "{\"gid\": \"1\",\"date\": \"2024-03-16\", " +
                            "\"capacity\": \"10\", \"owner\": \"1\", \"ownerName\": \"test1\"}",
                    )
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
            val response = handler.createSession(request)
            assertEquals(
                createJsonRspMessage(
                    message = "Session created with ID: 1 successfully.",
                    id = 1u,
                ),
                response.bodyString(),
            )
        }
    }

    @Test
    fun `bad request status creating a session due to missing gid`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.POST,
                    DUMMY_ROUTE,
                )
                    .body("{\"date\": \"2024-03-16T12:30:00\", \"capacity\": \"10\"}")
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
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
                    DUMMY_ROUTE,
                )
                    .body("{gid: 1, capacity: 10}")
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
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
                    DUMMY_ROUTE,
                )
                    .body("{gid: 1, date: 2024-03-16}")
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
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
                    DUMMY_ROUTE,
                )
                    .body("{gid: 1, date: invalid, capacity: 10}")
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
            val response = handler.createSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `bad request status getting a session due to missing sid`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/invalid")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}"),
                )
            val response = handler.getSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `not found status getting a session`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/1000")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}"),
                )
            val response = handler.getSession(request)
            assertEquals(Status.NOT_FOUND, response.status)
        }
    }

    @Test
    fun `found status getting a session`() {
        val sid = "1"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/$sid")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}"),
                )
            val response = handler.getSession(request)
            assertEquals(Status.FOUND, response.status)
        }
    }

    @Test
    fun `message of getting a session successfully`() {
        val sid = "1"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/$sid")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}"),
                )

            val response = handler.getSession(request)
            assertEquals(
                expected =
                    "{\"sid\":1,\"capacity\":1,\"gameInfo\":{\"gid\":1,\"name\":\"Game\"}," +
                        "\"date\":\"2024-03-10\",\"owner\":{\"pid\":1,\"username\":\"test1\"}," +
                        "\"players\":[{\"pid\":1,\"username\":\"test1\"}," +
                        "{\"pid\":2,\"username\":\"test2\"}]}",
                actual = response.bodyString(),
            )
        }
    }

    @Test
    fun `bad request status adding a player to a session due to missing parameters`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.PUT, "$DUMMY_ROUTE/invalid/invalid")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}/{pid}"),
                )
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
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
                RoutedRequest(
                    Request(Method.PUT, "$DUMMY_ROUTE/$sessionId/$playerId")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}/{pid}"),
                )
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
            val response = handler.addPlayerToSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `OK status adding a player to a session`() {
        val playerId = "1"
        val sessionId = "1"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.PUT, "$DUMMY_ROUTE/$sessionId/$playerId")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}/{pid}"),
                )
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
                    "$DUMMY_ROUTE?gid=$gid&state=$state",
                )
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
            val response = handler.getSessions(request)
            assertEquals(Status.FOUND, response.status)
        }
    }

    @Test
    fun `bad request status getting sessions due to missing gid`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(Method.GET, DUMMY_ROUTE)
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
            val response = handler.getSessions(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `found status getting sessions heaving with no sessions satisfying the details provided`() {
        val gid = "400"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(Method.GET, "$DUMMY_ROUTE?gid=$gid")
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
            val response = handler.getSessions(request)
            assertEquals(Status.FOUND, response.status)
        }
    }

    @Test
    fun `message of getting sessions successfully`() {
        val gid = "1"
        val state = "close"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.GET,
                    "$DUMMY_ROUTE?gid=$gid&state=$state",
                )
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
            val response = handler.getSessions(request)
            assertEquals(
                expected =
                    "[{\"sid\":1,\"owner\":{\"pid\":1,\"username\":\"test1\"}," +
                        "\"gameInfo\":{\"gid\":1,\"name\":\"Game\"},\"date\":\"2024-03-10\"}," +
                        "{\"sid\":2,\"owner\":{\"pid\":2,\"username\":\"test2\"}," +
                        "\"gameInfo\":{\"gid\":1,\"name\":\"Game\"},\"date\":\"2024-03-10\"}]",
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
            assertEquals(createJsonRspMessage("Unauthorized, token not provided."), response.bodyString())
        }

    @Test
    fun `unauthorized message due lack of token during create session request`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, DUMMY_ROUTE)
            val response = handler.createSession(request)
            assertEquals(createJsonRspMessage("Unauthorized, token not provided."), response.bodyString())
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
                RoutedRequest(
                    Request(Method.PUT, "$DUMMY_ROUTE/$sessionId/$playerId")
                        .header("Authorization", "Bearer ${UUID.randomUUID()}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}/{pid}"),
                )
            val response = handler.createSession(request)
            assertEquals(createJsonRspMessage("Unauthorized, invalid token."), response.bodyString())
        }

    @Test
    fun `unauthorized message due lack of token during add player request`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, DUMMY_ROUTE)
            val response = handler.createSession(request)
            assertEquals(createJsonRspMessage("Unauthorized, token not provided."), response.bodyString())
        }

    @Test
    fun `unauthorized status due lack of token during get session request`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request = Request(Method.GET, DUMMY_ROUTE)
            val response = handler.getSession(request)
            assertEquals(Status.UNAUTHORIZED, response.status)
        }

    @Test
    fun `bad request updating a session by capacity or date due to invalid sid`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.PUT, "$DUMMY_ROUTE/invalid")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
                        .body("{\"sid\": \"dummy\", \"date\": \"2024-03-16T12:30\", \"capacity\": \"10\"}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}"),
                )
            val response = handler.updateCapacityOrDate(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }

    @Test
    fun `bad request updating a session by capacity or date due to lack of sid`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.PUT, "$DUMMY_ROUTE/invalid")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
                        .body("{\"date\":\"2024-03-16T12:30\",\"capacity\":\"10\"}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}"),
                )
            val response = handler.updateCapacityOrDate(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }

    @Test
    fun `invalid date but valid sid and capacity returns OK status response`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.PUT, "$DUMMY_ROUTE/1")
                        .body("{\"date\":\"invalid_date_format\",\"capacity\":\"10\",\"pid\":\"1\"}")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}"),
                )
            val response = handler.updateCapacityOrDate(request)
            assertEquals(Status.OK, response.status)
        }

    @Test
    fun `invalid capacity but valid sid and date returns OK status response`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.PUT, "$DUMMY_ROUTE/1")
                        .body("{\"date\":\"2024-03-16\",\"capacity\":\"invalid_format\",\"pid\":\"1\"}")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}"),
                )
            val response = handler.updateCapacityOrDate(request)
            assertEquals(Status.OK, response.status)
        }

    @Test
    fun `bad request response when trying to update a session by capacity and date due to lack of capacity and date`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.PUT, "$DUMMY_ROUTE/1")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}"),
                )
            val response = handler.updateCapacityOrDate(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }

    @Test
    fun `OK response updating a session by capacity and date with valid parameters`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.PUT, "$DUMMY_ROUTE/1")
                        .body("{\"date\":\"2024-03-16\",\"capacity\":\"10\",\"pid\":\"1\"}")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}"),
                )
            val response = handler.updateCapacityOrDate(request)
            assertEquals(Status.OK, response.status)
        }

    @Test
    fun `OK response updating a session by capacity only`() =
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.PUT, "$DUMMY_ROUTE/1")
                        .body("{\"capacity\":\"10\",\"pid\":\"1\"}")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}"),
                )
            val response = handler.updateCapacityOrDate(request)
            assertEquals(Status.OK, response.status)
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
                RoutedRequest(
                    Request(Method.DELETE, "$DUMMY_ROUTE/5000")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}"),
                )
            // ACT
            val response = handler.deleteSession(request)

            // ASSERT
            assertEquals(Status.FORBIDDEN, response.status)
        }
    }

    @Test
    fun `successfully delete of a player from a session`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            // ARRANGE
            val request =
                RoutedRequest(
                    Request(Method.DELETE, "$DUMMY_ROUTE/1/1")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}/{pid}"),
                )

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
            val request =
                RoutedRequest(
                    Request(Method.DELETE, "$DUMMY_ROUTE/missing/missing")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}/{pid}"),
                )

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
                RoutedRequest(
                    Request(Method.DELETE, "$DUMMY_ROUTE/9/3")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}/{pid}"),
                )
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")

            // ACT
            val response = handler.removePlayerFromSession(request)

            // ASSERT
            assertEquals(Status.FORBIDDEN, response.status)
        }
    }

    @Test
    fun `getting sessions with date should change the underline to colon`() {
        val date = "2024-03-10"
        val state = "open"
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                Request(
                    Method.GET,
                    "$DUMMY_ROUTE?date=$date&state=$state",
                )
                    .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
            val response = handler.getSessions(request)
            assertEquals(
                expected =
                    "[{\"sid\":1,\"owner\":{\"pid\":1,\"username\":\"test1\"}," +
                        "\"gameInfo\":{\"gid\":1,\"name\":\"Game\"},\"date\":\"2024-03-10\"}" +
                        ",{\"sid\":2,\"owner\":{\"pid\":2,\"username\":\"test2\"}," +
                        "\"gameInfo\":{\"gid\":1,\"name\":\"Game\"},\"date\":\"2024-03-10\"}]",
                actual = response.bodyString(),
            )
        }
    }

    @Test
    fun `getPlayerFromSession returns FOUND status`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/1/1")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}/{pid}"),
                )
            val response = handler.getPlayerFromSession(request)
            assertEquals(Status.FOUND, response.status)
        }
    }

    @Test
    fun `getPlayerFromSession returns BAD_REQUEST status due to invalid parameters`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/invalid/invalid")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}/{pid}"),
                )
            val response = handler.getPlayerFromSession(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }
    }

    @Test
    fun `getPlayerFromSession returns the player if found`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/1/1")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}/{pid}"),
                )
            val response = handler.getPlayerFromSession(request)
            assertEquals(
                expected =
                    "{\"pid\":1,\"name\":\"test1\",\"username\":\"test1\",\"email\":" +
                        "\"test1@gmail.com\",\"password\":\"test1\",\"token\":\"568f8e19-4e4c-43a2-a1c9-d416aa39a8b4\"}",
                actual = response.bodyString(),
            )
        }
    }

    @Test
    fun `getPlayerFromSession returns NOT_FOUND status due to player not found`() {
        actionOfASessionArrangement { handler: SessionHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/1/2")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{sid}/{pid}"),
                )
            val response = handler.getPlayerFromSession(request)
            assertEquals(Status.FOUND, response.status)
        }
    }
}
