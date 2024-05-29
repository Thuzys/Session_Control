package pt.isel.ls.domain.errors

/**
 * Represents an error that occurs when a service operation fails due an invalid param.
 *
 * This class extends [ServicesError]
 * and provides a specific type of error that can be thrown when a service operation fails.
 *
 * @param msg The detailed message of this error.
 */
class ParamError(msg: String) : ServicesError(msg)
