package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.storage.PlayerStorageStunt
import pt.isel.ls.storage.SessionStorageStunt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

private val date1 = LocalDateTime(2024, 3, 10, 12, 30)
private val date2 = LocalDateTime(1904, 3, 10, 12, 30)

class SessionManagementTest {
    private fun actionSessionManagementTest(code: (session: SessionServices) -> Unit) =
        // arrangement
        SessionManagement(SessionStorageStunt(), PlayerStorageStunt())
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
    fun `add a player to a session increments size of session players`() {
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

    @Test
    fun `updating session capacity and date successfully updates both`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val oldSession = sessionManagement.getSessionDetails(1u)
            sessionManagement.updateCapacityOrDate(1u, 10u, date2)
            val updatedSession = sessionManagement.getSessionDetails(1u)
            assertTrue { oldSession.date != updatedSession.date }
            assertTrue { oldSession.capacity != updatedSession.capacity }
            assertEquals(10u, updatedSession.capacity)
            assertEquals(date2, updatedSession.date)
        }
    }

    @Test
    fun `remove a player from a session decrements size of session player list`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val currentCollection = sessionManagement.getSessionDetails(1u)
            val currentSize = currentCollection.players.size
            sessionManagement.removePlayer(1u, 1u)
            val newCollection = sessionManagement.getSessionDetails(1u)
            assertTrue { newCollection.players.size == currentSize.dec() }
        }
    }

    @Test
    fun `updating session successfully with date equal to null does not change actual date`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val oldSession = sessionManagement.getSessionDetails(1u)
            sessionManagement.updateCapacityOrDate(1u, 10u, null)
            val updatedSession = sessionManagement.getSessionDetails(1u)
            assertEquals(10u, updatedSession.capacity)
            assertEquals(oldSession.date, updatedSession.date)
        }
    }

    @Test
    fun `error removing a player from a session due to invalid player fails with ServicesError`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val currentCollection = sessionManagement.getSessionDetails(1u)
            val currentSize = currentCollection.players.size
            sessionManagement.removePlayer(4u, 1u)
            val newCollection = sessionManagement.getSessionDetails(1u)
            assertTrue { newCollection.players.size == currentSize }
        }
    }

    @Test
    fun `updating session successfully with capacity equal to null does not change actual capacity`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val oldSession = sessionManagement.getSessionDetails(1u)
            sessionManagement.updateCapacityOrDate(1u, null, date2)
            val updatedSession = sessionManagement.getSessionDetails(1u)
            assertEquals(date2, updatedSession.date)
            assertEquals(oldSession.capacity, updatedSession.capacity)
        }
    }

    @Test
    fun `trying to update a session with capacity and date null does not affect the session`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val oldSession = sessionManagement.getSessionDetails(1u)
            sessionManagement.updateCapacityOrDate(1u, null, null)
            val updatedSession = sessionManagement.getSessionDetails(1u)
            assertEquals(oldSession.date, updatedSession.date)
            assertEquals(oldSession.capacity, updatedSession.capacity)
        }
    }

    @Test
    fun `delete session successfully`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            // ARRANGE
            val sessions = sessionManagement.getSessions(1u)
            val sessionsSizeBeforeDelete = sessions.size

            // ACT
            sessionManagement.deleteSession(1u)

            // ASSERT
            assertEquals(sessionsSizeBeforeDelete.dec(), sessionManagement.getSessions(1u).size)
            assertFailsWith<ServicesError> { sessionManagement.getSessionDetails(1u) }
        }
    }
}
