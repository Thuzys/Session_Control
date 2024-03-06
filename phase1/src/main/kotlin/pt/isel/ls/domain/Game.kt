package pt.isel.ls.domain

/**
 * Represents a game.
 * @property id The game's id.
 * @property name The game's name.
 * @property dev The game's developer.
 * @property genres The game's genres.
 * @throws IllegalArgumentException If the name is empty.
 * @throws IllegalArgumentException If the developer is empty.
 * @throws IllegalArgumentException If the genres are empty.
 */
data class Game(
    val id: UInt,
    val name: String,
    val dev: String,
    val genres: Collection<String>,
) {
    init {
        require(name.isNotEmpty()) { "Game's name can´t be empty." }
        require(dev.isNotEmpty()) { "Game's developer can´t be empty." }
        require(genres.isNotEmpty()) { "Game needs to have at least one genre." }
    }
}
