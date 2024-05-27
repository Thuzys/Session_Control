package pt.isel.ls.utils

import org.junit.jupiter.api.Assertions.assertEquals
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.services.checkNotNullService
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CheckNotNullServiceTest {
    @Test
    fun `assert check not null service throws exception when condition is false`() {
        // Arrange
        val condition: String? = null
        val lazyMessage = { "Error message" }
        // Act
        val exception =
            assertFailsWith<ServicesError> {
                checkNotNullService(condition, lazyMessage)
            }
        // Assert
        assertEquals("Error message", exception.message)
    }

    @Test
    fun `assert check not null service does not throw exception when condition is true`() {
        // Arrange
        val condition = "not null"
        val lazyMessage = { "Error message" }
        // Act
        checkNotNullService(condition, lazyMessage)
        // Assert
        // No exception is thrown
    }
}
