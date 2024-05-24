package pt.isel.ls.services

import pt.isel.ls.domain.Player
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.domain.info.CreatePlayerEmailPasswordParam
import pt.isel.ls.domain.info.CreatePlayerNameParam
import pt.isel.ls.domain.info.PlayerAuthentication
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
     * @param nameUsername the name and username of the player.
     * If the username is null, the name will be used as the username.
     * @param emailPassword the email and password of the player.
     * @return A pair containing a [UInt] as a unique key to be associated with the new [Player]
     * and a [UUID] as a unique identifier.
     * @throws ServicesError containing the message of the error.
     */
    fun createPlayer(
        nameUsername: CreatePlayerNameParam,
        emailPassword: CreatePlayerEmailPasswordParam,
    ): PlayerAuthentication

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
     * @param username the username of each player.
     * @return a [Player] containing all the information wanted or null if nothing is found.
     * @throws ServicesError containing the message of the error.
     */
    fun getPlayerDetailsBy(username: String): Player

    /**
     * Logs in a player.
     *
     * @param username the username of the player.
     * @param password the password of the player.
     * @return a [PlayerAuthentication] containing the token of the player.
     * @throws ServicesError containing the message of the error.
     */
    fun login(
        username: String,
        password: String,
    ): PlayerAuthentication

    /**
     * Logs out a player.
     *
     * @param token the token of the player.
     */
    fun logout(token: UUID)
}
