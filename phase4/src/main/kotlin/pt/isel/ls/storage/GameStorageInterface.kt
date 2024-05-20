package pt.isel.ls.storage

import pt.isel.ls.domain.Game
import pt.isel.ls.domain.info.Genres

/**
 * Interface that defines the operations that can be done on the game storage.
 * The game storage is responsible for storing the games.
 * The games are stored in a storage.
 * The storage is a database.
 * The operations that can be done on the game storage are:
 * - create a new game;
 * - read a game by its id;
 * - read games by developer and genres;
 * - update a game;
 * - delete a game.
 */
interface GameStorageInterface {
    /**
     * Creates a new game.
     *
     * @param newItem the new game to be created.
     * @return the id of the new game.
     */
    fun create(newItem: Game): UInt

    /**
     * Reads a game by its id.
     *
     * @param uInt the id of the game.
     * @return the game.
     */
    fun read(uInt: UInt): Game

    /**
     * Reads a game by its offset and limit.
     *
     * @param offset the offset.
     * @param limit the limit.
     * @param dev the developer.
     * @param genres the genres.
     * @return the games.
     */
    fun readBy(
        offset: UInt = LIMIT.toUInt(),
        limit: UInt = OFFSET.toUInt(),
        dev: String? = null,
        genres: Collection<String>? = null,
        name: String? = null,
    ): Collection<Game>

    /**
     * Updates a game.
     *
     * @param uInt the id of the game.
     * @param newItem the new game.
     */
    fun update(
        uInt: UInt,
        newItem: Game,
    )

    /**
     * Deletes a game.
     *
     * @param uInt the id of the game.
     */
    fun delete(uInt: UInt)

    /**
     * Reads all genres.
     *
     * @return the [Genres].
     */
    fun readGenres(): Genres
}
