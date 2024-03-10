package pt.isel.ls.services

import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.associatedTo
import pt.isel.ls.storage.SessionDataMem
import pt.isel.ls.utils.tryCatch

/**
 * Represents the services made by the application.
 * @param dataMem the memory container to storage all the data.
 * @throws IllegalStateException containing the message of the error.
 */
class SessionServices(private val dataMem: SessionDataMem) {
    /**
     * Create a new player and storage the same.
     * @param name the name of the player.
     * @param email the email (is unique to each player) to be associated to the player.
     * @return [UInt] a unique key to be associated to the new [Player].
     * @throws IllegalStateException containing the message of the error.
     */
    fun createPlayer(
        name: String,
        email: String,
    ): UInt =
        tryCatch("Unable to create a new player") {
            dataMem.create(name associatedTo Email(email))
        }

    /**
     * returns the details of player.
     * @param uuid the identifier of each player.
     * @return a [Player] containing all the information wanted or null if nothing is found.
     * @throws IllegalStateException containing the message of the error.
     */
    fun getPlayerDetails(uuid: UInt): Player =
        tryCatch("Unable to get the details of a Player due") {
            dataMem.read(uuid, Player.hash) as Player
        }

    /**
     * Create a new game and storage the same.
     * @param name the name of the game.
     * @param dev the developer of the game.
     * @param genres the genres of the game.
     * @return [UInt] a unique key to be associated to the new [Game].
     * @throws IllegalStateException containing the message of the error.
     */
    fun createGame(
        name: String,
        dev: String,
        genres: Collection<String>,
    ): UInt =
        tryCatch("Unable to create a new game due to") {
            dataMem.create(name associatedTo Pair(dev, genres))
        }

    /**
     * returns the details of player.
     * @param uuid the identifier of each player.
     * @return a [Player] containing all the information wanted or null if nothing is found.
     */
    fun getGameDetails(uuid: UInt): Game =
        tryCatch("Unable to find the game due to") {
            dataMem.read(uuid, Game.hash) as Game
        }

    /**
     * Search for a game by the developer and genres.
     * @param dev the developer of the game.
     * @param genres the genres of the game.
     * @return a collection of [Game] containing all the information wanted or null if nothing is found.
     */
    fun searchGameByDevAndGenres(
        dev: String,
        genres: Collection<String>,
    ): Collection<Game> =
        dataMem.readBy(Game.hash) {
            it.filter { game ->
                game.dev == dev && game.genres.containsAll(genres)
            }
        }
}
