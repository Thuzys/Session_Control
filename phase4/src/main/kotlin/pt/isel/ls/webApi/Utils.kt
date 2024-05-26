package pt.isel.ls.webApi

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.routing.path
import pt.isel.ls.services.PlayerServices
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.UUID

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
 * Get the token of a request, if incapable return null.
 *
 * @return The token ([UUID]) or null.
 */
internal fun Request.toTokenOrNull(): UUID? {
    val token = header("authorization")?.removePrefix("Bearer ")
    return token?.let { UUID.fromString(it) }
}

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
    request
        .header("authorization")
        ?.removePrefix("Bearer ")
        ?.let { return if (pManagement.isValidToken(it)) null else "invalid token" }
        ?: "token not provided"

/**
 * Represents a message.
 *
 * @property msg The message.
 * @property error The error message. Can be null if there isn´t any information associated.
 */
@Serializable
private data class Message(
    val msg: String,
    val error: String? = null,
    val id: UInt? = null,
)

/**
 * Creates a JSON message.
 *
 * @param message The message to be converted to JSON.
 */
internal fun createJsonRspMessage(
    message: String,
    error: String? = null,
    id: UInt? = null,
): String = Json.encodeToString(Message(message, error, id))

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
    val errorMessages =
        listOfNotNull(
            if (gid == null) "'gid'" else null,
            if (date == null) "'date'" else null,
            if (capacity == null) "'capacity'" else null,
        )
    return if (errorMessages.isNotEmpty()) {
        val errorMsg = errorMessages.joinToString(", ")
        createJsonRspMessage("Missing or invalid $errorMsg. Please provide $errorMsg as valid values.")
    } else {
        createJsonRspMessage("Invalid request.")
    }
}
