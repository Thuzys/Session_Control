package pt.isel.ls.utils

import pt.isel.ls.domain.errors.DataMemError
import pt.isel.ls.domain.errors.ServicesError

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
