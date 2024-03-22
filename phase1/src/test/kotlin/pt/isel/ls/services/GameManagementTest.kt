package pt.isel.ls.services

import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.storage.GameDataMem
import pt.isel.ls.storage.GameStorageStunt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GameManagementTest {
    private fun makeGameManagementTest(code: (gameServices: GameServices) -> Unit) {
        GameManagement(GameDataMem(GameStorageStunt())).run { code(this) }
    }

    @Test
    fun `create a new Game`() {
        makeGameManagementTest {
            assertEquals(4u, it.createGame("test", "dev", setOf("genre")))
            assertEquals(5u, it.createGame("test", "dev", setOf("genre")))
        }
    }

    @Test
    fun `error creating a game`() {
        makeGameManagementTest {
            assertFailsWith<ServicesError> { it.createGame("test", "dev", setOf()) }
        }
    }

    @Test
    fun `get details of a game`() {
        makeGameManagementTest {
            val game = it.getGameDetails(1u, null, null)
            assertEquals("test", game.name)
            assertEquals("dev", game.dev)
            assertTrue { "genre" in game.genres }
        }
    }

    @Test
    fun `error getting details of a game`() {
        makeGameManagementTest {
            assertFailsWith<ServicesError> { it.getGameDetails(60u, null, null) }
        }
    }

    @Test
    fun `get Game by developer and genres`() {
        makeGameManagementTest {
            val games = it.getGameByDevAndGenres(dev = "dev", genres = setOf("genre"), null, null)
            assertEquals(2, games.size)
        }
    }

    @Test
    fun `get Game by developer and genres with no results`() {
        makeGameManagementTest {
            val games = it.getGameByDevAndGenres(dev = "dev", genres = setOf("genre2"), null, null)
            assertEquals(0, games.size)
        }
    }
}
