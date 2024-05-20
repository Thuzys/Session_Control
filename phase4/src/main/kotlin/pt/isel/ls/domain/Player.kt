package pt.isel.ls.domain

import kotlinx.serialization.Serializable
import org.eclipse.jetty.util.security.Password
import pt.isel.ls.domain.info.CreatePlayerEmailPasswordParam
import pt.isel.ls.domain.info.CreatePlayerNameParam
import pt.isel.ls.storage.serializer.PasswordSerializer
import pt.isel.ls.storage.serializer.UUIDSerializer
import java.util.UUID

/**
 * Represents a Player.
 *
 * @param pid the player’s identifier (unique).
 * @param name the UserName of the player.
 * @param email the unique email of a player.
 * @param token the access token of each player.
 * @param username the UserName of the player.
 * @throws IllegalArgumentException if the name is empty.
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
        require(username.isNotBlank()) { "UserName must not be blank." }
    }
}

/**
 * Associates a [CreatePlayerNameParam] with a [CreatePlayerEmailPasswordParam] to create a [Player].
 *
 * @receiver the [CreatePlayerNameParam] that represents the name and username if exits.
 * @param emailPass the [CreatePlayerEmailPasswordParam] that represents the email and password.
 *
 * @return a [Player] with the name, username, email, password and a random token.
 */
infix fun CreatePlayerNameParam.associateWith(emailPass: CreatePlayerEmailPasswordParam) =
    Player(
        name = first,
        username = second ?: first,
        email = Email(emailPass.first),
        password = Password(emailPass.second),
    )
