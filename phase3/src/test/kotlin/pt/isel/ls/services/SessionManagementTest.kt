package pt.isel.ls.services

import kotlinx.datetime.LocalDate
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.storage.SessionStorageStunt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private val date1 = LocalDate(2024, 3, 10)
private val date2 = LocalDate(1904, 3, 10)
private val owner = Pair(1u, "username")

class SessionManagementTest {
    private fun actionSessionManagementTest(code: (session: SessionServices) -> Unit) =
        // arrangement
        SessionManagement(SessionStorageStunt())
            .let(code)

    @Test
    fun `creating a session returns and sid successfully`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val expectedSid = 4u
            val gameInfo = Pair(1u, null)
            val actualSid = sessionManagement.createSession(gameInfo, date1, 2u, owner)
            assertEquals(expectedSid, actualSid)
        }
    }

    @Test
    fun `add a player to a session increments size of session players`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val currentCollection = sessionManagement.getSessionDetails(2u)
            val currentSize = currentCollection.players.size
            sessionManagement.addPlayer(3u, 2u)
            val newCollection = sessionManagement.getSessionDetails(2u)
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
            assertEquals(1u, sessionDetails.gameInfo.gid)
            assertEquals(date1, sessionDetails.date)
            assertTrue { sessionDetails.players.size == 2 }
        }
    }

    @Test
    fun `get Session by gameID successfully`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val gameInfo = Pair(1u, null)
            val sessions = sessionManagement.getSessions(gameInfo)
            assertEquals(2, sessions.size)
            assertTrue(sessions.all { session -> session.gameInfo.gid == 1u })
        }
    }

    @Test
    fun `no match in trying to get Sessions throws ServicesError`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val gameInfo = Pair(8u, null)
            assertFailsWith<ServicesError> { sessionManagement.getSessions(gameInfo) }
        }
    }

    @Test
    fun `get Sessions by date returns successfully`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val date = LocalDate(2024, 3, 10)
            val sessions = sessionManagement.getSessions(date = date)
            assertEquals(3, sessions.size)
            assertTrue(sessions.all { session -> session.date == date })
        }
    }

    @Test
    fun `get Sessions by state returns successfully`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val closeSessions = sessionManagement.getSessions(state = SessionState.CLOSE)
            assertEquals(1, closeSessions.size)
            val openSessions = sessionManagement.getSessions(state = SessionState.OPEN)
            assertEquals(2, openSessions.size)
        }
    }

    @Test
    fun `get Sessions by player ID successfully`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val playerInfo = Pair(2u, null)
            val sessions = sessionManagement.getSessions(playerInfo = playerInfo)
            assertEquals(2, sessions.size)
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
    fun `get session by gameName successfully`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            val gameInfo = Pair(null, "Game")
            val sessions = sessionManagement.getSessions(gameInfo)
            assertEquals(2, sessions.size)
            assertTrue(sessions.all { session -> session.gameInfo.name == "Game" })
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
            val gameInfo = Pair(1u, null)
            val sessions = sessionManagement.getSessions(gameInfo)
            val sessionsSizeBeforeDelete = sessions.size

            // ACT
            sessionManagement.deleteSession(1u)

            // ASSERT
            assertEquals(sessionsSizeBeforeDelete.dec(), sessionManagement.getSessions(gameInfo).size)
            assertFailsWith<ServicesError> { sessionManagement.getSessionDetails(1u) }
        }
    }

    @Test
    fun `isPlayerInSession returns true if player is in session`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            assertTrue { sessionManagement.isPlayerInSession(1u, 1u) }
        }
    }

    @Test
    fun `isPlayerInSession returns false if player is not in session`() {
        actionSessionManagementTest { sessionManagement: SessionServices ->
            assertFalse { sessionManagement.isPlayerInSession(500u, 1u) }
        }
    }
}
