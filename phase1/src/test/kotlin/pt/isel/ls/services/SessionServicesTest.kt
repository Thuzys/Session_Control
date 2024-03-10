package pt.isel.ls.services

import org.junit.jupiter.api.Test
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
            assertEquals(1u, it.createGame("test", "dev", listOf("genre")))
        }
    }

    @Test
    fun `error creating a game`() {
        makeSessionTest {
            assertFailsWith<IllegalStateException> { it.createGame("test", "dev", listOf()) }
        }
    }

    @Test
    fun `get details of a game`() {
        makeSessionTest {
            assertNotNull(it.getGameDetails(1u))
        }
    }

    @Test
    fun `get details of a non-existent game`() {
        makeSessionTest {
            assertNull(it.getGameDetails(2u))
        }
    }
}
