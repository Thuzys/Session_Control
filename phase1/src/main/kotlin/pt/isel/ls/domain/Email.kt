package pt.isel.ls.domain

import pt.isel.ls.utils.validateEmail

data class Email(val email: String) {
    init {
        require(validateEmail(email))
    }
}
