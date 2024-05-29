package pt.isel.ls.utils

import org.junit.jupiter.api.Assertions.assertEquals
import pt.isel.ls.domain.errors.ParamError
import pt.isel.ls.services.requireValidParam
import kotlin.test.Test
import kotlin.test.assertFailsWith

class RequireValidParamTest {
    @Test
    fun `assert check valid service throws exception when condition is false`() {
        // Arrange
        val condition = false
        val lazyMessage = { "Error message" }
        // Act
        val exception =
            assertFailsWith<ParamError> {
                requireValidParam(condition, lazyMessage)
            }
        // Assert
        assertEquals("Error message", exception.message)
    }

    @Test
    fun `assert check valid service does not throw exception when condition is true`() {
        // Arrange
        val condition = true
        val lazyMessage = { "Error message" }
        // Act
        requireValidParam(condition, lazyMessage)
        // Assert
        // No exception is thrown
    }
}
