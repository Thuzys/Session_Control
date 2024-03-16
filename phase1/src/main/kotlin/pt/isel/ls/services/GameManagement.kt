package pt.isel.ls.services

import pt.isel.ls.domain.Game
import pt.isel.ls.storage.GameDataMem
import pt.isel.ls.utils.tryCatch

/**
 * Class responsible for managing the game services.
 */
class GameManagement(private val gameDataMem: GameDataMem) : GameServices {
    override fun createGame(
        name: String,
        dev: String,
        genres: Collection<String>,
    ): UInt =
        tryCatch("Unable to create a new game due to") {
            gameDataMem.createGame(name, dev, genres)
        }

    override fun getGameDetails(
        gid: UInt,
        offset: UInt,
        limit: UInt,
    ): Game =
        tryCatch("Unable to find the game due to") {
            gameDataMem.getGameDetails(gid, offset, limit)
        }

    override fun getGameByDevAndGenres(
        offset: UInt,
        limit: UInt,
        dev: String,
        genres: Collection<String>,
    ): Collection<Game> =
        tryCatch("Unable to find the game due to") {
            gameDataMem.getGameByDevAndGenres(offset, limit, filterByDevAndGenres(dev, genres))
        }

    private fun filterByDevAndGenres(
        dev: String,
        genres: Collection<String>,
    ): (Iterable<Game>) -> Collection<Game> =
        { games ->
            games.filter { game ->
                game.dev == dev && game.genres.containsAll(genres)
            }
        }
}
