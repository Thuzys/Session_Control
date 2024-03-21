package pt.isel.ls.storage

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import kotlin.test.Test
import kotlin.test.assertEquals

private val defaultMail = Email("default@mail.com")
private val player1 = Player(1u, "test1", defaultMail)
private val date1 = LocalDateTime(2024, 3, 10, 12, 30)
private val players: Collection<Player> = listOf(player1)

class SessionDataMemTest {
    private fun makeSessionDataMemTest(code: (sessionDataMem: SessionDataMem) -> Unit) {
        SessionDataMem(SessionStorageStunt()).run { code(this) }
    }

    @Test
    fun createSession() {
        makeSessionDataMemTest {
            assertEquals(4u, it.createSession(Session(2u, 3u, 1u, date1, players)))
        }
    }

    @Test
    fun `readSession only with sid`() {
        makeSessionDataMemTest {
            assertEquals(1u, it.readSession(1u).first().gid)
        }
    }

    @Test
    fun `readSession with limit only`() {
        makeSessionDataMemTest {
            assertEquals(2, it.readSession(limit = 2u).size)
        }
    }

    @Test
    fun updateSession() {
        makeSessionDataMemTest { sessionDataMem ->
            val updatedSession = Session(capacity = 3u, gid = 1u, date = date1, players = players)
            sessionDataMem.updateSession(1u, updatedSession)
            val retrievedSession = sessionDataMem.readSession(1u).first()
            assertEquals(updatedSession, retrievedSession)
        }
    }
}
