package pt.isel.ls.services

import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError
import java.sql.SQLException

const val DEFAULT_OFFSET = 0u
const val DEFAULT_LIMIT = 11u

/**
 * Tries to execute a block of code and catches any exception that may occur.
 *
 * @param msg The message to be displayed in case of an error.
 * @param block The block of code to be executed.
 * @return The resulting block of code.
 * @throws ServicesError containing the message of the error.
 */
internal inline fun <T> tryCatch(
    msg: String,
    block: () -> T,
): T =
    try {
        block()
    } catch (error: NoSuchElementException) {
        throw ServicesError("$msg: ${treatResponse(error.message)}")
    } catch (domainError: IllegalArgumentException) {
        throw ServicesError("$msg: ${treatResponse(domainError.message)}")
    } catch (domainError: IllegalStateException) {
        throw ServicesError("$msg: ${treatResponse(domainError.message)}")
    } catch (storageError: SQLException) {
        throw ServicesError("$msg: ${treatResponse(storageError.message)}")
    }

private fun treatResponse(msg: String?): String {
    return if (msg != null && msg.last() == '.') {
        msg
    } else {
        msg?.let { "$it." } ?: "An error occurred."
    }
}

/**
 * Determines the state of a session.
 *
 * @param session The session for which the state is being determined.
 * @return The state of the session (OPEN or CLOSE).
 */
internal fun getSessionState(session: Session): SessionState {
    return if (session.players.size.toUInt() < session.capacity) SessionState.OPEN else SessionState.CLOSE
}
