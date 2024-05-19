package pt.isel.ls.services

import kotlinx.datetime.LocalDate
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.domain.info.AuthenticationParam
import pt.isel.ls.domain.info.GameInfo
import pt.isel.ls.domain.info.GameInfoParam
import pt.isel.ls.domain.info.PlayerInfo
import pt.isel.ls.domain.info.PlayerInfoParam
import pt.isel.ls.domain.info.SessionInfo
import pt.isel.ls.storage.SessionStorageInterface

/**
 * Represents the services related to the session in the application.
 *
 * @property sessionDataMem the session storage interface
 * @throws ServicesError containing the message of the error.
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

    override fun sessionDetails(
        sid: UInt,
        limit: UInt?,
        offset: UInt?,
    ): Session =
        tryCatch("Unable to get the details of a Session due") {
            sessionDataMem.read(
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
            val (gid, name) = gameInfo
            requireNotNull(gid) { "Game must be provided" }
            val game = GameInfo(gid, name ?: "")
            val (pid, username) = owner
            requireNotNull(pid) { "Owner pid must be provided" }
            val ownerInfo = PlayerInfo(pid, username ?: "")
            sessionDataMem.create(Session(gameInfo = game, date = date, capacity = capacity, owner = ownerInfo))
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
            sessionDataMem.readBy(
                gameInfo = gameInfo,
                date = date,
                state = state,
                playerInfo = playerInfo,
                offset = offset ?: DEFAULT_OFFSET,
                limit = limit ?: DEFAULT_LIMIT,
            ) ?: throw NoSuchElementException("No sessions found")
        }

    override fun updateCapacityOrDate(
        authentication: AuthenticationParam,
        capacity: UInt?,
        date: LocalDate?,
    ) = tryCatch("Unable to update session ${authentication.second} due") {
        sessionDataMem.updateCapacityOrDate(
            authentication = authentication,
            capacity = capacity,
            date = date,
        )
    }

    override fun deleteSession(sid: UInt) =
        tryCatch("Unable to delete the session due") {
            sessionDataMem.delete(sid = sid)
        }

    override fun removePlayer(
        player: UInt,
        session: UInt,
    ) = tryCatch("Unable to remove player from session due") {
        sessionDataMem.updateRemovePlayer(session, player)
    }

    override fun isPlayerInSession(
        player: UInt,
        session: UInt,
    ): Boolean =
        tryCatch("Unable to check if player is in session due") {
            sessionDataMem.isPlayerInSession(player, session)
        }
}
