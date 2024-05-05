package pt.isel.ls.domain

import kotlinx.datetime.LocalDate
import java.lang.Math.random
import kotlin.test.Test
import kotlin.test.assertFailsWith

class SessionTest {
    private val capacity = 10u
    private val gid = 1111u
    private val uuid = 1234u
    private val date = LocalDate(2024, 3, 10)
    private val randomUuid = random().toUInt()

    @Test
    fun `instantiating a session successfully`() {
        Session(uuid, capacity, gid, date)
    }

    @Test
    fun `instantiating a session returns a unique session identifier`() {
        val newSession = Session(uuid, capacity, gid, date)
        assert(newSession.sid != randomUuid)
    }

    @Test
    fun `creating a session with capacity equals to zero fails with IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> { Session(uuid, 0u, gid, date) }
    }
}
