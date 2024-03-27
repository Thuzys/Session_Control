package pt.isel.ls.domain

import kotlinx.serialization.Serializable

/**
 * Represents a game.
 *
 * @property gid The game's id (unique).
 * @property name The game's name.
 * @property dev The game's developer.
 * @property genres The game's genres.
 * @throws IllegalArgumentException If the name is empty.
 * @throws IllegalArgumentException If the developer is empty.
 * @throws IllegalArgumentException If the genres are empty.
 */
@Serializable
data class Game(
    val gid: UInt? = null,
    val name: String,
    val dev: String,
    val genres: Collection<String>,
) {
    init {
        require(name.isNotBlank()) { "Game's name can´t be empty." }
        require(dev.isNotBlank()) { "Game's developer can´t be empty." }
        require(genres.isNotEmpty()) { "Game needs to have at least one genre." }
    }
}
