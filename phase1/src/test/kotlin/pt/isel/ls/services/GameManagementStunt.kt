package pt.isel.ls.services

import pt.isel.ls.domain.Game
import pt.isel.ls.domain.errors.ServicesError

object GameManagementStunt : GameServices {
    private val gameId = 1u
    private const val gameName = "Test"
    private const val gameDev = "TestDev"
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

    override fun getGameDetails(
        gid: UInt,
        offset: UInt?,
        limit: UInt?,
    ): Game =
        if (gid == gameId) {
            Game(gid, gameName, gameDev, gameGenres)
        } else {
            throw ServicesError("Unable to find the game due to invalid game id.")
        }

    override fun getGameByDevAndGenres(
        dev: String,
        genres: Collection<String>,
        offset: UInt?,
        limit: UInt?,
    ): Collection<Game> =
        if (dev == gameDev && gameGenres.containsAll(genres)) {
            listOf(Game(gameId, gameName, gameDev, gameGenres))
        } else {
            throw ServicesError("Unable to find the game due to invalid dev or genres.")
        }
}
