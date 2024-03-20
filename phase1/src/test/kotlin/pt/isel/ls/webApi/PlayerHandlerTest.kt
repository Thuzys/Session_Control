package pt.isel.ls.webApi

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.services.PlayerManagementStunt
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayerHandlerTest {
    private fun createPlayerHandler(
        request: Request,
        code: PlayerHandlerInterface.(request: Request) -> Unit,
    ) = PlayerHandler(PlayerManagementStunt).run { code(request) }

    @Test
    fun `bad request creating a player`() =
        createPlayerHandler(Request(Method.POST, "/playersTest")) { request: Request ->
            val response = createPlayer(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }

    @Test
    fun `message of Bad Request creating a player`() =
        createPlayerHandler(Request(Method.POST, "/playersTest")) { request: Request ->
            val response = createPlayer(request)
            assertEquals("Bad Request", response.bodyString())
        }

    @Test
    fun `bad request getting a player`() =
        createPlayerHandler(Request(Method.GET, "/playersTest")) { request: Request ->
            val response = getPlayer(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }

    @Test
    fun `message of Bad Request getting a player`() =
        createPlayerHandler(Request(Method.GET, "/playersTest")) { request: Request ->
            val response = getPlayer(request)
            assertEquals("Bad Request", response.bodyString())
        }

    @Test
    fun `player created`() =
        createPlayerHandler(
            request = Request(Method.POST, "/playerTest").body("{name: name, email: email}"),
        ) { request: Request ->
            val response = createPlayer(request)
            assertEquals(Status.CREATED, response.status)
        }

    @Test
    fun `message of player created`() =
        createPlayerHandler(
            request = Request(Method.POST, "/playerTest").body("{name: name, email: email}"),
        ) { request: Request ->
            val response = createPlayer(request)
            assertEquals(
                expected =
                    "Player created with id ${PlayerManagementStunt.playerId} " +
                        "and token ${PlayerManagementStunt.playerToken}.",
                actual = response.bodyString(),
            )
        }

    @Test
    fun `internal server error creating a player`() =
        createPlayerHandler(
            request = Request(Method.POST, "/playerTest").body("{name: name, email: }"),
        ) { request: Request ->
            val response = createPlayer(request)
            assertEquals(Status.INTERNAL_SERVER_ERROR, response.status)
        }

    @Test
    fun `player not found`() =
        createPlayerHandler(
            request = Request(Method.GET, "/playerTest?pid=0"),
        ) { request: Request ->
            val response = getPlayer(request)
            assertEquals(Status.NOT_FOUND, response.status)
        }

    @Test
    fun `message of player not found`() =
        createPlayerHandler(
            request = Request(Method.GET, "/playerTest?pid=0"),
        ) { request: Request ->
            val response = getPlayer(request)
            assertEquals(
                expected = "Unable to get the details of a Player due to invalid pid.",
                actual = response.bodyString(),
            )
        }

    @Test
    fun `player found`() =
        createPlayerHandler(
            request = Request(Method.GET, "/playerTest?pid=${PlayerManagementStunt.playerId}"),
        ) { request: Request ->
            val response = getPlayer(request)
            assertEquals(Status.FOUND, response.status)
        }

    @Test
    fun `message of player found`() =
        createPlayerHandler(
            request = Request(Method.GET, "/playerTest?pid=${PlayerManagementStunt.playerId}"),
        ) { request: Request ->
            val response = getPlayer(request)
            assertEquals(
                expected =
                    "{\"pid\":${PlayerManagementStunt.playerId},\"name\":\"Test\",\"email\":" +
                        "{\"email\":\"${PlayerManagementStunt.playerEmail.email}\"},\"token\"" +
                        ":\"${PlayerManagementStunt.playerToken}\"}",
                actual = response.bodyString(),
            )
        }
}
