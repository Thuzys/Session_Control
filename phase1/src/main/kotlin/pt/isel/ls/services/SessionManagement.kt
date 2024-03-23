package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.addPlayer
import pt.isel.ls.storage.PlayerDataInterface
import pt.isel.ls.storage.SessionStorageInterface
import pt.isel.ls.utils.tryCatch

const val DEFAULT_OFFSET = 0u
const val DEFAULT_LIMIT = 10u

/**
 * Represents the services related to the session in the application.
 */
class SessionManagement(
    private val sessionDataMem: SessionStorageInterface,
    private val playerDataMem: PlayerDataInterface,
) : SessionServices {
    override fun addPlayer(
        player: UInt,
        session: UInt,
    ) = tryCatch("Unable to add player to session") {
        val playerToAdd = playerDataMem.readPlayer(pid = player)
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
    ): Collection<Session> =
        tryCatch("Unable to create a new session due to") {
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
