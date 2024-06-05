package pt.isel.ls.services

import kotlinx.datetime.LocalDate
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.domain.info.AuthenticationParam
import pt.isel.ls.domain.info.GameInfoParam
import pt.isel.ls.domain.info.PlayerInfoParam
import pt.isel.ls.domain.info.SessionInfo

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
     * @param limit the maximum number of players to be retrieved.
     * @param offset the offset to be applied to the collection of players.
     * @return a [Session] containing all the information wanted or null if nothing is found.
     * @throws ServicesError containing the message of the error.
     */
    fun sessionDetails(
        sid: UInt,
        limit: UInt? = null,
        offset: UInt? = null,
    ): Session

    /**
     * Creates a new session and stores it.
     *
     * @param gameInfo The identifier of the game for which the session is being created.
     * @param date The date and time of the session.
     * @param capacity The capacity of the session.
     * @param owner The player that created the session.
     * @return The unique identifier of the new session.
     */
    fun createSession(
        gameInfo: GameInfoParam,
        date: LocalDate,
        capacity: UInt,
        owner: PlayerInfoParam,
    ): UInt

    /**
     * Retrieves sessions based on the specified parameters.
     *
     * @param gameInfo The identifier of the game for which sessions are to be retrieved.
     * @param date The date of the sessions to be retrieved.
     * @param state The state of the sessions to be retrieved.
     * @param playerInfo The identifier of the player to filter sessions by.
     * @param offset The offset to be applied to the collection.
     * @param limit The maximum number of sessions to be retrieved.
     *
     * @return A collection of sessions that match the specified parameters.
     */
    fun getSessions(
        gameInfo: GameInfoParam? = null,
        date: LocalDate? = null,
        state: SessionState? = null,
        playerInfo: PlayerInfoParam? = null,
        offset: UInt? = DEFAULT_OFFSET,
        limit: UInt? = DEFAULT_LIMIT,
    ): Collection<SessionInfo>

    /**
     * Updates the capacity or date of a session.
     *
     * This function allows updating either the capacity or the date of a session identified by the given session identifier.
     * The session identified by 'sid' will have its capacity updated to the new value provided in 'capacity', and/or its date
     * updated to the new value provided in 'date'. If 'capacity' is null, only the date will be updated, and vice versa.
     * If both 'capacity' and 'date' are null, no changes will be applied to the session.
     *
     * @param authentication The identifier of the player and the session.
     * @param capacity The new capacity of the session (optional). If null, the capacity will not be updated.
     * @param date The new date and time of the session (optional). If null, the date will not be updated.
     */
    fun updateCapacityOrDate(
        authentication: AuthenticationParam,
        capacity: UInt? = null,
        date: LocalDate? = null,
    )

    /**
     * Removes a player from a session.
     *
     * @param player The identifier of the player to remove.
     * @param session The identifier of the session from which the player will be removed.
     * @param token The token of the player requesting the removal.
     */
    fun removePlayer(
        player: UInt,
        session: UInt,
        token: String,
    )

    /**
     * Deletes a session from the storage.
     *
     * @param sid The unique identifier of the session to be deleted.
     */
    fun deleteSession(sid: UInt)

    /**
     * Retrieves a player from a session.
     *
     * @param player The identifier of the player to retrieve.
     * @param session The identifier of the session from which the player will be retrieved.
     * @return The player that was retrieved.
     */
    fun getPlayerFromSession(
        player: UInt,
        session: UInt,
    ): Player?
}
