package pt.isel.ls.domain

import kotlinx.datetime.LocalDateTime
import java.lang.Math.random
import kotlin.test.Test
import kotlin.test.assertFailsWith

class SessionTest {
    private val capacity = 10u
    private val gid = 1111u
    private val uuid = 1234u
    private val date = LocalDateTime(2024, 3, 10, 12, 30)
    private val randomUuid = random().toUInt()

    @Test
    fun `session instantiation`() {
        Session(uuid, capacity, gid, date)
    }

    @Test
    fun `unique session identifier`() {
        val newSession = Session(uuid, capacity, gid, date)
        assert(newSession.uuid != randomUuid)
    }

    @Test
    fun `creating a session with capacity equals to zero`() {
        assertFailsWith<IllegalArgumentException> { Session(uuid, 0u, gid, date) }
    }
}
