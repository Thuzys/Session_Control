package pt.isel.ls.domain

import kotlinx.datetime.LocalDateTime
import java.util.*


/**
 * Represents a game session with specified capacity, game identifier, date, and UUID.
 *
 * @param capacity The maximum number of players allowed in the session.
 * @param gid The identifier of the game associated with the session.
 * @param date The date and time of the session.
 * @param uuid The universally unique identifier (UUID) of the session.
 */
data class Session(
    val capacity: UInt,
    val gid: UInt,
    val date: LocalDateTime.Companion,
    val uuid: UInt,
    val players: List<Player> = listOf()
    ){
    /**
     * Adds a player to the session.
     *
     * @param player The player to add.
     * @throws IllegalStateException if the session is already at maximum capacity.
     */
    fun addPlayer(player: Player): Session{
        check(players.size + 1 != capacity.toInt()){ "Session is already at maximum capacity" }
        return this.copy(players = players + player)
    }
}

