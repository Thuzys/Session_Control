package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.addPlayer
import pt.isel.ls.storage.PlayerStorageInterface
import pt.isel.ls.storage.SessionStorageInterface

/**
 * Represents the services related to the session in the application.
 */
class SessionManagement(
    private val sessionDataMem: SessionStorageInterface,
    private val playerDataMem: PlayerStorageInterface,
) : SessionServices {
    override fun addPlayer(
        player: UInt,
        session: UInt,
    ) = tryCatch("Unable to add player to session due") {
        val playerToAdd = playerDataMem.read(pid = player)
        val whereSession = sessionDataMem.readSession(sid = session)
        val updatedSession = whereSession?.addPlayer(playerToAdd) ?: throw NoSuchElementException()
        sessionDataMem.updateAddPlayer(sid = session, newItem = updatedSession.players)
    }

    override fun getSessionDetails(sid: UInt): Session =
        tryCatch("Unable to get the details of a Session due") {
            sessionDataMem.readSession(sid = sid) ?: throw NoSuchElementException()
        }

    override fun createSession(
        gid: UInt,
        date: LocalDateTime,
        capacity: UInt,
    ): UInt =
        tryCatch("Unable to create a new session due") {
            sessionDataMem.createSession(Session(gid = gid, date = date, capacity = capacity))
        }

    override fun getSessions(
        gid: UInt,
        date: LocalDateTime?,
        state: SessionState?,
        playerId: UInt?,
        offset: UInt?,
        limit: UInt?,
    ): Collection<Session> =
        tryCatch("Unable to create a new session due") {
            sessionDataMem.readSessions(
                gid = gid,
                date = date,
                state = state,
                playerId = playerId,
                offset = offset ?: DEFAULT_OFFSET,
                limit = limit ?: DEFAULT_LIMIT,
            ) ?: throw NoSuchElementException()
        }
}
