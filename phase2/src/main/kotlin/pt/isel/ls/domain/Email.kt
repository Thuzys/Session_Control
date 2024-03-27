package pt.isel.ls.domain

import kotlinx.serialization.Serializable

/**
 * Represents an Email.
 *
 * @param email the [String] containing the email.
 * @throws IllegalArgumentException if the given email is not valid.
 */
@Serializable
data class Email(val email: String) {
    init {
        require(validateEmail(email)) { "Invalid email pattern." }
    }
}
