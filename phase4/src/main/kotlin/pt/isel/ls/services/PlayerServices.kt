package pt.isel.ls.services

import pt.isel.ls.domain.Player
import pt.isel.ls.domain.errors.ServicesError
import java.util.UUID

/**
 * Represents the services related to the player in the application.
 *
 * This interface provides methods for creating a new player and retrieving the details of a player.
 *
 * @throws ServicesError containing the message of the error.
 */
interface PlayerServices {
    /**
     * Creates a new player and stores it.
     *
     * @param name the name of the player.
     * @param email the email (is unique to each player) to be associated with the player.
     * @return A pair containing a [UInt] as a unique key to be associated with the new [Player] and a [UUID] as a unique identifier.
     * @throws ServicesError containing the message of the error.
     */
    fun createPlayer(
        name: String,
        email: String,
        userName: String? = null,
    ): Pair<UInt, UUID>

    /**
     * Returns the details of a player.
     *
     * @param pid the identifier of each player.
     * @return a [Player] containing all the information wanted or null if nothing is found.
     * @throws ServicesError containing the message of the error.
     */
    fun getPlayerDetails(pid: UInt): Player

    /**
     * Checks if the token is valid.
     *
     * @param token the token to be checked.
     * @return true if the token is valid, false otherwise.
     */
    fun isValidToken(token: String): Boolean

    /**
     * Returns the details of a player.
     *
     * @param userName the username of each player.
     * @return a [Player] containing all the information wanted or null if nothing is found.
     * @throws ServicesError containing the message of the error.
     */
    fun getPlayerDetailsBy(userName: String): Player
}
