package pt.isel.ls.domain

/**
 * Enum class representing the possible states of a session.
 *
 * The session can be either:
 * - OPEN: Indicates that there is room for more players in the session.
 * - CLOSE: Indicates that the session is at maximum capacity.
 */
enum class SessionState {
    OPEN,
    CLOSE,
}
