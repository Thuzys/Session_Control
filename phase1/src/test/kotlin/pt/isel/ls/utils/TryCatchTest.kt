package pt.isel.ls.utils

import pt.isel.ls.domain.errors.ServicesError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TryCatchTest {
    @Test
    fun `tryCatch without exception test`() {
        val result = tryCatch("test") { 1 }
        assert(result == 1)
    }

    @Test
    fun `tryCatch with IllegalStateException test`() {
        assertFailsWith<ServicesError> {
            tryCatch("test") {
                throw IllegalStateException("the reason.")
            }
        }
    }

    @Test
    fun `tryCatch with IllegalArgumentException test`() {
        assertFailsWith<ServicesError> {
            tryCatch("test") {
                throw IllegalArgumentException("the reason.")
            }
        }
    }

    @Test
    fun `tryCatch with NoSuchElementException test`() {
        assertFailsWith<ServicesError> {
            tryCatch("test") {
                throw NoSuchElementException("the reason.")
            }
        }
    }

    @Test
    fun `tryCatch with exception message test`() {
        assertEquals(
            expected = "test: the reason.",
            actual =
                runCatching {
                    tryCatch("test") { throw IllegalStateException("the reason.") }
                }.exceptionOrNull()?.message,
        )
    }
}
