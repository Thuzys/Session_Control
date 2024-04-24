package pt.isel.ls.webApi

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.services.GameManagementStunt
import pt.isel.ls.services.PlayerManagementStunt
import kotlin.test.Test
import kotlin.test.assertEquals

private const val DUMMY_ROUTE = "/dummyRoute"

class GameHandlerTest {
    private fun executeGameHandlerTest(action: (GameHandlerInterface) -> Response) =
        GameHandler(GameManagementStunt, PlayerManagementStunt)
            .run(action)

    @Test
    fun `bad request creating a game due to missing parameters (name, developer and genres)`() {
        // ARRANGE
        val request = Request(Method.POST, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}")
        val expectedStatus = Status.BAD_REQUEST

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.createGame(request)
            }

        // ASSERT
        assertEquals(expectedStatus, response.status)
    }

    @Test
    fun `message of Bad Request creating a game due to missing parameters (name, developer and genres)`() {
        // ARRANGE
        val request = Request(Method.POST, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}")
        val expectedMessage = "Bad Request."

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.createGame(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }

    @Test
    fun `bad request getting the game details due to missing game id`() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}")
        val expectedStatus = Status.BAD_REQUEST

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGameDetails(request)
            }

        // ASSERT
        assertEquals(expectedStatus, response.status)
    }

    @Test
    fun `message of Bad Request getting the game details due to missing game id`() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}")
        val expectedMessage = "Bad Request."

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGameDetails(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }

    @Test
    fun `game created with success`() {
        // ARRANGE
        val request =
            Request(
                method = Method.POST,
                uri = "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}",
            ).body("{\"name\": \"name\", \"dev\": \"dev\", \"genres\": \"genre1,genre2,genre3\"")

        val expectedStatus = Status.CREATED

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.createGame(request)
            }

        // ASSERT
        assertEquals(expectedStatus, response.status)
    }

    @Test
    fun `message of game created with success`() {
        // ARRANGE
        val request =
            Request(
                method = Method.POST,
                uri = "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}",
            ).body("{\"name\": \"name\", \"dev\": \"dev\", \"genres\": \"genre1,genre2,genre3\"")
        val expectedMessage = "Game created with id 1."

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.createGame(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }

    @Test
    fun `internal server error creating a game due to invalid parameter`() {
        // ARRANGE
        val request =
            Request(
                method = Method.POST,
                uri = "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}",
            ).body("{\"name\": \"\", \"dev\": \"dev\", \"genres\": \"genre1,genre2,genre3\"}")
        val expectedStatus = Status.INTERNAL_SERVER_ERROR

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.createGame(request)
            }

        // ASSERT
        assertEquals(expectedStatus, response.status)
    }

    @Test
    fun `bad request getting the game by dev and genres due to missing developer and genres`() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}")
        val expectedStatus = Status.BAD_REQUEST

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGameByDevAndGenres(request)
            }

        // ASSERT
        assertEquals(expectedStatus, response.status)
    }

    @Test
    fun `message of Bad Request getting the game by dev and genres due to missing developer and genres`() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}")
        val expectedMessage = "Bad Request."

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGameByDevAndGenres(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }

    @Test
    fun `game found by dev and genres successfully`() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}&dev=TestDev&genres=TestGenre")
        val expectedStatus = Status.FOUND

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGameByDevAndGenres(request)
            }

        // ASSERT
        assertEquals(expectedStatus, response.status)
    }

    @Test
    fun `message of game by dev and genres not found due to parameters that not correspond to an existing Game`() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}&dev=TestDev2&genres=TestGenre")
        val expectedMessage =
            """
            Error:Game not found.
            Cause:Unable to find the game due to invalid dev or genres.
            """.trimIndent()

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGameByDevAndGenres(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }

    @Test
    fun `message of game by dev and genres found`() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}&dev=TestDev&genres=TestGenre")
        val expectedMessage = "[{\"gid\":1,\"name\":\"Test\",\"dev\":\"TestDev\",\"genres\":[\"TestGenre\"]}]"

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGameByDevAndGenres(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }

    @Test
    fun `game by dev and genres not found due to parameters that not correspond to an existing Game`() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}&dev=TestDev2&genres=TestGenre")
        val expectedStatus = Status.NOT_FOUND

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGameByDevAndGenres(request)
            }

        // ASSERT
        assertEquals(expectedStatus, response.status)
    }

    @Test
    fun `message of game not found due to a game id that does not correspond to an existing Game`() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}&gid=34")
        val expectedMessage =
            """
            Error:Game not found.
            Cause:Unable to find the game due to invalid game id.
            """.trimIndent()

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGameDetails(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }

    @Test
    fun `message of game found due to a game id that corresponds to an existing Game `() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?token=${PlayerManagementStunt.playerToken}&gid=1")
        val expectedMessage = "{\"gid\":1,\"name\":\"Test\",\"dev\":\"TestDev\",\"genres\":[\"TestGenre\"]}"

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGameDetails(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }

    @Test
    fun `game found by player successfully`() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?pid=1&token=${PlayerManagementStunt.playerToken}")
        val expectedStatus = Status.FOUND

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGamesByPlayer(request)
            }

        // ASSERT
        assertEquals(expectedStatus, response.status)
    }

    @Test
    fun `message of game not found by player due to a player id that does not correspond to an existing Player`() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?pid=34&token=${PlayerManagementStunt.playerToken}")
        val expectedMessage =
            """
            Error:Game not found.
            Cause:Unable to find the game due to invalid player id.
            """.trimIndent()

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGamesByPlayer(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }

    @Test
    fun `message of game found by player`() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?pid=1&token=${PlayerManagementStunt.playerToken}")
        val expectedMessage = "[{\"gid\":1,\"name\":\"Test\",\"dev\":\"TestDev\",\"genres\":[\"TestGenre\"]}]"

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGamesByPlayer(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }

    @Test
    fun `game not found by player due to a player id that does not correspond to an existing Player`() {
        // ARRANGE
        val request = Request(Method.GET, "$DUMMY_ROUTE?pid=34&token=${PlayerManagementStunt.playerToken}")
        val expectedStatus = Status.NOT_FOUND

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGamesByPlayer(request)
            }

        // ASSERT
        assertEquals(expectedStatus, response.status)
    }
}
