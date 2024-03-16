package pt.isel.ls.storage

import pt.isel.ls.domain.Game

/**
 * Represents the game's data management.
 *
 * This class allows defining the operations that can be performed over the game's data.
 * The operations are:
 * - Create a new game.
 * - Get the game's details.
 * - Get the game by developer and genres.
 */
class GameDataMem(private val storage: Storage<Game>) : GameDataInterface {
    override fun createGame(
        name: String,
        dev: String,
        genres: Collection<String>,
    ): UInt = storage.create(Game(name = name, dev = dev, genres = genres))

    override fun getGameDetails(
        gid: UInt,
        offset: UInt,
        limit: UInt,
    ): Game = storage.read(gid, offset, limit)?.first() ?: throw NoSuchElementException("Game with id $gid not found.")

    override fun getGameByDevAndGenres(
        offset: UInt,
        limit: UInt,
        filter: (Iterable<Game>) -> Collection<Game>,
    ): Collection<Game> =
        filter(
            storage.read(offset = offset, limit = limit) ?: throw NoSuchElementException("No games found."),
        )
}
