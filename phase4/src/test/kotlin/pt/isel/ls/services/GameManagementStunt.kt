package pt.isel.ls.services

import pt.isel.ls.domain.Game
import pt.isel.ls.domain.errors.ServicesError

object GameManagementStunt : GameServices {
    private val gameId = 1u
    private const val GAME_NAME = "Test"
    private const val GAME_DEV = "TestDev"
    private val gameGenres = setOf("TestGenre")

    override fun createGame(
        name: String,
        dev: String,
        genres: Collection<String>,
    ): UInt =
        if (name.isNotBlank() && dev.isNotBlank() && genres.isNotEmpty()) {
            gameId
        } else {
            throw ServicesError(
                "Unable to create a new game due to invalid name, dev or genres.",
            )
        }

    override fun getGameDetails(gid: UInt): Game =
        if (gid == gameId) {
            Game(gid, GAME_NAME, GAME_DEV, gameGenres)
        } else {
            throw ServicesError("Unable to find the game due to invalid game id.")
        }

    override fun getGames(
        dev: String?,
        genres: Collection<String>?,
        name: String?,
        offset: UInt?,
        limit: UInt?,
    ): Collection<Game> =
        if (dev == GAME_DEV && gameGenres.containsAll(genres ?: emptyList())) {
            listOf(Game(gameId, GAME_NAME, GAME_DEV, gameGenres))
        } else {
            throw ServicesError("Unable to find the game due to invalid dev or genres.")
        }

    override fun getAllGenres() = gameGenres
}
