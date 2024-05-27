package pt.isel.ls.domain.errors

/**
 * Represents an error that occurs when a service operation fails.
 *
 * This class extends [IllegalStateException] and provides a specific type of error that can be thrown when a service operation fails.
 *
 * @param msg The detailed message of this error.
 */
open class ServicesError(msg: String?) : IllegalStateException(msg)
