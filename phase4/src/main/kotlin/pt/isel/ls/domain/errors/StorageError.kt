package pt.isel.ls.domain.errors

/**
 * Represents an error that occurs when storing data.
 *
 * @property message The message of the error.
 */
class StorageError(message: String) : IllegalStateException(message)
