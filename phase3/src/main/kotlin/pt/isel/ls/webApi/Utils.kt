package pt.isel.ls.webApi

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path
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
 * Reads the body of a request and returns it as a map.
 *
 * @param request The request from which the body is to be read.
 * @return The body of the request as a map.
 */
internal fun readBody(request: Request): Map<String, String> {
    val body = request.bodyString()
    body.ifBlank { return emptyMap() }
    val keyValueRegex = Regex("\"(\\w+)\":\\s*\"(.*?)\"")
    val matchResults = keyValueRegex.findAll(body)
    return matchResults.associate {
        val (key, value) = it.destructured
        key to value
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
            ?.let {
                makeResponse(
                    errorStatus,
                    """
                    Error:$errorMsg
                    Cause:$it
                    """.trimIndent(),
                )
            }
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
        date?.replace('_', ':')?.toLocalDateTime()
    } catch (e: IllegalArgumentException) {
        null
    }
}

/**
 * Get the pid of a query request, if incapable return null.
 *
 * @return The player id ([UInt]) or null.
 */
internal fun Request.toPidOrNull(): UInt? = path("pid")?.toUIntOrNull()

/**
 * Get the sid of a query request, if incapable return null.
 *
 * @return The session id ([UInt]) or null.
 */
internal fun Request.toSidOrNull(): UInt? = path("sid")?.toUIntOrNull()

/**
 * Get the gid of a query request, if incapable return null.
 *
 * @return The game id ([UInt]) or null.
 */
internal fun Request.toGidOrNull(): UInt? = path("gid")?.toUIntOrNull()

/**
 * Verifies if the request has a valid token.
 *
 * @param request The request to be verified.
 * @param pManagement The player management service.
 * @return The error message if the token is invalid, or null if the token is valid.
 */
internal fun unauthorizedAccess(
    request: Request,
    pManagement: PlayerServices,
): String? =
    request.header("authorization")?.removePrefix("Bearer ")
        ?.let { return if (pManagement.isValidToken(it)) null else "invalid token" } ?: "token not provided"
