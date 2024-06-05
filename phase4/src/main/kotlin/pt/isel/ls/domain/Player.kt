package pt.isel.ls.domain

import kotlinx.serialization.Serializable
import org.eclipse.jetty.util.security.Password
import pt.isel.ls.domain.info.email_password
import pt.isel.ls.domain.info.name_username
import pt.isel.ls.storage.serializer.PasswordSerializer
import pt.isel.ls.storage.serializer.UUIDSerializer
import java.util.UUID

/**
 * Represents a Player.
 *
 * @param pid the player’s identifier (unique).
 * @param name the name of the player.
 * @param email the unique email of a player.
 * @param token the access token of each player.
 * @param username the username of the player (unique).
 * @param password the password chosen by the player.
 * @throws IllegalArgumentException if the name or username is empty.
 */
@Serializable
data class Player(
    val pid: UInt? = null,
    val name: String,
    val username: String,
    val email: Email,
    @Serializable(with = PasswordSerializer::class)
    val password: Password,
    @Serializable(with = UUIDSerializer::class)
    val token: UUID = UUID.randomUUID(),
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank." }
        require(username.isNotBlank()) { "Username must not be blank." }
    }
}

/**
 * Associates a [name_username] with a [email_password] to create a [Player].
 *
 * @receiver the [name_username] that represents the name and username if exits.
 * @param emailPass the [email_password] that represents the email and password.
 *
 * @return a [Player] with the name, username, email, password and a random token.
 */
infix fun name_username.associateWith(emailPass: email_password) =
    Player(
        name = first,
        username = second ?: first,
        email = Email(emailPass.first),
        password = Password(emailPass.second),
    )
