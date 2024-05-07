package pt.isel.ls.services

import kotlinx.datetime.LocalDate
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.info.GameInfo
import pt.isel.ls.domain.info.GameInfoParam
import pt.isel.ls.domain.info.PlayerInfo
import pt.isel.ls.domain.info.PlayerInfoParam
import pt.isel.ls.domain.info.SessionInfo
import pt.isel.ls.storage.SessionStorageInterface

/**
 * Represents the services related to the session in the application.
 */
class SessionManagement(private val sessionDataMem: SessionStorageInterface) : SessionServices {
    override fun addPlayer(
        player: UInt,
        session: UInt,
    ) = tryCatch("Unable to add player to session due") {
        if (!sessionDataMem.updateAddPlayer(session, setOf(player))) {
            throw IllegalArgumentException("Unable to add player to session.")
        }
    }

    override fun getSessionDetails(
        sid: UInt,
        limit: UInt?,
        offset: UInt?,
    ): Session =
        tryCatch("Unable to get the details of a Session due") {
            sessionDataMem.readSession(
                sid,
                limit ?: DEFAULT_LIMIT,
                offset ?: DEFAULT_OFFSET,
            ) ?: throw NoSuchElementException()
        }

    override fun createSession(
        gameInfo: GameInfoParam,
        date: LocalDate,
        capacity: UInt,
        owner: PlayerInfoParam,
    ): UInt =
        tryCatch("Unable to create a new session due") {
            requireNotNull(gameInfo.first) { "Game must be provided" }
            val game =
                gameInfo.first?.let { GameInfo(it, gameInfo.second) }
                    ?: throw IllegalArgumentException("Game must be provided")
            val (pid, userName) = owner
            requireNotNull(pid) { "Owner pid must be provided" }
            val ownerInfo = PlayerInfo(pid, userName ?: "")
            sessionDataMem.createSession(Session(gameInfo = game, date = date, capacity = capacity, owner = ownerInfo))
        }

    override fun getSessions(
        gameInfo: GameInfoParam?,
        date: LocalDate?,
        state: SessionState?,
        playerInfo: PlayerInfoParam?,
        offset: UInt?,
        limit: UInt?,
    ): Collection<SessionInfo> =
        tryCatch("Unable to get the sessions due") {
            sessionDataMem.readSessions(
                gameInfo = gameInfo,
                date = date,
                state = state,
                playerInfo = playerInfo,
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

    override fun removePlayer(
        player: UInt,
        session: UInt,
    ) = tryCatch("Unable to remove player from session due") {
        sessionDataMem.updateRemovePlayer(session, player)
    }
}
