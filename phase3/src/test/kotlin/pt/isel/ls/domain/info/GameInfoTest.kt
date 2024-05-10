package pt.isel.ls.domain.info

import kotlin.test.Test

class GameInfoTest {
    @Test
    fun testGameInfo() {
        val gameInfo = GameInfo(1u, "Game")
        assert(gameInfo.gid == 1u)
        assert(gameInfo.name == "Game")
    }

    @Test
    fun testGameInfoWithNullName() {
        val gameInfo = GameInfo(1u)
        assert(gameInfo.gid == 1u)
        assert(gameInfo.name == null)
    }
}
