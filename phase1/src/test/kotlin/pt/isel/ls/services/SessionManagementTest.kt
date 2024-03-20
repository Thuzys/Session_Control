package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.storage.PlayerDataMem
import pt.isel.ls.storage.PlayerStorageStunt
import pt.isel.ls.storage.SessionDataMem
import pt.isel.ls.storage.SessionStorageStunt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SessionManagementTest {
    private fun makeSessionTest(code: (session: SessionManagement) -> Unit) {
        SessionManagement(SessionDataMem(SessionStorageStunt()), PlayerDataMem(PlayerStorageStunt())).run {
            code(this)
        }
    }

    @Test
    fun `add a player to a session`() {
        makeSessionTest {
            val currentCollection = it.getSessionDetails(1u)
            val currentSize = currentCollection.players.size
            it.addPlayer(2u, 1u)
            val newCollection = it.getSessionDetails(1u)
            assertTrue { newCollection.players.size == currentSize.inc() }
        }
    }

    @Test
    fun `error adding a player to a session (invalid player)`() {
        makeSessionTest {
            assertFailsWith<ServicesError> { it.addPlayer(4u, 1u) }
        }
    }

    @Test
    fun `error adding a player to a session (invalid session)`() {
        makeSessionTest {
            assertFailsWith<ServicesError> { it.addPlayer(1u, 3u) }
        }
    }

    @Test
    fun `trying to get details of a non-existent session`() {
        makeSessionTest {
            assertFailsWith<ServicesError> { it.getSessionDetails(5u) }
        }
    }

    @Test
    fun `get details of a session`() {
        makeSessionTest {
            val date = LocalDateTime(2024, 3, 10, 12, 30)
            val sessionDetails = it.getSessionDetails(1u)
            assertEquals(1u, sessionDetails.sid)
            assertEquals(2u, sessionDetails.capacity)
            assertEquals(1u, sessionDetails.gid)
            assertEquals(date, sessionDetails.date)
            assertTrue { sessionDetails.players.size == 2 }
        }
    }

    @Test
    fun `get Session by gameID`() {
        makeSessionTest {
            val sessions = it.getSessions(1u)
            assertEquals(2, sessions.size)
            assertTrue(sessions.all { session -> session.gid == 1u })
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
            val date = LocalDateTime(2024, 3, 10, 12, 30)
            val sessions = it.getSessions(1u, date)
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
            assertTrue(sessions.all { session -> session.players.any { player -> player.pid == 1u } })
        }
    }
}
