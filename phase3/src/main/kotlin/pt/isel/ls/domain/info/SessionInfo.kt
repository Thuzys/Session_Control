package pt.isel.ls.domain.info

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import pt.isel.ls.domain.Session

/**
 * Represents the [Session] information with specified game identifier, date, sid and owner.
 *
 * @param sid The unique identifier of the session.
 * @param owner The information about the session owner.
 * @param gameInfo The game being played in the session.
 * @param date The date of the session
 */
@Serializable
data class SessionInfo(
    val sid: UInt,
    val owner: PlayerInfo,
    val gameInfo: GameInfo,
    val date: LocalDate,
)
