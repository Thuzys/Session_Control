package pt.isel.ls.webApi

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.routing.RoutedRequest
import pt.isel.ls.services.PlayerManagementStunt
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

private const val DUMMY_ROUTE = "/dummyRoute"

class PlayerHandlerTest {
    private inline fun actionOfAPlayerArrangement(act: (PlayerHandlerInterface) -> Unit) =
        // arrangement
        PlayerHandler(PlayerManagementStunt)
            .let(act)

    @Test
    fun `status of bad request creating a player due lack of name and email`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            val request = Request(Method.POST, DUMMY_ROUTE)
            val response = handler.createPlayer(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }

    @Test
    fun `message of Bad Request creating a player due lack of name and email`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            val request = Request(Method.POST, DUMMY_ROUTE)
            val response = handler.createPlayer(request)
            assertEquals("Bad Request, insufficient parameters.", response.bodyString())
        }

    @Test
    fun `status of bad request getting a player due lack of pid`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/invalid")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{pid}"),
                )
            val response = handler.getPlayer(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }

    @Test
    fun `message of Bad Request getting a player due lack of pid`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/invalid")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{pid}"),
                )
            val response = handler.getPlayer(request)
            assertEquals("Bad Request, pid not found.", response.bodyString())
        }

    @Test
    fun `status of a player created successfully`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            val request = Request(Method.POST, DUMMY_ROUTE).body("{\"name\": \"name\", \"email\": \"email\"}")
            val response = handler.createPlayer(request)
            assertEquals(Status.CREATED, response.status)
        }

    @Test
    fun `message of a player created successfully`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            val request = Request(Method.POST, DUMMY_ROUTE).body("{\"name\": \"name\", \"email\": \"email\"}")
            val response = handler.createPlayer(request)
            assertEquals(
                expected =
                    "Player created with id ${PlayerManagementStunt.playerId} " +
                        "and token ${PlayerManagementStunt.playerToken}.",
                actual = response.bodyString(),
            )
        }

    @Test
    fun `status of bad request creating a player due invalid email provided`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            // a space represents an invalid email
            val request = Request(Method.POST, DUMMY_ROUTE).body("{\"name\": \"name\", \"email\": \" \"}")
            val response = handler.createPlayer(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }

    @Test
    fun `message of bad request creating a player due invalid email provided`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            // a space represents an invalid email
            val request = Request(Method.POST, DUMMY_ROUTE).body("{\"name\": \"name\", \"email\": \" \"}")
            val response = handler.createPlayer(request)
            val expected =
                """
                Error:Unable to create player.
                Cause:Unable to create a new Player due to invalid name or email.
                """.trimIndent()
            assertEquals(
                expected = expected,
                actual = response.bodyString(),
            )
        }

    @Test
    fun `status of player not found due nonexistent pid`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/1000")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{pid}"),
                )
            val response = handler.getPlayer(request)
            assertEquals(Status.NOT_FOUND, response.status)
        }

    @Test
    fun `message of player not found due nonexistent pid`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/1000")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{pid}"),
                )
            val response = handler.getPlayer(request)
            val expected =
                """
                Error:Player not found.
                Cause:Unable to get the details of a Player due to nonexistent pid.
                """.trimIndent()
            assertEquals(
                expected = expected,
                actual = response.bodyString(),
            )
        }

    @Test
    fun `status of player found successfully`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/${PlayerManagementStunt.playerId}")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{pid}"),
                )
            val response = handler.getPlayer(request)
            assertEquals(Status.FOUND, response.status)
        }

    @Test
    fun `message of player found successfully`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/${PlayerManagementStunt.playerId}")
                        .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}"),
                    UriTemplate.from("$DUMMY_ROUTE/{pid}"),
                )
            val response = handler.getPlayer(request)
            assertEquals(
                expected =
                    "{\"pid\":${PlayerManagementStunt.playerId},\"name\":\"Test\",\"userName\":\"Test\",\"email\":" +
                        "\"${PlayerManagementStunt.playerEmail.email}\",\"token\"" +
                        ":\"${PlayerManagementStunt.playerToken}\"}",
                actual = response.bodyString(),
            )
        }

    @Test
    fun `unauthorized status due lack of token during get player request`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            val request = Request(Method.GET, "$DUMMY_ROUTE?pid=${PlayerManagementStunt.playerId}")
            val response = handler.getPlayer(request)
            assertEquals(Status.UNAUTHORIZED, response.status)
        }

    @Test
    fun `unauthorized message due invalid token during get player request`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            val request =
                RoutedRequest(
                    Request(Method.GET, "$DUMMY_ROUTE/${PlayerManagementStunt.playerId}")
                        .header("Authorization", "Bearer ${UUID.randomUUID()}"),
                    UriTemplate.from("$DUMMY_ROUTE/{pid}"),
                )
            val response = handler.getPlayer(request)
            assertEquals("Unauthorized, invalid token.", response.bodyString())
        }

    @Test
    fun `unauthorized message due lack of token during get player request`() =
        actionOfAPlayerArrangement { handler: PlayerHandlerInterface ->
            val request = Request(Method.GET, "$DUMMY_ROUTE?pid=${PlayerManagementStunt.playerId}")
            val response = handler.getPlayer(request)
            assertEquals("Unauthorized, token not provided.", response.bodyString())
        }
}
