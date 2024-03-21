package pt.isel.ls.utils

import pt.isel.ls.domain.errors.ServicesError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TryCatchTest {
    @Test
    fun `test of tryCatch without an exception`() {
        val result = tryCatch("test") { 1 }
        assert(result == 1)
    }

    @Test
    fun `test of tryCatch with IllegalStateException`() {
        assertFailsWith<ServicesError> {
            tryCatch("test") {
                throw IllegalStateException("the reason.")
            }
        }
    }

    @Test
    fun `test of tryCatch with IllegalArgumentException`() {
        assertFailsWith<ServicesError> {
            tryCatch("test") {
                throw IllegalArgumentException("the reason.")
            }
        }
    }

    @Test
    fun `test of tryCatch with NoSuchElementException`() {
        assertFailsWith<ServicesError> {
            tryCatch("test") {
                throw NoSuchElementException("the reason.")
            }
        }
    }

    @Test
    fun `test of tryCatch with exception message`() {
        assertEquals(
            expected = "test: the reason.",
            actual =
                runCatching {
                    tryCatch("test") { throw IllegalStateException("the reason.") }
                }.exceptionOrNull()?.message,
        )
    }
}
