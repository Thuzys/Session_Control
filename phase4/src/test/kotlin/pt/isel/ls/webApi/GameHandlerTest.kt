package pt.isel.ls.webApi

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.routing.RoutedRequest
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
        val request =
            Request(Method.POST, DUMMY_ROUTE)
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
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
        val request =
            Request(Method.POST, DUMMY_ROUTE)
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
        val expectedMessage = createJsonRspMessage("Bad Request, Missing arguments: name:null, dev:null, genres:null.")

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
        val request =
            RoutedRequest(
                Request(Method.GET, "$DUMMY_ROUTE/INVALID"),
                UriTemplate.from("$DUMMY_ROUTE/{gid}"),
            )
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
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
        val request =
            RoutedRequest(
                Request(Method.GET, "$DUMMY_ROUTE/INVALID"),
                UriTemplate.from("$DUMMY_ROUTE/{gid}"),
            )
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
        val expectedMessage = createJsonRspMessage("Bad Request, Invalid arguments: gid must be provided.")

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
                uri = DUMMY_ROUTE,
            ).body("{\"name\": \"name\", \"dev\": \"dev\", \"genres\": \"genre1,genre2,genre3\"")
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")

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
                uri = DUMMY_ROUTE,
            )
                .body("{\"name\": \"name\", \"dev\": \"dev\", \"genres\": \"genre1,genre2,genre3\"")
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")

        val expectedMessage =
            createJsonRspMessage(
                message = "Game created with id 1.",
                id = 1u,
            )

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
                uri = DUMMY_ROUTE,
            ).body("{\"name\": \"\", \"dev\": \"dev\", \"genres\": \"genre1,genre2,genre3\"}")
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")

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
    fun `bad request getting the game by dev and genres due to missing developer and genres`() {
        // ARRANGE
        val request =
            Request(Method.GET, DUMMY_ROUTE)
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
        val expectedStatus = Status.BAD_REQUEST

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGames(request)
            }

        // ASSERT
        assertEquals(expectedStatus, response.status)
    }

    @Test
    fun `message of Bad Request getting the game by dev and genres due to missing developer and genres`() {
        // ARRANGE
        val request =
            Request(Method.GET, DUMMY_ROUTE)
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
        val expectedMessage =
            createJsonRspMessage(
                "Bad Request, Invalid arguments: at least one of the following must be provided: dev, genres, name.",
            )

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGames(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }

    @Test
    fun `game found by dev and genres successfully`() {
        // ARRANGE
        val request =
            Request(Method.GET, "$DUMMY_ROUTE?dev=TestDev&genres=TestGenre")
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
        val expectedStatus = Status.FOUND

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGames(request)
            }

        // ASSERT
        assertEquals(expectedStatus, response.status)
    }

    @Test
    fun `message of game by dev and genres not found due to parameters that not correspond to an existing Game`() {
        // ARRANGE
        val request =
            Request(Method.GET, "$DUMMY_ROUTE?dev=TestDev2&genres=TestGenre")
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
        val expectedMessage =
            createJsonRspMessage(
                "An error occurred while retrieving games.",
                "Unable to find the game due to invalid dev or genres.",
            )

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGames(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }

    @Test
    fun `message of game by dev and genres found`() {
        // ARRANGE
        val request =
            Request(Method.GET, "$DUMMY_ROUTE?dev=TestDev&genres=TestGenre")
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
        val expectedMessage = "[{\"gid\":1,\"name\":\"Test\",\"dev\":\"TestDev\",\"genres\":[\"TestGenre\"]}]"

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGames(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }

    @Test
    fun `game by dev and genres not found due to parameters that not correspond to an existing Game`() {
        // ARRANGE
        val request =
            Request(Method.GET, "$DUMMY_ROUTE?dev=TestDev2&genres=TestGenre")
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
        val expectedStatus = Status.NOT_FOUND

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getGames(request)
            }

        // ASSERT
        assertEquals(expectedStatus, response.status)
    }

    @Test
    fun `message of game not found due to a game id that does not correspond to an existing Game`() {
        // ARRANGE
        val request =
            RoutedRequest(
                Request(Method.GET, "$DUMMY_ROUTE/34"),
                UriTemplate.from("$DUMMY_ROUTE/{gid}"),
            )
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
        val expectedMessage =
            createJsonRspMessage(
                message = "Game not found.",
                error = "Unable to find the game due to invalid game id.",
            )

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
        val request =
            RoutedRequest(
                Request(Method.GET, "$DUMMY_ROUTE/1"),
                UriTemplate.from("$DUMMY_ROUTE/{gid}"),
            )
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
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
    fun `all genres found status`() {
        // ARRANGE
        val request = Request(Method.GET, DUMMY_ROUTE)
        val expectedStatus = Status.FOUND

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getAllGenres(request)
            }

        // ASSERT
        assertEquals(expectedStatus, response.status)
    }

    @Test
    fun `all genres found message`() {
        // ARRANGE
        val request =
            Request(Method.GET, DUMMY_ROUTE)
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")
        val expectedMessage = "[\"TestGenre\"]"

        // ACT
        val response =
            executeGameHandlerTest { handler ->
                handler.getAllGenres(request)
            }

        // ASSERT
        assertEquals(expectedMessage, response.bodyString())
    }
}
