package pt.isel.ls.storage

import kotlinx.datetime.LocalDate
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
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
    fun createSession(newItem: Session): UInt

    /**
     * Reads the details of a [Session] from the storage.
     *
     * @param sid The unique identifier of the session to read.
     * @param limit The maximum number of players to be retrieved.
     * @param offset The offset to be applied to the collection of players.
     * @return A [Session] or null if nothing is found.
     */
    fun readSession(
        sid: UInt,
        limit: UInt = 11u,
        offset: UInt = 0u,
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
    fun readSessions(
        gameInfo: GameInfoParam? = null,
        date: LocalDate? = null,
        state: SessionState? = null,
        playerInfo: PlayerInfoParam? = null,
        offset: UInt = 0u,
        limit: UInt = 11u,
    ): Collection<SessionInfo>?

    /**
     * Updates a [Session] capacity, date or both.
     *
     * @param sid The unique identifier of the [Session] to be updated.
     * @param capacity the new value for the capacity. Defaults to null.
     * @param date the new value for the date. Defaults to null
     */
    fun updateCapacityOrDate(
        sid: UInt,
        capacity: UInt? = null,
        date: LocalDate? = null,
    )

    /**
     * Updates a session by removing players from it.
     *
     * @param sid The unique identifier of the [Session] to be updated.
     * @param pid The unique identifier of the [Player] to be removed from the session.
     */
    fun updateRemovePlayer(
        sid: UInt,
        pid: UInt,
    )

    /**
     * Deletes a session from the storage.
     *
     * @param sid The unique identifier of the [Session] to be deleted.
     */
    fun deleteSession(sid: UInt)

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
}
