package pt.isel.ls.webApi

import org.http4k.core.Request
import org.http4k.core.Response

/**
 * Interface defining operations for handling sessions in the web API.
 */
interface SessionHandlerInterface {
    /**
     * Creates a new session based on the request data.
     *
     * @param request The HTTP request containing the session data.
     * @return A response indicating the outcome of the session creation operation.
     */
    fun createSession(request: Request): Response

    /**
     * Retrieves details of a session based on the provided session ID.
     *
     * @param request The HTTP request containing the session ID.
     * @return A response containing the session details if found, or an error response otherwise.
     */
    fun getSession(request: Request): Response

    /**
     * Retrieves a collection of sessions based on the following criteria:
     *  - gid: The game identifier (required).
     *  - date: The date of the sessions. (optional)
     *  - state: The state of the sessions (OPEN or CLOSE). (optional)
     *  - playerId: The player identifier. (optional)
     *  - offset: The offset for pagination. (offset = 0u if not provided)
     *  - limit: The maximum number of sessions to retrieve. (limit = 10u if not provided)
     * @param request The HTTP request containing the session criteria.
     *
     * @return A response containing the sessions matching the criteria, or an error response if no sessions are found.
     */
    fun getSessions(request: Request): Response

    /**
     * Adds a player to a session based on the provided player ID and session ID.
     *
     * @param request The HTTP request containing the player and session data.
     * @return A response indicating the outcome of the player addition operation.
     */
    fun addPlayerToSession(request: Request): Response

    /**
     * Updates the capacity or date of a session based on the provided request data.
     *
     * This function handles the request data to update the capacity or date of a session.
     * The request should contain the session identifier (sid) and optionally the new capacity and/or date.
     * If both capacity and date are provided in the request, the session's capacity and date will be updated.
     * If either capacity or date is not provided, only the specified value will be updated.
     * If neither capacity nor date is provided, the function will return an error response.
     *
     * @param request The HTTP request containing the session identifier and optionally the new capacity and/or date.
     * @return A response indicating the outcome of the session update operation.
     */
    fun updateCapacityOrDate(request: Request): Response

    /**
     * Removes a player from a session based on the provided player ID and session ID.
     *
     * @param request The HTTP request containing the player and session data.
     * @return A response indicating the outcome of the player removal operation.
     */
    fun removePlayerFromSession(request: Request): Response

    /**
     * Deletes a session based on the provided session ID.
     *
     * @param request The HTTP request containing the session ID.
     * @return A response indicating the outcome of the session deletion operation.
     */
    fun deleteSession(request: Request): Response

    /**
     * Retrieves the player associated with the session.
     *
     * @param request The HTTP request containing the session and the player's ID.
     * @return A response containing the player associated with the session.
     */
    fun getPlayerFromSession(request: Request): Response
}
