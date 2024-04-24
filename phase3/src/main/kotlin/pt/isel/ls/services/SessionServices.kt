package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError

/**
 * Represents the services related to the session in the application.
 *
 * This interface provides methods for adding a player to a session, creating a new session, and retrieving the details
 * of a session.
 * The operations are defined by the methods of the interface.
 */
interface SessionServices {
    /**
     * Adds a player to a session.
     * Retrieves the player information based on the given player identifier (uuid) and adds it to the session
     * identified by the given session identifier.
     *
     * @param player The identifier of the player to add.
     * @param session The identifier of the session to which the player will be added.
     * @throws ServicesError if the player or session is not found, or if there's an issue adding the player to the session.
     */
    fun addPlayer(
        player: UInt,
        session: UInt,
    )

    /**
     * Returns the details of a session.
     *
     * @param sid the identifier of each session.
     * @return a [Session] containing all the information wanted or null if nothing is found.
     * @throws ServicesError containing the message of the error.
     */
    fun getSessionDetails(sid: UInt): Session

    /**
     * Creates a new session and stores it.
     *
     * @param gid The identifier of the game for which the session is being created.
     * @param date The date and time of the session.
     * @param capacity The capacity of the session.
     * @return The unique identifier of the new session.
     * @throws ServicesError containing the message of the error.
     */
    fun createSession(
        gid: UInt,
        date: LocalDateTime,
        capacity: UInt,
    ): UInt

    /**
     * Retrieves sessions based on the specified parameters.
     *
     * @param gid The identifier of the game for which sessions are being retrieved.
     * @param date The date and time of the sessions to retrieve (optional).
     * @param state The state of the sessions to retrieve (optional).
     * @param playerId The identifier of the player to filter sessions by (optional).
     * @return A collection of sessions that match the specified parameters.
     */
    fun getSessions(
        gid: UInt? = null,
        date: LocalDateTime? = null,
        state: SessionState? = null,
        playerId: UInt? = null,
        offset: UInt? = 0u,
        limit: UInt? = 10u,
    ): Collection<Session>

    /**
     * Updates the capacity or date of a session.
     *
     * This function allows updating either the capacity or the date of a session identified by the given session identifier.
     * The session identified by 'sid' will have its capacity updated to the new value provided in 'capacity', and/or its date
     * updated to the new value provided in 'date'. If 'capacity' is null, only the date will be updated, and vice versa.
     * If both 'capacity' and 'date' are null, no changes will be applied to the session.
     *
     * @param sid The identifier of the session to update.
     * @param capacity The new capacity of the session (optional). If null, the capacity will not be updated.
     * @param date The new date and time of the session (optional). If null, the date will not be updated.
     * @throws ServicesError if there's an issue updating the session.
     */
    fun updateCapacityOrDate(
        sid: UInt,
        capacity: UInt? = null,
        date: LocalDateTime? = null,
    )

    /**
     * Removes a player from a session.
     *
     * @param player The identifier of the player to remove.
     * @param session The identifier of the session from which the player will be removed.
     */
    fun removePlayer(
        player: UInt,
        session: UInt,
    )

    /**
     * Deletes a session from the storage.
     *
     * @param sid The unique identifier of the session to be deleted.
     */
    fun deleteSession(sid: UInt)

//    /**
//     * Retrieves the sid of the sessions containing the player.
//     * @param pid The identifier of the player.
//     *
//     * @return A collection of sessions that contain the player.
//     */
//    fun getSessionsByPlayer(pid: UInt): Collection<Int>
}
