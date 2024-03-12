package pt.isel.ls.domain.errors

/**
 * Represents an error that occurs when reading data from the storage.
 *
 * This class extends [DataMemError] and provides a specific type of error that can be thrown when a read operation fails.
 *
 * @param msg The detailed message of this error.
 */
class ReadError(msg: String) : DataMemError(msg)
