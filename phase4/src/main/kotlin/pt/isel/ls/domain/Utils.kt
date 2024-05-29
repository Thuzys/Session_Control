package pt.isel.ls.domain

/**
 * Represents a valid email address pattern.
 */
private val emailPattern: Regex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)".toRegex()

/**
 * Validates if an email has the correct pattern.
 *
 * An email is considered to have the correct pattern if it follows the standard format:
 * - Starts with one or more letters (uppercase or lowercase).
 * - Contains an '@' symbol.
 * - Followed by one or more characters.
 * - Contains a dot ('.') symbol.
 * - Ends with one or more characters.
 *
 * @param email The email to be validated.
 * @return true if the email has the correct pattern, false otherwise.
 */
internal fun validateEmail(email: String): Boolean = email.matches(emailPattern)

/**
 * Represents a valid password pattern.
 */
private val passwordPattern: Regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$".toRegex()

/**
 * Validates if a password has the correct pattern.
 *
 * A password is considered to have the correct pattern if it follows the standard format:
 * - Contains at least one letter.
 * - Contains at least one digit.
 * - Contains at least 8 characters.
 *
 * @param password The password to be validated.
 * @return true if the password has the correct pattern, false otherwise.
 */
internal fun validatePassword(password: String): Boolean = password.matches(passwordPattern)
