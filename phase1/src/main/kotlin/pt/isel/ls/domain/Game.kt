package pt.isel.ls.domain

/**
 * Represents a game.
 * @property uuid The game's id (unique).
 * @property name The game's name.
 * @property dev The game's developer.
 * @property genres The game's genres.
 * @throws IllegalArgumentException If the name is empty.
 * @throws IllegalArgumentException If the developer is empty.
 * @throws IllegalArgumentException If the genres are empty.
 */
data class Game(
    override val uuid: UInt? = null,
    val name: String,
    val dev: String,
    val genres: Collection<String>,
) : Domain(uuid = uuid) {
    init {
        require(name.isNotEmpty()) { "Game's name can´t be empty." }
        require(dev.isNotEmpty()) { "Game's developer can´t be empty." }
        require(genres.isNotEmpty()) { "Game needs to have at least one genre." }
    }

    companion object {
        val hash: Int = hashCode()
    }
}

/**
 * Creates a new player.
 * @receiver the name of the player([String]).
 * @param email the email (is unique to each player) to be associated to the player.
 * @return [Player] a new player.
 */
infix fun String.associatedTo(gameDetails: Pair<String, Collection<String>>) =
    Game(
        name = this,
        dev = gameDetails.first,
        genres = gameDetails.second,
    )
