package pt.isel.ls.utils

import kotlinx.datetime.LocalDate
import pt.isel.ls.webApi.dateVerification
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DateVerificationTest {
    @Test
    fun `valid date parsing`() {
        val parsedDate = dateVerification("2024-03-16")
        assertEquals(LocalDate(2024, 3, 16), parsedDate)
    }

    @Test
    fun `invalid date parsing`() {
        val parsedDate = dateVerification("invalid_date_format")
        assertNull(parsedDate)
    }
}
