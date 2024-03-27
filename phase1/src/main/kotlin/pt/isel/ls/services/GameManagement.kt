package pt.isel.ls.services

import pt.isel.ls.domain.Game
import pt.isel.ls.storage.GameStorageInterface

/**
 * Class responsible for managing the game services.
 */
class GameManagement(private val storage: GameStorageInterface) : GameServices {
    override fun createGame(
        name: String,
        dev: String,
        genres: Collection<String>,
    ): UInt =
        tryCatch("Unable to create a new game due") {
            val newGame = Game(name = name, dev = dev, genres = genres)
            storage.create(newGame)
        }

    override fun getGameDetails(gid: UInt): Game =
        tryCatch("Unable to find the game due") {
            storage.read(gid)
        }

    override fun getGameByDevAndGenres(
        dev: String?,
        genres: Collection<String>?,
        offset: UInt?,
        limit: UInt?,
    ): Collection<Game> =
        tryCatch("Unable to find the game due") {
            storage.readBy(
                offset ?: DEFAULT_OFFSET,
                limit ?: DEFAULT_LIMIT,
                dev,
                genres,
            )
        }
}
