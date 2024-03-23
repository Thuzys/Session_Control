package pt.isel.ls.utils

import pt.isel.ls.domain.validateEmail
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateEmailTest {
    @Test
    fun `returning false with an email without starting letters`() {
        val email = "@gmail.com"
        assertFalse { validateEmail(email) }
    }

    @Test
    fun `returning false with an email without @`() {
        val email = "xptogmail.com"
        assertFalse { validateEmail(email) }
    }

    @Test
    fun `retuning false with an email without specified mail service`() {
        val email = "xpto@.com"
        assertFalse { validateEmail(email) }
    }

    @Test
    fun `returning false with an email without dot`() {
        val email = "xpto@gmailcom"
        assertFalse { validateEmail(email) }
    }

    @Test
    fun `returning false with an email without Top-Level Domain`() {
        val email = "xpto@gmail."
        assertFalse { validateEmail(email) }
    }

    @Test
    fun `returning true with a valid email`() {
        val email = "test@email.com"
        assertTrue { validateEmail(email) }
    }
}
