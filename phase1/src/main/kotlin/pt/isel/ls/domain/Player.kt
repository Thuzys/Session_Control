package pt.isel.ls.domain

import pt.isel.ls.utils.validateEmail
import java.util.UUID

/**
 * represents the Player's data.
 * @param uId the player’s identifier.
 * @param name the UserName of the player.
 * @param email the unique email of a player.
 * @param token the access token of each player.
 * @throws IllegalArgumentException if the name is empty.
 * @throws IllegalArgumentException if the email is invalid.
 */
data class Player(
    val uId: UInt,
    val name: String,
    val email: String,
    val token: UUID = UUID.randomUUID(),
) {
    init {
        require(name.isNotEmpty()) { "name must not be blank." }
        require(validateEmail(email)) { "invalid email pattern." }
    }
}
