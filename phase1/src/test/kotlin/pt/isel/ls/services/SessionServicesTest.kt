package pt.isel.ls.services

import org.junit.jupiter.api.Test
import pt.isel.ls.storage.StorageStunt
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SessionServicesTest {
    private fun makeSessionTest(code: (session: SessionServices) -> Unit) = SessionServices(StorageStunt()).run { code }

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
}
