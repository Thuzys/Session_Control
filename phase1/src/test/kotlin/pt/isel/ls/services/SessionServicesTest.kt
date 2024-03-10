package pt.isel.ls.services

import org.junit.jupiter.api.Test
import pt.isel.ls.domain.Game
import pt.isel.ls.storage.SessionDataMem
import pt.isel.ls.storage.StorageStunt
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SessionServicesTest {
    private fun makeSessionTest(code: (session: SessionServices) -> Unit) {
        SessionServices(SessionDataMem(StorageStunt())).run { code }
    }

    @Test
    fun `create a new Player`() {
        makeSessionTest {
            assertEquals(3u, it.createPlayer("test3", "email@t.com"))
        }
    }

    @Test
    fun `get details of a player`() {
        makeSessionTest {
            assertNotNull(it.getPlayerDetails(2u))
        }
    }

    @Test
    fun `get details of a non-existent player`() {
        makeSessionTest {
            assertNull(it.getPlayerDetails(3u))
        }
    }

    @Test
    fun `error creating a player`() {
        makeSessionTest {
            assertFailsWith<IllegalStateException> { it.createPlayer("test", "badEmail") }
        }
    }

    @Test
    fun `create a new Game`() {
        makeSessionTest {
            assertEquals(4u, it.createGame("test", "dev", setOf("genre")))
        }
    }

    @Test
    fun `error creating a game`() {
        makeSessionTest {
            assertFailsWith<IllegalStateException> { it.createGame("test", "dev", setOf()) }
        }
    }

    @Test
    fun `get details of a game`() {
        makeSessionTest {
            val game = it.getGameDetails(1u) as Game
            assertEquals("test", game.name)
            assertEquals("dev", game.dev)
            assertEquals(setOf("genre"), game.genres)
        }
    }

    @Test
    fun `error getting details of a game`() {
        makeSessionTest {
            assertFailsWith<IllegalStateException> { it.getGameDetails(60u) }
        }
    }

    @Test
    fun `get Game by developer and genres`() {
        makeSessionTest {
            val games = it.searchGameByDevAndGenres("dev", setOf("genre"))
            assertEquals(2, games.size)
        }
    }

    @Test
    fun `get Game by developer and genres with no results`() {
        makeSessionTest {
            val games = it.searchGameByDevAndGenres("dev", setOf("genre2"))
            assertEquals(0, games.size)
        }
    }
}
