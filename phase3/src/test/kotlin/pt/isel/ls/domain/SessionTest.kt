package pt.isel.ls.domain

import kotlinx.datetime.LocalDate
import pt.isel.ls.domain.info.GameInfo
import pt.isel.ls.domain.info.PlayerInfo
import kotlin.test.Test
import kotlin.test.assertFailsWith

class SessionTest {
    private val capacity = 10u
    private val gameInfo = GameInfo(1111u, "Game")
    private val uuid = 1234u
    private val date = LocalDate(2024, 3, 10)
    private val owner = PlayerInfo(1u, "username")

    @Test
    fun `instantiating a session successfully`() {
        Session(uuid, capacity, gameInfo, date, owner)
    }

    @Test
    fun `creating a session with capacity equals to zero fails with IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> { Session(uuid, 0u, gameInfo, date, owner) }
    }
}
