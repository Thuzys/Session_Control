package pt.isel.ls.utils

import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DateVerificationTest {
    @Test
    fun `valid date parsing`() {
        val parsedDate = dateVerification("2024-03-16T12:30:00")
        assertEquals(LocalDateTime(2024, 3, 16, 12, 30, 0), parsedDate)
    }

    @Test
    fun `invalid date parsing`() {
        val parsedDate = dateVerification("invalid_date_format")
        assertNull(parsedDate)
    }
}
