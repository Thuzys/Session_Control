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
 * @property sessionDataMem the session storage interface
 * @throws ServicesError containing the message of the error.
 */
class SessionManagement(private val sessionDataMem: SessionStorageInterface) : SessionServices {
    override fun addPlayer(
        player: UInt,
        session: UInt,
    ) = tryCatch("Unable to add player to session due") {
        if (!sessionDataMem.updateAddPlayer(session, setOf(player))) {
            throw ServicesError("Unable to add player to session.")
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
            checkNotNullService(gid) { "Game must be provided" }
            val game = GameInfo(gid, name ?: "")
            val (pid, username) = owner
            checkNotNullService(pid) { "Owner pid must be provided" }
            checkValidService(!username.isNullOrBlank()) { "Owner username must be provided" }
            username as String
            checkValidService(date >= currentLocalDate()) { "Date must not be in the past." }
            val ownerInfo = pid associateWith username
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
            val condition = gameInfo != null || date != null || state != null || playerInfo != null
            checkValidService(condition) { "At least one parameter must be provided." }
            sessionDataMem.readBy(
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
        checkValidService(date != null || capacity != null) { "At least one parameter must be provided." }
        checkValidService(condition) { "Date must not be in the past." }
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

    override fun getPlayerFromSession(
        player: UInt,
        session: UInt,
    ): Player =
        tryCatch("Unable to get player from session due") {
            sessionDataMem.readPlayer(player, session)
                ?: throw NoSuchElementException("Player not found in session")
        }
}
