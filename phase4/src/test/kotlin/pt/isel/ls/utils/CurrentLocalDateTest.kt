package pt.isel.ls.utils

import kotlinx.datetime.toKotlinLocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import pt.isel.ls.services.currentLocalDate
import java.time.LocalDate
import kotlin.test.Test

class CurrentLocalDateTest {
    @Test
    fun `assert current local date is correct`() {
        // Arrange
        val expected = LocalDate.now().toKotlinLocalDate()
        // Act
        val actual = currentLocalDate()
        // Assert
        assertEquals(expected, actual)
    }
}
