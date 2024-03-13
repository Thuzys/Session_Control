package pt.isel.ls.domain

import java.util.UUID

/**
 * Represents a Player.
 *
 * @param uuid the player’s identifier (unique).
 * @param name the UserName of the player.
 * @param email the unique email of a player.
 * @param token the access token of each player.
 * @throws IllegalArgumentException if the name is empty.
 */
data class Player(
    val uuid: UInt? = null,
    val name: String,
    val email: Email,
    val token: UUID = UUID.randomUUID(),
) {
    init {
        require(name.isNotBlank()) { "name must not be blank." }
    }

    companion object {
        val hash = hashCode()
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
