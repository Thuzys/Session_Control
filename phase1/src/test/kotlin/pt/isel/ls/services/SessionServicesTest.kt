package pt.isel.ls.services

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Test
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.storage.SessionDataMem
import pt.isel.ls.storage.StorageStunt

class SessionServicesTest {
    private fun makeSessionTest(code: (session: SessionServices) -> Unit) {
        SessionServices(SessionDataMem(StorageStunt())).run { code(this) }
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
    fun `add a player to a session`() {
        makeSessionTest {
            val currentCollection = it.getSessionDetails(1u) as Session
            val currentSize = currentCollection.players.size
            it.addPlayer(2u, 1u)
            assertTrue { currentCollection.players.size == currentSize.inc() }
        }
    }

    @Test
    fun `error adding a player to a session (invalid player)`() {
        makeSessionTest {
            assertFailsWith<IllegalArgumentException> { it.addPlayer(4u, 1u) }
        }
    }

    @Test
    fun `error adding a player to a session (invalid session)`() {
        makeSessionTest {
            assertFailsWith<IllegalArgumentException> { it.addPlayer(1u, 3u) }
        }
    }

    @Test
    fun `trying to get details of a non existent session`() {
        makeSessionTest {
            assertFailsWith<IllegalArgumentException> { it.getSessionDetails(5u) }
        }
    }

    @Test
    fun `get details of a session`() {
        makeSessionTest {
            val player = Player(1u, "test1", Email("xpto@gmail.com"))
            val date = LocalDateTime(2024, 3, 10, 12, 30)
            val players: Collection<Player> = listOf(player)
            val sessionDetails = it.getSessionDetails(1u) as Session
            assertEquals(1u, sessionDetails.uuid)
            assertEquals(2u, sessionDetails.capacity)
            assertEquals(1u, sessionDetails.gid)
            assertEquals(date, sessionDetails.date)
            assertEquals(players, sessionDetails.players)
        }
    }

    @Test
    fun `get Session by gameID`() {
        makeSessionTest {
            val sessions = it.getSessions(1u)
            assertEquals(2, sessions.size)
            assertTrue(sessions.all { session -> session.gid == 2u })
        }
    }

    @Test
    fun `no match in trying to get Sessions`() {
        makeSessionTest {
            val sessions = it.getSessions(8u)
            assertEquals(0, sessions.size)
        }
    }

    @Test
    fun `get Sessions by date`() {
        makeSessionTest {
            val sessions = it.getSessions(1u, date = LocalDateTime(2024, 3, 10, 12, 30))
            assertEquals(2, sessions.size)
            assertTrue(sessions.all { session -> session.date == LocalDateTime(2024, 3, 10, 12, 30) })
        }
    }

    @Test
    fun `get Sessions by state`() {
        makeSessionTest {
            val closeSessions = it.getSessions(1u, state = SessionState.CLOSE)
            assertEquals(1, closeSessions.size)
            val openSessions = it.getSessions(1u, state = SessionState.OPEN)
            assertEquals(1, openSessions.size)
        }
    }

    @Test
    fun `get Sessions by player ID`() {
        makeSessionTest {
            val sessions = it.getSessions(1u, playerId = 1u)
            assertEquals(2, sessions.size)
            assertTrue(sessions.all { session -> session.players.any { player -> player.uuid == 1u } })
        }
    }
}
