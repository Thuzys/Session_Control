package pt.isel.ls.domain

import kotlin.test.Test
import kotlin.test.assertFailsWith

class EmailTest {
    @Test
    fun `good email test`() {
        Email("good@emailTest.com")
    }

    @Test
    fun `bad email test`() {
        assertFailsWith<IllegalArgumentException> { Email("badEmail") }
    }
}
