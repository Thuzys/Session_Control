package pt.isel.ls.domain.info

import kotlinx.serialization.Serializable

/**
 * Represents a game with its identifier and name.
 *
 * @param gid The identifier of the game.
 * @param name The name of the game.
 */
@Serializable
data class GameInfo(
    val gid: UInt,
    val name: String? = null,
)
