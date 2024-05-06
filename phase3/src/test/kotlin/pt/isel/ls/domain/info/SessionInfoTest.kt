package pt.isel.ls.domain.info

import kotlinx.datetime.LocalDate
import kotlin.test.Test

class SessionInfoTest {
    @Test
    fun makeSessionInfo() {
        val date = LocalDate.parse("2021-06-01")
        val sessionInfo = SessionInfo(1u, 1u, GameInfo(1u, "game"), date)
        assert(sessionInfo.sid == 1u)
        assert(sessionInfo.owner == 1u)
        assert(sessionInfo.gameInfo == GameInfo(1u, "game"))
        assert(sessionInfo.date == date)
    }
}
