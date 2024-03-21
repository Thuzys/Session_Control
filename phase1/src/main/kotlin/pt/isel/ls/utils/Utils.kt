package pt.isel.ls.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.UUID
import kotlin.NoSuchElementException

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
 * Gets a parameter from a request.
 *
 * @param request The request from which the parameter is to be retrieved.
 * @param parameter The parameter to be retrieved.
 * @return The parameter from the request.
 */
fun getParameter(
    request: Request,
    parameter: String,
): String? = request.query(parameter)

/**
 * Reads the body of a request and returns it as a map.
 *
 * @param request The request from which the body is to be read.
 * @return The body of the request as a map.
 */
fun readBody(request: Request): Map<String, String> {
    val body = request.bodyString()
    return if (body.isBlank()) {
        emptyMap()
    } else {
        body.filter { it !in setOf('{', '}') }
            .split(", ").associate {
                val (key, value) =
                    it.split(
                        ":",
                        limit = 2,
                    )
                key to value.trim()
            }
    }
}

/**
 * Creates a response with a given status and message.
 * The response is created by executing a block of code.
 * If an exception occurs, the response will have the given status and message.
 * Otherwise, the response will be the block of code results.
 *
 * @param errorStatus The status of the response.
 * @param errorMsg The message of the response.
 * @param block The block of code to be executed.
 * @return The response with the given status and message.
 */
inline fun tryResponse(
    errorStatus: Status,
    errorMsg: String,
    block: () -> Response,
): Response =
    try {
        block()
    } catch (e: ServicesError) {
        e.message?.let { makeResponse(errorStatus, "$errorMsg: $it") } ?: makeResponse(errorStatus, "$errorMsg.")
    }

/**
 * Creates a response with a given status and message.
 *
 * @param status The status of the response.
 * @param msg The message of the response.
 * @return The response with the given status and message.
 */
fun makeResponse(
    status: Status,
    msg: String,
): Response = Response(status).body(msg).header("Content-Type", "application/json")

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
        throw ServicesError("$msg: ${error.message}")
    } catch (domainError: IllegalArgumentException) {
        throw ServicesError("$msg: ${domainError.message}")
    } catch (domainError: IllegalStateException) {
        throw ServicesError("$msg: ${domainError.message}")
    } catch (storageError: SQLException) {
        throw ServicesError("$msg: ${storageError.message}")
    }

/**
 * Verifies and parses a date string into a LocalDateTime object.
 *
 * @param date A string representing the date.
 * @return A LocalDateTime object parsed from the input date string, or null if the input date is invalid.
 */
fun dateVerification(date: String?): LocalDateTime? {
    return try {
        date?.toLocalDateTime()
    } catch (e: IllegalArgumentException) {
        null
    }
}

/**
 * Executes a command on a connection.
 * If an exception occurs, the connection will be rolled back and the auto-commit will be set to true.
 * Otherwise, the connection will be committed and the auto-commit will be set to true.
 * @param cmd The command to be executed.
 * @throws SQLException if an exception occurs.
 */
fun <T> Connection.executeCommand(cmd: Connection.() -> T): T {
    try {
        autoCommit = false
        val response = cmd()
        autoCommit = true
        return response
    } catch (e: SQLException) {
        rollback()
        autoCommit = true
        throw e
    }
}

/**
 * Makes a list of [Player] objects from a [PreparedStatement].
 * @param stmt The [PreparedStatement] to make the list from.
 * @return A list of [Player] objects.
 */
fun makePlayers(stmt: PreparedStatement): Collection<Player> {
    val rs = stmt.executeQuery()
    val players = mutableListOf<Player>()
    while (rs.next()) {
        players.add(
            Player(
                rs.getInt("pid").toUInt(),
                rs.getString("name"),
                Email(rs.getString("email")),
                UUID.fromString(
                    rs.getString("token"),
                ),
            ),
        )
    }
    return players
}
