package pt.isel.ls.storage

import kotlinx.datetime.LocalDate
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.info.AuthenticationParam
import pt.isel.ls.domain.info.GameInfoParam
import pt.isel.ls.domain.info.PlayerInfoParam
import pt.isel.ls.domain.info.SessionInfo

/**
 * Represents the storage of the [Session].
 *
 * This interface provides methods for creating, reading and updating objects of type Session in the storage.
 *
 */
interface SessionStorageInterface {
    /**
     * Creates a new Session and stores it.
     *
     * @param newItem The new [Session] to be stored.
     * @return The unique identifier of the new [Session].
     */
    fun create(newItem: Session): UInt

    /**
     * Reads the details of a [Session] from the storage.
     *
     * @param sid The unique identifier of the session to read.
     * @param limit The maximum number of players to be retrieved.
     * @param offset The offset to be applied to the collection of players.
     * @return A [Session] or null if nothing is found.
     */
    fun read(
        sid: UInt,
        limit: UInt,
        offset: UInt,
    ): Session?

    /**
     * Retrieves a collection of sessions based on the specified parameters.
     *
     * @param gameInfo The [GameInfoParam] of the game for which sessions are to be retrieved.
     * Defaults to null, meaning all games.
     * @param date The date of the sessions to be retrieved. Defaults to null, meaning all dates.
     * @param state The [SessionState] of the sessions to be retrieved. Defaults to null, meaning all states.
     * @param playerInfo The [PlayerInfoParam] of the player to filter sessions by. Defaults to null, meaning all players.
     * @param offset The offset to be applied to the collection. Defaults to 0.
     * @param limit The maximum number of sessions to be retrieved. Defaults to 10.
     * @return A collection of sessions matching the specified parameters, or null if no sessions are found.
     */
    fun readBy(
        gameInfo: GameInfoParam? = null,
        date: LocalDate? = null,
        state: SessionState? = null,
        playerInfo: PlayerInfoParam? = null,
        offset: UInt = OFFSET.toUInt(),
        limit: UInt = LIMIT.toUInt(),
    ): Collection<SessionInfo>

    /**
     * Updates a [Session] capacity, date or both.
     *
     * @param authentication The [AuthenticationParam] of the player and session.
     * @param capacity the new value for the capacity. Defaults to null.
     * @param date the new value for the date. Defaults to null
     */
    fun updateCapacityOrDate(
        authentication: AuthenticationParam,
        capacity: UInt? = null,
        date: LocalDate? = null,
    )

    /**
     * Updates a session by removing players from it.
     *
     * @param sid The unique identifier of the [Session] to be updated.
     * @param pid The unique identifier of the [Player] to be removed from the session.
     * @param token The token of the player requesting the removal.
     */
    fun updateRemovePlayer(
        sid: UInt,
        pid: UInt,
        token: String,
    )

    /**
     * Deletes a session from the storage.
     *
     * @param sid The unique identifier of the [Session] to be deleted.
     */
    fun delete(sid: UInt)

    /**
     * Updates a session by adding players to it.
     * @param sid The unique identifier of the [Session] to be updated.
     * @param pid A collection of pids to be added to the [Session].
     * @return true if the update was successful, false otherwise.
     */
    fun updateAddPlayer(
        sid: UInt,
        pid: Collection<UInt>,
    ): Boolean

    /**
     * Retrieves a player from a session.
     *
     * @param player The identifier of the player to retrieve.
     * @param session The identifier of the session which the player is in.
     * @return The [Player] if found, null otherwise.
     */
    fun readPlayer(
        player: UInt,
        session: UInt,
    ): Player?
}
