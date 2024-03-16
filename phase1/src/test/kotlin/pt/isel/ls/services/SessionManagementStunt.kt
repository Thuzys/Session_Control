package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState

class SessionManagementStunt : SessionMInterface {
    override fun addPlayer(
        player: UInt,
        session: UInt,
    ) {
        TODO("Not yet implemented")
    }

    override fun getSessionDetails(sid: UInt): Session {
        TODO("Not yet implemented")
    }

    override fun createSession(
        gid: UInt,
        date: LocalDateTime,
        capacity: UInt,
    ): UInt {
        TODO("Not yet implemented")
    }

    override fun getSessions(
        gid: UInt,
        date: LocalDateTime?,
        state: SessionState?,
        playerId: UInt?,
        offset: UInt?,
        limit: UInt?,
    ): Collection<Session> {
        TODO("Not yet implemented")
    }
}
