package pt.isel.ls.services

import kotlinx.datetime.LocalDate
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.domain.info.AuthenticationParam
import pt.isel.ls.domain.info.GameInfo
import pt.isel.ls.domain.info.GameInfoParam
import pt.isel.ls.domain.info.PlayerInfoParam
import pt.isel.ls.domain.info.SessionInfo
import pt.isel.ls.domain.info.associateWith
import pt.isel.ls.storage.SessionStorageInterface

/**
 * Represents the services related to the session in the application.
 *
 * @property storage the session storage interface
 * @throws ServicesError containing the message of the error.
 */
class SessionManagement(private val storage: SessionStorageInterface) : SessionServices {
    override fun addPlayer(
        player: UInt,
        session: UInt,
    ) = tryCatch("Unable to add player to session due") {
        if (!storage.updateAddPlayer(session, setOf(player))) {
            throw ServicesError("Unable to add player to session.")
        }
    }

    override fun sessionDetails(
        sid: UInt,
        limit: UInt?,
        offset: UInt?,
    ): Session =
        tryCatch("Unable to get the details of a Session due") {
            storage.read(
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
            checkNotNullParam(gid) { "Game must be provided" }
            val game = GameInfo(gid, name ?: "")
            val (pid, username) = owner
            checkNotNullParam(pid) { "Owner pid must be provided" }
            requireValidParam(date >= currentLocalDate()) { "Date must not be in the past." }
            val ownerInfo = pid associateWith (username ?: "")
            storage.create(Session(gameInfo = game, date = date, capacity = capacity, owner = ownerInfo))
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
            val condition = arrayOf(gameInfo, date, state, playerInfo).any { it != null }
            requireValidParam(condition) { "At least one parameter must be provided." }
            storage.readBy(
                gameInfo = gameInfo,
                date = date,
                state = state,
                playerInfo = playerInfo,
                offset = offset ?: DEFAULT_OFFSET,
                limit = limit ?: DEFAULT_LIMIT,
            )
        }

    override fun updateCapacityOrDate(
        authentication: AuthenticationParam,
        capacity: UInt?,
        date: LocalDate?,
    ) = tryCatch("Unable to update session ${authentication.second} due") {
        val condition = date == null || date >= currentLocalDate()
        requireValidParam(date != null || capacity != null) { "At least one parameter must be provided." }
        requireValidParam(condition) { "Date must not be in the past." }
        storage.updateCapacityOrDate(
            authentication = authentication,
            capacity = capacity,
            date = date,
        )
    }

    override fun deleteSession(sid: UInt) =
        tryCatch("Unable to delete the session due") {
            storage.delete(sid = sid)
        }

    override fun removePlayer(
        player: UInt,
        session: UInt,
        token: String,
    ) = tryCatch("Unable to remove player from session due") {
        storage.updateRemovePlayer(session, player, token)
    }

    override fun getPlayerFromSession(
        player: UInt,
        session: UInt,
    ): Player? =
        tryCatch("Unable to get player from session due") {
            storage.readPlayer(player, session)
        }
}
