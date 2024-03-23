package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.storage.PlayerDataMem
import pt.isel.ls.storage.PlayerStorageStuntDummy
import pt.isel.ls.storage.SessionStorageStunt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

private val date1 = LocalDateTime(2024, 3, 10, 12, 30)

class SessionManagementTest {
    private fun actionSessionManagementTest(code: (session: SessionServices) -> Unit) =
        // arrangement
        SessionManagement(SessionStorageStunt(), PlayerDataMem(PlayerStorageStuntDummy()))
            .let(code)

    @Test
    fun `creating a session returns and sid successfully`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val expectedSid = 4u
            val actualSid = sessionManagement.createSession(1u, date1, 2u)
            assertEquals(expectedSid, actualSid)
        }
    }

    @Test
    fun `add a player to a session increments size of session`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val currentCollection = sessionManagement.getSessionDetails(1u)
            val currentSize = currentCollection.players.size
            sessionManagement.addPlayer(2u, 1u)
            val newCollection = sessionManagement.getSessionDetails(1u)
            assertTrue { newCollection.players.size == currentSize.inc() }
        }
    }

    @Test
    fun `error adding a player to a session due to invalid player fails with ServicesError`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            assertFailsWith<ServicesError> { sessionManagement.addPlayer(4u, 1u) }
        }
    }

    @Test
    fun `error adding a player to a session due to invalid session fails with ServicesError`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            assertFailsWith<ServicesError> { sessionManagement.addPlayer(1u, 3u) }
        }
    }

    @Test
    fun `trying to get details of a non-existent session fails with ServicesError`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            assertFailsWith<ServicesError> { sessionManagement.getSessionDetails(5u) }
        }
    }

    @Test
    fun `successfully getSessionDetails gets the correct details`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val sessionDetails = sessionManagement.getSessionDetails(1u)
            assertEquals(1u, sessionDetails.sid)
            assertEquals(2u, sessionDetails.capacity)
            assertEquals(1u, sessionDetails.gid)
            assertEquals(date1, sessionDetails.date)
            assertTrue { sessionDetails.players.size == 2 }
        }
    }

    @Test
    fun `get Session by gameID successfully`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val sessions = sessionManagement.getSessions(1u)
            assertEquals(2, sessions.size)
            assertTrue(sessions.all { session -> session.gid == 1u })
        }
    }

    @Test
    fun `no match in trying to get Sessions throws ServicesError`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            assertFailsWith<ServicesError> { sessionManagement.getSessions(8u) }
        }
    }

    @Test
    fun `get Sessions by date returns successfully`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val date = LocalDateTime(2024, 3, 10, 12, 30)
            val sessions = sessionManagement.getSessions(1u, date)
            assertEquals(2, sessions.size)
            assertTrue(sessions.all { session -> session.date == LocalDateTime(2024, 3, 10, 12, 30) })
        }
    }

    @Test
    fun `get Sessions by state returns successfully`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val closeSessions = sessionManagement.getSessions(1u, state = SessionState.CLOSE)
            assertEquals(1, closeSessions.size)
            val openSessions = sessionManagement.getSessions(1u, state = SessionState.OPEN)
            assertEquals(1, openSessions.size)
        }
    }

    @Test
    fun `get Sessions by player ID successfully`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val sessions = sessionManagement.getSessions(1u, playerId = 1u)
            assertEquals(2, sessions.size)
            assertTrue(sessions.all { session -> session.players.any { player -> player.pid == 1u } })
        }
    }
}
