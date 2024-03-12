package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.storage.SessionDataMem
import pt.isel.ls.storage.StorageStunt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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
            assertFailsWith<ServicesError> { (it.getPlayerDetails(3u)) }
        }
    }

    @Test
    fun `error creating a player`() {
        makeSessionTest {
            assertFailsWith<ServicesError> { it.createPlayer("test", "badEmail") }
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
    fun `create a new Game`() {
        makeSessionTest {
            assertEquals(4u, it.createGame("test", "dev", setOf("genre")))
        }
    }

    @Test
    fun `error adding a player to a session (invalid player)`() {
        makeSessionTest {
            assertFailsWith<ServicesError> { it.addPlayer(4u, 1u) }
        }
    }

    @Test
    fun `error creating a game`() {
        makeSessionTest {
            assertFailsWith<ServicesError> { it.createGame("test", "dev", setOf()) }
        }
    }

    @Test
    fun `error adding a player to a session (invalid session)`() {
        makeSessionTest {
            assertFailsWith<ServicesError> { it.addPlayer(1u, 3u) }
        }
    }

    @Test
    fun `get details of a game`() {
        makeSessionTest {
            val game = it.getGameDetails(1u)
            assertEquals("test", game.name)
            assertEquals("dev", game.dev)
            assertTrue { "genre" in game.genres }
        }
    }

    @Test
    fun `trying to get details of a non existent session`() {
        makeSessionTest {
            assertFailsWith<ServicesError> { it.getSessionDetails(5u) }
        }
    }

    @Test
    fun `error getting details of a game`() {
        makeSessionTest {
            assertFailsWith<ServicesError> { it.getGameDetails(60u) }
        }
    }

    @Test
    fun `get details of a session`() {
        makeSessionTest {
            val date = LocalDateTime(2024, 3, 10, 12, 30)
            val sessionDetails = it.getSessionDetails(1u)
            assertEquals(1u, sessionDetails.uuid)
            assertEquals(2u, sessionDetails.capacity)
            assertEquals(1u, sessionDetails.gid)
            assertEquals(date, sessionDetails.date)
            assertTrue { sessionDetails.players.size == 2 }
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

    @Test
    fun `get Game by developer and genres with no results`() {
        makeSessionTest {
            val games = it.searchGameByDevAndGenres("dev", setOf("genre2"))
            assertEquals(0, games.size)
        }
    }
}
