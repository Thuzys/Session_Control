package pt.isel.ls.services

import kotlinx.datetime.LocalDate
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.storage.SessionStorageInterface

/**
 * Represents the services related to the session in the application.
 */
class SessionManagement(
    private val sessionDataMem: SessionStorageInterface,
//    private val playerDataMem: PlayerStorageInterface,
) : SessionServices {
    override fun addPlayer(
        player: UInt,
        session: UInt,
    ) = tryCatch("Unable to add player to session due") {
//        val playerToAdd = playerDataMem.read(pid = player)
//        val whereSession = sessionDataMem.readSession(sid = session)
//        val updatedSession = whereSession?.addPlayer(playerToAdd) ?: throw NoSuchElementException()
//        sessionDataMem.updateAddPlayer(sid = session, newItem = updatedSession.players)
        if (!sessionDataMem.updateAddPlayer(session, setOf(player))) {
            throw IllegalArgumentException("Player already in session")
        }
    }

    override fun getSessionDetails(sid: UInt): Session =
        tryCatch("Unable to get the details of a Session due") {
            sessionDataMem.readSession(sid = sid) ?: throw NoSuchElementException()
        }

    override fun createSession(
        gid: UInt,
        date: LocalDate,
        capacity: UInt,
    ): UInt =
        tryCatch("Unable to create a new session due") {
            sessionDataMem.createSession(Session(gid = gid, date = date, capacity = capacity))
        }

    override fun getSessions(
        gid: UInt?,
        date: LocalDate?,
        state: SessionState?,
        pid: UInt?,
        userName: String?,
        offset: UInt?,
        limit: UInt?,
    ): Collection<Session> =
        tryCatch("Unable to get the sessions due") {
            sessionDataMem.readSessions(
                gid = gid,
                date = date,
                state = state,
                pid = pid,
                userName = userName,
                offset = offset ?: DEFAULT_OFFSET,
                limit = limit ?: DEFAULT_LIMIT,
            ) ?: throw NoSuchElementException("No sessions found")
        }

    override fun updateCapacityOrDate(
        sid: UInt,
        capacity: UInt?,
        date: LocalDate?,
    ) = tryCatch("Unable to update session $sid due") {
        sessionDataMem.updateCapacityOrDate(
            sid = sid,
            capacity = capacity,
            date = date,
        )
    }

    override fun deleteSession(sid: UInt) =
        tryCatch("Unable to delete the session due") {
            sessionDataMem.deleteSession(sid = sid)
        }

//    override fun getSessionsByPlayer(pid: UInt): Collection<Int> =
//        tryCatch("Unable to get the sessions by player due") {
//            sessionDataMem.getPlayerSessions(pid)
//        }

    override fun removePlayer(
        player: UInt,
        session: UInt,
    ) = tryCatch("Unable to remove player from session due") {
        sessionDataMem.updateRemovePlayer(session, player)
    }
}
