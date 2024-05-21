package pt.isel.ls.webApi

import org.http4k.core.Request
import org.http4k.core.Response

/**
 * Handles game-related HTTP requests.
 *
 * This class provides methods for creating a new game, retrieving game details
 * and retrieving games by its corresponding developer and genres.
 * Each method corresponds to a specific HTTP request and returns an HTTP response.
 * The operations are:
 * - Create a new game
 * - Retrieve game details
 * - Retrieve games by developer and genres
 */
interface GameHandlerInterface {
    /**
     * Creates a new game and stores it.
     *
     * @param request The HTTP request containing the game information (name, dev and genres).
     * @return The HTTP response containing the result of the operation.
     */
    fun createGame(request: Request): Response

    /**
     * Retrieves the details of a game.
     *
     * @param request The HTTP request containing the game identifier, the offset and the limit.
     * @return The HTTP response containing the result of the operation.
     */
    fun getGameDetails(request: Request): Response

    /**
     * Retrieves games based on the specified parameters.
     *
     * @param request The HTTP request containing the offset, the limit, the dev and the genres.
     * @return The HTTP response containing the result of the operation.
     */
    fun getGames(request: Request): Response

    /**
     * Retrieves the genres of a game.
     *
     * @param request The HTTP request containing the game identifier.
     * @return The HTTP response containing the result of the operation.
     */
    fun getAllGenres(request: Request): Response
}
