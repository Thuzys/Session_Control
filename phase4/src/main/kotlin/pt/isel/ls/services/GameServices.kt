package pt.isel.ls.services

import pt.isel.ls.domain.Game
import pt.isel.ls.domain.info.Genres

/**
 * Represents the game's services management.
 * This interface allows defining the operations that can be performed over the game's data.
 * The operations are:
 * - Create a new game.
 * - Get the game's details.
 * - Get the game by developer and genres.
 * The operations are defined by the methods of the interface.
 */
interface GameServices {
    /**
     * Creates a new game.
     *
     * @param name The game's name.
     * @param dev The game's developer.
     * @param genres The game's genres.
     * @return The game's id.
     */
    fun createGame(
        name: String,
        dev: String,
        genres: Collection<String>,
    ): UInt

    /**
     * Gets the game's details.
     *
     * @param gid The game's id.
     * @return The game's details.
     */
    fun getGameDetails(gid: UInt): Game

    /**
     * Gets the game by developer and genres.
     *
     * @param offset The offset.
     * @param limit The limit.
     * @param dev The game's developer.
     * @param genres The game's genres.
     * @return Collection of games.
     */
    fun getGames(
        dev: String?,
        genres: Collection<String>?,
        name: String?,
        offset: UInt? = null,
        limit: UInt? = null,
    ): Collection<Game>

    /**
     * Gets all genres on the DB.
     *
     * @return [Genres].
     */
    fun getAllGenres(): Genres
}
