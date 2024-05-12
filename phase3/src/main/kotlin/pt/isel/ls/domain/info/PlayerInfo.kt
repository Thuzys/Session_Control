package pt.isel.ls.domain.info

import kotlinx.serialization.Serializable

/**
 * Represents the player information with a specified identifier, username, and email.
 *
 * @param pid The unique identifier of the player.
 * @param userName The username of the player.
 */
@Serializable
data class PlayerInfo(
    val pid: UInt,
    val userName: String,
)
