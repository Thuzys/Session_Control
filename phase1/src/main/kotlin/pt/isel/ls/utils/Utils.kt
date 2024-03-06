package pt.isel.ls.utils

private val emailPattern: Regex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)".toRegex()

fun validateEmail(email: String): Boolean = email.matches(emailPattern)
