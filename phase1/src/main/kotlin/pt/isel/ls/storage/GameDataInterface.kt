package pt.isel.ls.storage

import pt.isel.ls.domain.Game

/**
 * Represents the game's data management.
 * This interface allows to define the operations that can be performed over the game's data.
 * The operations are:
 * - Create a new game.
 * - Get the game's details.
 * - Get the game by developer and genres.
 * The operations are defined by the methods of the interface.
 */
interface GameDataInterface {
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
    fun getGameDetails(
        gid: UInt,
        offset: UInt = 0u,
        limit: UInt = 10u,
    ): Game

    fun getGameByDevAndGenres(
        offset: UInt = 0u,
        limit: UInt = 10u,
        filter: (Iterable<Game>) -> Collection<Game>,
    ): Collection<Game>
}
