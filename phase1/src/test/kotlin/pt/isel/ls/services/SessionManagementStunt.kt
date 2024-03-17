package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError

private val pid = 1u
private val sid1 = 1u
private val gid1 = 1u
private val defaultMail = Email("default@mail.com")
private val player1 = Player(1u, "test1", defaultMail)
private val date1 = LocalDateTime(2024, 3, 10, 12, 30)
private val players: Collection<Player> = listOf(player1)

object SessionManagementStunt : SessionServices {
    override fun addPlayer(
        player: UInt,
        session: UInt,
    ) = if (player != pid || session != sid1) {
        throw ServicesError("Unable to add player")
    } else {
    }

    override fun getSessionDetails(sid: UInt): Session =
        if (sid == sid1) {
            Session(sid, 1u, 1u, date1, players)
        } else {
            throw ServicesError("Unable to get the details of the Session due to invalid sid.")
        }

    override fun createSession(
        gid: UInt,
        date: LocalDateTime,
        capacity: UInt,
    ): UInt {
        return 1u
    }

    override fun getSessions(
        gid: UInt,
        date: LocalDateTime?,
        state: SessionState?,
        playerId: UInt?,
        offset: UInt?,
        limit: UInt?,
    ): Collection<Session> {
        TODO()
    }
}
