package pt.isel.ls.domain

import kotlinx.datetime.LocalDateTime
import java.lang.Math.random
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertTrue

class SessionTest {
    private val capacity = 10u
    private val gid = 1111u
    private val uuid = 1234u
    private val date = LocalDateTime
    private val randomUuid = random().toUInt()

    @Test
    fun `session instantiation`() {
        Session(capacity, gid, date, uuid)
    }

    @Test
    fun `unique session identifier`() {
        val newSession = Session(capacity, gid, date, uuid)
        assert(newSession.uuid != randomUuid)
    }

    @Test
    fun `add player`() {
        val session = Session(capacity, gid, date, uuid)
        val newPlayer = Player(123u, "João", "João@gmail.com", UUID.randomUUID())
        val finalSession = session.copy(players = listOf(newPlayer))
        val addPlayerSession = session.addPlayer(newPlayer)
        assertTrue { finalSession == addPlayerSession }
    }
}
