package pt.isel.ls.webApi

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.services.PlayerServices
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Processes the genres string and returns a collection of genres.
 *
 * @param genres The string containing the genres.
 * @return The collection of genres.
 */
fun processGenres(genres: String): Collection<String> =
    URLDecoder
        .decode(genres, StandardCharsets.UTF_8.toString())
        .split(",")

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
                    createJsonMessage(errorMsg, it),
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
internal fun dateVerification(date: String?): LocalDate? {
    return try {
        date?.toLocalDate()
    } catch (e: IllegalArgumentException) {
        null
    }
}

/**
 * Reads a query from a request.
 *
 * @param elem The element to be read.
 * @return The query string.
 */
fun Request.readQuery(elem: String): String? {
    val query = query(elem) ?: return null
    return URLDecoder.decode(query, StandardCharsets.UTF_8.toString())
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

/**
 * Represents a message.
 *
 * @property msg The message.
 * @property error The error message. Can be null if there isn´t any information associated.
 */
@Serializable
data class Message(
    val msg: String,
    val error: String? = null,
)

/**
 * Creates a JSON message.
 *
 * @param message The message to be converted to JSON.
 */
internal fun createJsonMessage(
    message: String,
    error: String? = null,
): String {
    val messageObject = Message(message, error)
    return Json.encodeToString(messageObject)
}

/**
 * Creates a JSON message with the invalid parameters.
 *
 * @param gid The game id.
 * @param date The date.
 * @param capacity The capacity.
 */
internal fun invalidParamsRspCreateSession(
    gid: UInt?,
    date: LocalDate?,
    capacity: UInt?,
): String {
    val errorMsgs =
        listOfNotNull(
            if (gid == null) "'gid'" else null,
            if (date == null) "'date'" else null,
            if (capacity == null) "'capacity'" else null,
        )
    return if (errorMsgs.isNotEmpty()) {
        val errorMsg = errorMsgs.joinToString(", ")
        createJsonMessage("Missing or invalid $errorMsg. Please provide $errorMsg as valid values.")
    } else {
        createJsonMessage("Invalid request.")
    }
}

/**
 * Creates an unauthorized response.
 *
 * @param reason The reason for the unauthorized response.
 */
internal fun unauthorizedResponse(reason: String): Response =
    makeResponse(
        Status.UNAUTHORIZED,
        createJsonMessage("Unauthorized, $reason."),
    )
