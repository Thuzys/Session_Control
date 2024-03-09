package pt.isel.ls.domain

import java.util.UUID

/**
 * represents the Player's data.
 * @param uuid the player’s identifier (unique).
 * @param name the UserName of the player.
 * @param email the unique email of a player.
 * @param token the access token of each player.
 * @throws IllegalArgumentException if the name is empty.
 * @throws IllegalArgumentException if the email is invalid.
 */
data class Player(
    override val uuid: UInt? = null,
    val name: String,
    val email: Email,
    val token: UUID = UUID.randomUUID(),
) : Domain(uuid = uuid) {
    init {
        require(name.isNotEmpty()) { "name must not be blank." }
    }

    companion object {
        val hash = hashCode()
    }
}

val t = Player.hash
