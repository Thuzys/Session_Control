package pt.isel.ls.domain.info

import kotlinx.serialization.Serializable

/**
 * The information about the player.
 * Used to retrieve a session.
 *
 * [UInt] is the identifier of the player.
 * [String] is the username of the player.
 */
typealias PlayerInfoParam = Pair<UInt?, String?>

/**
 * The information to create a player.
 * Used to create a new player.
 *
 * - first [String] is the name of the player.
 * - second [String] is the username of the player, if null the name will be used as username.
 */
typealias name_username = Pair<String, String?>

/**
 * The information to create a player.
 * Used to create a new player.
 *
 * - first [String] is the email of the player.
 * - second [String] is the password of the player.
 */
typealias email_password = Pair<String, String>

/**
 * Represents the player information with a specified identifier, username, and email.
 *
 * @param pid The unique identifier of the player.
 * @param username The username of the player.
 */
@Serializable
data class PlayerInfo(
    val pid: UInt,
    val username: String,
)

/**
 * Represents the player information with a specified identifier, username, and email.
 *
 * @receiver The unique identifier of the player.
 * @param username The username of the player.
 */
infix fun UInt.associateWith(username: String): PlayerInfo = PlayerInfo(this, username)
