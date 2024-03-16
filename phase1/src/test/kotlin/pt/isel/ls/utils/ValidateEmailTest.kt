package pt.isel.ls.utils

import kotlin.test.Test
import kotlin.test.assertFalse

class ValidateEmailTest {
    @Test
    fun `email without starting letters`() {
        val email = "@gmail.com"
        assertFalse { validateEmail(email) }
    }

    @Test
    fun `email without @`() {
        val email = "xptogmail.com"
        assertFalse { validateEmail(email) }
    }

    @Test
    fun `email without specified mail service`() {
        val email = "xpto@.com"
        assertFalse { validateEmail(email) }
    }

    @Test
    fun `email without dot`() {
        val email = "xpto@gmailcom"
        assertFalse { validateEmail(email) }
    }

    @Test
    fun `email without Top-Level Domain`() {
        val email = "xpto@gmail."
        assertFalse { validateEmail(email) }
    }
}
