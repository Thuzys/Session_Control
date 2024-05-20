package pt.isel.ls.domain.info

import kotlinx.serialization.Serializable
import pt.isel.ls.storage.serializer.UUIDSerializer
import java.util.UUID

/**
 * Represents the authentication of a player.
 * This class provides the player's identifier and token.
 *
 * @property pid the identifier of each player.
 * @property token the token of the player.
 */
@Serializable
data class PlayerAuthentication(
    val pid: UInt,
    @Serializable(with = UUIDSerializer::class)
    val token: UUID,
)
