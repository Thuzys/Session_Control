package pt.isel.ls.webApi.response

import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.webApi.createJsonRspMessage

/**
 * Creates an unauthorized response.
 *
 * @param reason The reason for the unauthorized response.
 * @return The response with the given status and message.
 */
internal fun unauthorizedResponse(reason: String): Response =
    makeResponse(Status.UNAUTHORIZED, createJsonRspMessage("Unauthorized, $reason."))

/**
 * Creates a bad request response.
 *
 * @param reason The reason for the bad request response.
 * @return The response with the given status and message.
 */
internal fun badResponse(reason: String): Response =
    makeResponse(
        Status.BAD_REQUEST,
        createJsonRspMessage("Bad Request, $reason."),
    )

/**
 * Creates a Found response.
 *
 * @param message The message of the response.
 * @return The response with the given status and message.
 */
internal fun foundResponse(message: String) = makeResponse(Status.FOUND, message)

/**
 * Creates a Created response.
 *
 * @param message The message of the response.
 * @return The response with the given status and message.
 */
internal fun createdResponse(message: String) = makeResponse(Status.CREATED, message)

/**
 * Creates an Ok response.
 *
 * @param message The message of the response.
 * @return The response with the given status and message.
 */
internal fun okResponse(message: String) = makeResponse(Status.OK, message)
