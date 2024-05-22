package pt.isel.ls.webApi.response

import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.webApi.createJsonRspMessage

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
                    createJsonRspMessage(errorMsg, it),
                )
            }
            ?: makeResponse(errorStatus, createJsonRspMessage("$errorMsg."))
    }
