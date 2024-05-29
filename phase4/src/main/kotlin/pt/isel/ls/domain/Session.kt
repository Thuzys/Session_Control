package pt.isel.ls.domain

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import pt.isel.ls.domain.info.GameInfo
import pt.isel.ls.domain.info.PlayerInfo

/**
 * Represents a game session with specified capacity, game identifier, date, and UUID.
 *
 * @param sid The universally unique identifier of the session.
 * @param capacity The maximum number of players allowed in the session.
 * @param gameInfo The game being played in the session.
 * @param date The date and time of the session.
 * @param owner The player that created the session.
 * @param players Collection of players currently in the session.
 * @throws IllegalArgumentException If the capacity is zero or lower.
 */
@Serializable
data class Session(
    val sid: UInt? = null,
    val capacity: UInt,
    val gameInfo: GameInfo,
    val date: LocalDate,
    val owner: PlayerInfo,
    val players: Collection<PlayerInfo> = listOf(),
) {
    init {
        require(capacity > 0u) { "Capacity must be greater than 0" }
    }
}
