package pt.isel.ls.storage

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState

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
     * @return A [Session] or null if nothing is found.
     */

    fun readSession(sid: UInt): Session?

    /**
     * Retrieves a collection of sessions based on the specified parameters.
     *
     * @param gid The unique identifier of the game for which sessions are to be retrieved.
     * @param date The date of the sessions to be retrieved. Defaults to null, meaning all dates.
     * @param state The [SessionState] of the sessions to be retrieved. Defaults to null, meaning all states.
     * @param playerId The unique identifier of the player for whom sessions are to be retrieved. Defaults to null, meaning all players.
     * @param offset The offset to be applied to the collection. Defaults to 0.
     * @param limit The maximum number of sessions to be retrieved. Defaults to 10.
     * @return A collection of sessions matching the specified parameters, or null if no sessions are found.
     */
    fun readSessions(
        gid: UInt,
        date: LocalDateTime? = null,
        state: SessionState? = null,
        playerId: UInt? = null,
        offset: UInt = 0u,
        limit: UInt = 10u,
    ): Collection<Session>?

    /**
     * Updates a session by adding players to it.
     *
     * @param sid The unique identifier of the [Session] to be updated.
     * @param newItem A collection of players to be added to the [Session].
     */
    fun updateAddPlayer(
        sid: UInt,
        newItem: Collection<Player>,
    )
}
