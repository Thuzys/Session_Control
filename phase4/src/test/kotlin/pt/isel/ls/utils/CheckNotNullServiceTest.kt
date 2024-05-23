package pt.isel.ls.utils

import org.junit.jupiter.api.Assertions.assertEquals
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.services.checkValidService
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CheckNotNullServiceTest {
    @Test
    fun `assert check not null service throws exception when condition is false`() {
        // Arrange
        val condition = false
        val lazyMessage = { "Error message" }
        // Act
        val exception =
            assertFailsWith<ServicesError> {
                checkValidService(condition, lazyMessage)
            }
        // Assert
        assertEquals("Error message", exception.message)
    }

    @Test
    fun `assert check not null service does not throw exception when condition is true`() {
        // Arrange
        val condition = true
        val lazyMessage = { "Error message" }
        // Act
        checkValidService(condition, lazyMessage)
        // Assert
        // No exception is thrown
    }
}
