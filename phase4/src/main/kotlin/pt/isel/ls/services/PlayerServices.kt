package pt.isel.ls.services

import pt.isel.ls.domain.Player
import pt.isel.ls.domain.info.PlayerAuthentication
import pt.isel.ls.domain.info.email_password
import pt.isel.ls.domain.info.name_username
import java.util.UUID

/**
 * Represents the services related to the player in the application.
 *
 * This interface provides methods for creating a new player and retrieving the details of a player.
 *
 */
interface PlayerServices {
    /**
     * Creates a new player and stores it.
     *
     * @param nameUsername the name and username of the player.
     * If the username is null, the name will be used as the username.
     * @param emailPassword the email and password of the player.
     * @return A pair containing a [UInt] as a unique key to be associated with the new [Player]
     * and a [UUID] as a unique identifier.
     */
    fun createPlayer(
        nameUsername: name_username,
        emailPassword: email_password,
    ): PlayerAuthentication

    /**
     * Returns the details of a player.
     *
     * @param pid the identifier of each player.
     * @return a [Player] containing all the information wanted or null if nothing is found.
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
     * @param username the username of each player.
     * @return a [Player] containing all the information wanted or null if nothing is found.
     */
    fun getPlayerDetailsBy(username: String): Player

    /**
     * Logs in a player.
     *
     * @param username the username of the player.
     * @param password the password of the player.
     * @return a [PlayerAuthentication] containing the token of the player.
     */
    fun login(
        username: String,
        password: String,
    ): PlayerAuthentication

    /**
     * Logs out a player.
     *
     * @param pid the identifier of the player.
     */
    fun logout(pid: UInt)
}
