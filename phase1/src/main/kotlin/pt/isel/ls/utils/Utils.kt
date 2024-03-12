package pt.isel.ls.utils

import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.DataMemError
import pt.isel.ls.domain.errors.ServicesError

private val emailPattern: Regex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)".toRegex()

/**
 * Validates if an email has the correct pattern.
 *
 * An email is considered to have the correct pattern if it follows the standard format:
 * - Starts with one or more letters (uppercase or lowercase).
 * - Contains an '@' symbol.
 * - Followed by one or more characters.
 * - Contains a dot ('.') symbol.
 * - Ends with one or more characters.
 *
 * @param email The email to be validated.
 * @return true if the email has the correct pattern, false otherwise.
 */
fun validateEmail(email: String): Boolean = email.matches(emailPattern)

/**
 * Determines the state of a session.
 *
 * @param session The session for which the state is being determined.
 * @return The state of the session (OPEN or CLOSE).
 */
fun getSessionState(session: Session): SessionState {
    return if (session.players.size.toUInt() < session.capacity) SessionState.OPEN else SessionState.CLOSE
}

/**
 * Tries to execute a block of code and catches any [DataMemError] thrown.
 *
 * @param msg The message to be displayed in case of an error.
 * @param block The block of code to be executed.
 * @return The result of the block of code.
 * @throws ServicesError containing the message of the error.
 */
internal inline fun <T> tryCatch(
    msg: String,
    block: () -> T,
): T =
    try {
        block()
    } catch (error: DataMemError) {
        throw ServicesError("$msg: ${error.message}")
    } catch (domainError: IllegalArgumentException) {
        throw ServicesError("$msg: ${domainError.message}")
    } catch (domainError: IllegalStateException) {
        throw ServicesError("$msg: ${domainError.message}")
    }
