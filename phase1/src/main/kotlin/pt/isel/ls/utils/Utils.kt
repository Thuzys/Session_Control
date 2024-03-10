package pt.isel.ls.utils

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
fun validateEmail(email: String): Boolean = email.matches(emailPattern)

/**
 * Represents the services made by the application.
 * @param msg the memory container to storage all the data.
 * @param block the code to be executed.
 * @throws IllegalStateException containing the message of the error.
 */
inline fun <T> tryCatch(
    msg: String,
    block: () -> T,
): T =
    try {
        block()
    } catch (error: IllegalArgumentException) {
        error("$msg: ${error.message}")
    }
