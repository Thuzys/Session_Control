package pt.isel.ls.domain.info

import kotlin.test.Test

class PlayerInfoTest {
    @Test
    fun makePlayerInfo() {
        val playerInfo = PlayerInfo(1u, "username")
        assert(playerInfo.pid == 1u)
        assert(playerInfo.username == "username")
    }
}
