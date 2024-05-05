package pt.isel.ls.domain

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * Represents a game session with specified capacity, game identifier, date, and UUID.
 *
 * @param sid The universally unique identifier (UUID) of the session.
 * @param capacity The maximum number of players allowed in the session.
 * @param gid The identifier of the game associated with the session.
 * @param date The date and time of the session.
 * @param players Collection of players currently in the session.
 * @throws IllegalArgumentException If the capacity is zero.
 */
@Serializable
data class Session(
    val sid: UInt? = null,
    val capacity: UInt,
    val gid: UInt,
    val date: LocalDate,
    val players: Collection<Player> = listOf(),
) {
    init {
        require(capacity > 0u) { "Capacity must be greater than 0" }
    }
}

/**
 * Adds a player to the session.
 *
 * @param player The player to add.
 * @throws IllegalStateException if the session is already at maximum capacity.
 */
fun Session.addPlayer(player: Player): Session {
    check(players.size + 1 != capacity.toInt()) { "Session is already at maximum capacity" }
    return this.copy(players = players + player)
}
