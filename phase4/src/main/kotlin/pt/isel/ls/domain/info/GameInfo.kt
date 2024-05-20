package pt.isel.ls.domain.info

import kotlinx.serialization.Serializable

/**
 * The information about the game.
 * Used to retrieve a session.
 *
 * [UInt] is the identifier of the game.
 * [String] is the name of the game.
 */
typealias GameInfoParam = Pair<UInt?, String?>

/**
 * All genres of the games.
 *
 * [String] is the genre of the game.
 * [Genres] is the collection of genres.
 */
typealias Genres = Collection<String>

/**
 * Represents a game with its identifier and name.
 *
 * @param gid The identifier of the game.
 * @param name The name of the game.
 */
@Serializable
data class GameInfo(
    val gid: UInt,
    val name: String? = null,
)
