package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.addPlayer
import pt.isel.ls.storage.PlayerDataInterface
import pt.isel.ls.storage.SessionDataInterface
import pt.isel.ls.utils.getSessionState
import pt.isel.ls.utils.tryCatch

/**
 * Represents the services related to the session in the application.
 */
class SessionManagement(
    private val sessionDataMem: SessionDataInterface,
    private val playerDataMem: PlayerDataInterface,
) : SessionMInterface {
    override fun addPlayer(
        player: UInt,
        session: UInt,
    ) = tryCatch("Unable to add player to session") {
        val playerToAdd = playerDataMem.readPlayer(pid = player)
        val whereSession = sessionDataMem.readSession(sid = session).first()
        val updatedSession = whereSession.addPlayer(playerToAdd)
        sessionDataMem.updateSession(sid = session, newItem = updatedSession)
    }

    override fun getSessionDetails(sid: UInt): Session =
        tryCatch("Unable to get the details of a Session due") {
            sessionDataMem.readSession(sid = sid).first()
        }

    override fun createSession(
        gid: UInt,
        date: LocalDateTime,
        capacity: UInt,
    ): UInt =
        tryCatch("Unable to create a new session due to") {
            sessionDataMem.createSession(Session(gid = gid, date = date, capacity = capacity))
        }

    override fun getSessions(
        gid: UInt,
        date: LocalDateTime?,
        state: SessionState?,
        playerId: UInt?,
        offset: UInt?,
        limit: UInt?,
    ): Collection<Session> {
        return sessionDataMem.readSession(offset = offset, limit = limit).filter { session ->
            session.gid == gid &&
                (date == null || session.date == date) &&
                (state == null || getSessionState(session) == state) &&
                (playerId == null || session.players.any { player -> player.pid == playerId })
        }
    }
}
