package pt.isel.ls.webApi

import org.http4k.core.Request
import org.http4k.core.Response

/**
 * Interface for handling player-related HTTP requests.
 *
 * This interface provides methods for creating a new player and retrieving player details.
 * Each method corresponds to a specific HTTP request and returns an HTTP response.
 */
interface PlayerHandlerInterface {
    /**
     * Handles the HTTP request for creating a new player.
     *
     * @param request The HTTP request containing the necessary information to create a new player.
     * @return The HTTP response indicating the result of the operation.
     */
    fun createPlayer(request: Request): Response

    /**
     * Handles the HTTP request for retrieving a player's details.
     *
     * @param request The HTTP request containing the necessary information to retrieve a player's details.
     * @return The HTTP response containing the player's details.
     */
    fun getPlayer(request: Request): Response

    /**
     * Handles the HTTP request for retrieving a player's details.
     *
     * @param request The HTTP request containing the necessary information to retrieve a player's details.
     * @return The HTTP response containing the player's details.
     */
    fun getPlayerBy(request: Request): Response

    /**
     * Handles the HTTP request for logging in a player.
     *
     * @param request The HTTP request containing the necessary information to log in a player.
     * @return The HTTP response indicating the result of the operation.
     */
    fun login(request: Request): Response

    /**
     * Handles the HTTP request for logging out a player.
     *
     * @param request The HTTP request containing the necessary information to log out a player.
     * @return The HTTP response indicating the result of the operation.
     */
    fun logout(request: Request): Response
}
