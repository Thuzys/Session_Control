package pt.isel.ls.storage

import pt.isel.ls.utils.DEFAULT_LIMIT
import pt.isel.ls.utils.DEFAULT_OFFSET
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GameDataMemTest {
    private fun makeGameDataMemTest(code: (gameDataMem: GameDataInterface) -> Unit) {
        GameDataMem(GameStorageStunt()).run { code(this) }
    }

    @Test
    fun `create a new Game`() {
        makeGameDataMemTest {
            assertEquals(4u, it.createGame("test", "dev", setOf("genre")))
            assertEquals(5u, it.createGame("test", "dev", setOf("genre")))
        }
    }

    @Test
    fun `error creating a game`() {
        makeGameDataMemTest {
            assertFailsWith<IllegalArgumentException> { it.createGame("test", "dev", setOf()) }
        }
    }

    @Test
    fun `get details of a game`() {
        makeGameDataMemTest {
            val game = it.getGameDetails(1u, DEFAULT_OFFSET, DEFAULT_LIMIT)
            assertEquals("test", game.name)
            assertEquals("dev", game.dev)
            assertEquals(setOf("genre"), game.genres)
        }
    }

    @Test
    fun `error getting details of a game`() {
        makeGameDataMemTest {
            assertFailsWith<NoSuchElementException> { it.getGameDetails(60u, DEFAULT_OFFSET, DEFAULT_LIMIT) }
        }
    }

    @Test
    fun `get Game by developer and genres`() {
        makeGameDataMemTest {
            val games =
                it.getGameByDevAndGenres(DEFAULT_OFFSET, DEFAULT_LIMIT) { games ->
                    games.filter { game ->
                        game.dev == "dev" && game.genres.containsAll(setOf("genre"))
                    }
                }
            assertEquals(2, games.size)
        }
    }

    @Test
    fun `get Game by developer and genres with no results`() {
        makeGameDataMemTest {
            val games =
                it.getGameByDevAndGenres(DEFAULT_OFFSET, DEFAULT_LIMIT) { games ->
                    games.filter { game ->
                        game.dev == "dev" && game.genres.containsAll(setOf("genre1"))
                    }
                }
            assertEquals(0, games.size)
        }
    }
}
