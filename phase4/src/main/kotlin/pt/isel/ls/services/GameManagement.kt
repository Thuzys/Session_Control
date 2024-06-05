package pt.isel.ls.services

import pt.isel.ls.domain.Game
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.domain.info.Genres
import pt.isel.ls.storage.GameStorageInterface

/**
 * Class responsible for managing the game services.
 *
 * @property storage The storage interface for the game.
 * @throws ServicesError containing the message of the error.
 */
class GameManagement(private val storage: GameStorageInterface) : GameServices {
    override fun createGame(
        name: String,
        dev: String,
        genres: Collection<String>,
    ): UInt =
        tryCatch("Unable to create a new game due") {
            requireValidParam(genres.isNotEmpty()) { "At least on genre must be provided." }
            val newGame = Game(name = name, dev = dev, genres = genres)
            storage.create(newGame)
        }

    override fun getGameDetails(gid: UInt): Game =
        tryCatch("Unable to find the game due") {
            storage.read(gid)
        }

    override fun getGames(
        dev: String?,
        genres: Collection<String>?,
        name: String?,
        offset: UInt?,
        limit: UInt?,
    ): Collection<Game> =
        tryCatch("Unable to find games due") {
            val condition = arrayOf(dev, genres, name).any { it != null }
            requireValidParam(condition) { "At least one parameter must be provided." }
            storage.readBy(
                offset ?: DEFAULT_OFFSET,
                limit ?: DEFAULT_LIMIT,
                dev,
                genres,
                name,
            )
        }

    override fun getAllGenres(): Genres =
        tryCatch("Unable to find the genres due") {
            storage.readGenres()
        }
}
