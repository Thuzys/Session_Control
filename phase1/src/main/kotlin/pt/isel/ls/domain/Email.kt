package pt.isel.ls.domain

import pt.isel.ls.utils.validateEmail

/**
 * Represents an Email.
 *
 * @param email the [String] containing the email.
 * @throws IllegalArgumentException if the given email is not valid.
 */
data class Email(val email: String) {
    init {
        require(validateEmail(email)) { "invalid email pattern." }
    }
}
