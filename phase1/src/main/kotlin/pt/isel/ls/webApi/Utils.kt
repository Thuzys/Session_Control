package pt.isel.ls.webApi

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.services.PlayerServices

/**
 * Processes the genres string and returns a collection of genres.
 *
 * @param genres The string containing the genres.
 * @return The collection of genres.
 */
fun processGenres(genres: String): Collection<String> = genres.split(",")

/**
 * Gets a parameter from a request.
 *
 * @param request The request from which the parameter is to be retrieved.
 * @param parameter The parameter to be retrieved.
 * @return The parameter from the request.
 */
internal fun getParameter(
    request: Request,
    parameter: String,
): String? = request.query(parameter)

/**
 * Reads the body of a request and returns it as a map.
 *
 * @param request The request from which the body is to be read.
 * @return The body of the request as a map.
 */
internal fun readBody(request: Request): Map<String, String> {
    val body = request.bodyString()
    body.ifBlank { return emptyMap() }
    return body
        .filter { it !in setOf('{', '}') }
        .split(", ")
        .associate {
            val (key, value) = it.split(":", limit = 2)
            key to value.trim()
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
internal inline fun tryResponse(
    errorStatus: Status,
    errorMsg: String,
    block: () -> Response,
): Response =
    try {
        block()
    } catch (e: ServicesError) {
        e.message
            ?.let { makeResponse(errorStatus, "$errorMsg: $it") }
            ?: makeResponse(errorStatus, "$errorMsg.")
    }

/**
 * Creates a response with a given status and message.
 *
 * @param status The status of the response.
 * @param msg The message of the response.
 * @return The response with the given status and message.
 */
internal fun makeResponse(
    status: Status,
    msg: String,
): Response = Response(status).body(msg).header("Content-Type", "application/json")

/**
 * Verifies and parses a date string into a LocalDateTime object.
 *
 * @param date A string representing the date.
 * @return A LocalDateTime object parsed from the input date string, or null if the input date is invalid.
 */
internal fun dateVerification(date: String?): LocalDateTime? {
    return try {
        date?.toLocalDateTime()
    } catch (e: IllegalArgumentException) {
        null
    }
}

/**
 * Get the pid of a query request, if incapable return null.
 *
 * @return The player id ([UInt]) or null.
 */
internal fun Request.toPidOrNull(): UInt? = query("pid")?.toUIntOrNull()

/**
 * Verifies if the request has a valid token.
 *
 * @param request The request to be verified.
 * @param playerManagement The player management service.
 * @return The error message if the token is invalid, or null if the token is valid.
 */
internal fun unauthorizedAccess(
    request: Request,
    playerManagement: PlayerServices,
): String? =
    request.query("token")
        ?.let {
            return if (playerManagement.isValidToken(it)) null else "invalid token."
        } ?: "token not found."
