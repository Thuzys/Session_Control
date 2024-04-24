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

/**
 * Extension function to parse a string into a SessionState enum value.
 *
 * @return The corresponding SessionState enum value, or null if the string does not match any known state.
 */
fun String?.toSessionState(): SessionState? {
    return when (this?.uppercase()) {
        "OPEN" -> SessionState.OPEN
        "CLOSE" -> SessionState.CLOSE
        else -> null
    }
}
