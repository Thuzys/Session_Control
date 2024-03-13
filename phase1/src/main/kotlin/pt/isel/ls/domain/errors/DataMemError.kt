package pt.isel.ls.domain.errors

/**
 * Represents an error that occurs when a data memory operation fails.
 *
 * This class extends [IllegalArgumentException] and provides a specific type of error that can be thrown when a data memory operation fails.
 *
 * @param msg The detailed message of this error.
 */
open class DataMemError(msg: String) : IllegalArgumentException(msg)
