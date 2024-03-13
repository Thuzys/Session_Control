package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Domain
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.errors.ServicesError

/**
 * Represents the services made by the application.
 *
 * This interface provides methods for creating, reading and updating [Player], [Session], and [Game] objects.
 * It also provides methods for adding a player to a session and searching for a game by the developer and genres.
 *
 * @throws ServicesError containing the message of the error.
 */
interface Services {
    /**
     * Creates a new player and stores it.
     *
     * @param name the name of the player.
     * @param email the email (is unique to each player) to be associated with the player.
     * @return [UInt] a unique key to be associated with the new [Player].
     * @throws ServicesError containing the message of the error.
     */
    fun createPlayer(
        name: String,
        email: String,
    ): UInt

    /**
     * Returns the details of a player.
     *
     * @param uuid the identifier of each player.
     * @return a [Player] containing all the information wanted or null if nothing is found.
     * @throws ServicesError containing the message of the error.
     */
    fun getPlayerDetails(uuid: UInt): Player

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
     * @param uuid the identifier of each session.
     * @return a [Session] containing all the information wanted or null if nothing is found.
     * @throws ServicesError containing the message of the error.
     */
    fun getSessionDetails(uuid: UInt): Session

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
     * Creates a new game and stores it.
     *
     * @param name the name of the game.
     * @param dev the developer of the game.
     * @param genres the genres of the game.
     * @return [UInt] a unique key to be associated with the new [Game].
     * @throws ServicesError containing the message of the error.
     */
    fun createGame(
        name: String,
        dev: String,
        genres: Collection<String>,
    ): UInt

    /**
     * Returns the details of a game.
     *
     * @param uuid the identifier of each game.
     * @return a [Game] containing all the information wanted or null if nothing is found.
     * @throws ServicesError containing the message of the error.
     */
    fun getGameDetails(uuid: UInt): Game

    /**
     * Searches for a domain by the given parameters.
     *
     * @param type The hash of each instance class of [Domain].
     * It's used to determine the type of items to be retrieved from the storage.
     * @param offset The offset to be applied to the collection.
     * It's used for pagination to skip a certain number of items in the collection.
     * @param limit The limit to be applied to the collection.
     * It's used for pagination to limit the number of items returned in the collection.
     * @param filter The filter to be applied to the collection.
     * @return a collection of [Domain] containing all the information wanted or an empty list if nothing is found.
     */
    fun <T : Domain> searchBy(
        type: Int,
        offset: UInt = 0u,
        limit: UInt = 10u,
        filter: (T) -> Boolean,
    ): Collection<T>
}
