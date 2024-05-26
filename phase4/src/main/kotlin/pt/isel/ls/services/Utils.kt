package pt.isel.ls.services

import kotlinx.datetime.toKotlinLocalDate
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ParamError
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.domain.errors.StorageError
import java.time.LocalDate
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

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
    } catch (storageError: StorageError) {
        throw ServicesError("$msg: ${treatResponse(storageError.message)}")
    }

/**
 * Treats the response message.
 *
 * @param msg The message to be treated.
 * @return The treated message.
 */
private fun treatResponse(msg: String?): String {
    return if (msg != null && msg.last() == '.') {
        msg
    } else {
        msg?.let { "$it." } ?: "An error occurred."
    }
}

/**
 * Checks if a value used as param is valid.
 *
 * @param condition The condition to be checked.
 * @param lazyMessage The message to be displayed in case of an error.
 * @throws ParamError containing the message of the error.
 */
internal inline fun requireValidParam(
    condition: Boolean,
    lazyMessage: () -> String,
) {
    if (!condition) {
        throw ParamError(lazyMessage())
    }
}

/**
 * Checks if a value used as param is not null.
 *
 * @see ExperimentalContracts
 *
 * @param value The value to be checked.
 * @param lazyMessage The message to be displayed in case of an error.
 * @return The value if it is not null.
 * @throws ParamError containing the message of the error.
 */
@OptIn(ExperimentalContracts::class)
internal inline fun <T> checkNotNullParam(
    value: T?,
    lazyMessage: () -> String,
): T {
    contract {
        returns() implies (value != null)
    }
    return value ?: throw ParamError(lazyMessage())
}

/**
 * Checks if a value is not null.
 *
 * @see ExperimentalContracts
 *
 * @param value The value to be checked.
 * @param lazyMessage The message to be displayed in case of an error.
 * @return The value if it is not null.
 * @throws ServicesError containing the message of the error.
 */
@OptIn(ExperimentalContracts::class)
internal inline fun <T> checkNotNullService(
    value: T?,
    lazyMessage: () -> String,
): T {
    contract {
        returns() implies (value != null)
    }
    return value ?: throw ServicesError(lazyMessage())
}

/**
 * Gets the current local date.
 *
 * @return The current local date.
 */
fun currentLocalDate(): kotlinx.datetime.LocalDate = LocalDate.now().toKotlinLocalDate()

/**
 * Determines the state of a session.
 *
 * @param session The session for which the state is being determined.
 * @return The state of the session (OPEN or CLOSE).
 */
internal fun getSessionState(session: Session): SessionState {
    val condition = session.players.size.toUInt() < session.capacity && session.date >= currentLocalDate()
    return if (condition) SessionState.OPEN else SessionState.CLOSE
}
