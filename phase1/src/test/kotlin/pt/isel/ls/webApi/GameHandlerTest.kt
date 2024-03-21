package pt.isel.ls.webApi

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.services.GameManagementStunt
import kotlin.test.Test
import kotlin.test.assertEquals

class GameHandlerTest {
    private fun makeGameHandlerTest(
        request: Request,
        code: GameHandlerInterface.(request: Request) -> Unit,
    ) = GameHandler(GameManagementStunt()).run { code(request) }

    @Test
    fun `bad request creating a game`() =
        makeGameHandlerTest(Request(Method.POST, "/gamesTest")) { request: Request ->
            val response = createGame(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }

    @Test
    fun `message of Bad Request creating a game`() =
        makeGameHandlerTest(Request(Method.POST, "/gamesTest")) { request: Request ->
            val response = createGame(request)
            assertEquals("Bad Request", response.bodyString())
        }

    @Test
    fun `bad request getting the game details`() =
        makeGameHandlerTest(Request(Method.GET, "/gamesTest")) { request: Request ->
            val response = getGameDetails(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }

    @Test
    fun `message of Bad Request getting the game details`() =
        makeGameHandlerTest(Request(Method.GET, "/gamesTest")) { request: Request ->
            val response = getGameDetails(request)
            assertEquals("Bad Request", response.bodyString())
        }

    @Test
    fun `game created with success`() =
        makeGameHandlerTest(
            request = Request(Method.POST, "/gameTest").body("{name: name, dev: dev, genres: genre1,genre2,genre3}"),
        ) { request: Request ->
            val response = createGame(request)
            assertEquals(Status.CREATED, response.status)
        }

    @Test
    fun `message of game created with success`() =
        makeGameHandlerTest(
            request = Request(Method.POST, "/gameTest").body("{name: name, dev: dev, genres: genre1,genre2,genre3}"),
        ) { request: Request ->
            val response = createGame(request)
            assertEquals(
                expected = "Game created with id 1.",
                actual = response.bodyString(),
            )
        }

    @Test
    fun `internal server error creating a game`() =
        makeGameHandlerTest(
            request = Request(Method.POST, "/gameTest").body("{name: , dev: dev, genres: genre1,genre2,genre3}"),
        ) { request: Request ->
            val response = createGame(request)
            assertEquals(Status.INTERNAL_SERVER_ERROR, response.status)
        }

    @Test
    fun `bad request getting the game by dev and genres`() =
        makeGameHandlerTest(Request(Method.GET, "/gamesTest")) { request: Request ->
            val response = getGameByDevAndGenres(request)
            assertEquals(Status.BAD_REQUEST, response.status)
        }

    @Test
    fun `message of Bad Request getting the game by dev and genres`() =
        makeGameHandlerTest(Request(Method.GET, "/gamesTest")) { request: Request ->
            val response = getGameByDevAndGenres(request)
            assertEquals("Bad Request", response.bodyString())
        }
/*
    @Test
    fun `game by dev and genres found`() =
        makeGameHandlerTest(
            request = Request(Method.GET, "/gameTest?dev=TestDev&genres=TestGenre"),
        ) { request: Request ->
            val response = getGameByDevAndGenres(request)
            assertEquals(Status.FOUND, response.status)
        }

    @Test
    fun `message of game by dev and genres not found`() =
        makeGameHandlerTest(
            request = Request(Method.GET, "/gameTest?dev=TestDev2&genres=TestGenre"),
        ) { request: Request ->
            val response = getGameByDevAndGenres(request)
            assertEquals("Game not found.", response.bodyString())
        }

@Test
fun `message of game by dev and genres found`() =
    makeGameHandlerTest(
        request = Request(Method.GET, "/gameTest?dev=TestDev&genres=TestGenre"),
    ) { request: Request ->
        val response = getGameByDevAndGenres(request)
        assertEquals(
            expected = "[Game(id=1, name=Test, dev=TestDev, genres=[TestGenre])]",
            actual = response.bodyString()
        )
    }

    @Test
    fun `game by dev and genres not found`() =
        makeGameHandlerTest(
            request = Request(Method.GET, "/gameTest?dev=TestDev2&genres=TestGenre"),
        ) { request: Request ->
            val response = getGameByDevAndGenres(request)
            assertEquals(Status.NOT_FOUND, response.status)
        }


    @Test
    fun `message of game not found`() =
        makeGameHandlerTest(Request(Method.GET, "/gameTest?")) { request: Request ->
            val response = getGameDetails(request)
            assertEquals("Game not found.", response.bodyString())
        }
 */
}
