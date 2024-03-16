package pt.isel.ls.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID


/**
 * Represents a Player.
 *
 * @param pid the player’s identifier (unique).
 * @param name the UserName of the player.
 * @param email the unique email of a player.
 * @param token the access token of each player.
 * @throws IllegalArgumentException if the name is empty.
 */
@Serializable
data class Player(
    val pid: UInt? = null,
    val name: String,
    val email: Email,
    @Contextual
    val token: UUID = UUID.randomUUID(),
    // use what instead of @Contextual?
) {
    init {
        require(name.isNotBlank()) { "name must not be blank." }
    }
}

/**
 * Creates a new player.
 *
 * @receiver the name of the player([String]).
 * @param email the email (is unique to each player) to be associated to the player.
 * @return [Player] a new player.
 */
infix fun String.associatedTo(email: Email) = Player(name = this, email = email)
