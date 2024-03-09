package pt.isel.ls.domain

import kotlinx.datetime.LocalDateTime

/**
 * Represents a game session with specified capacity, game identifier, date, and UUID.
 *
 * @param capacity The maximum number of players allowed in the session.
 * @param gid The identifier of the game associated with the session.
 * @param date The date and time of the session.
 * @param uuid The universally unique identifier (UUID) of the session.
 * @throws IllegalArgumentException If the capacity is zero.
 */
data class Session(
    override val uuid: UInt? = null,
    val capacity: UInt,
    val gid: UInt,
    val date: LocalDateTime.Companion,
    val players: List<Player> = listOf(),
) : Domain(uuid = uuid) {
    init {
        require(capacity > 0u) { "Capacity must be greater than 0" }
    }

    companion object {
        val hash: Int = hashCode()
    }
}
