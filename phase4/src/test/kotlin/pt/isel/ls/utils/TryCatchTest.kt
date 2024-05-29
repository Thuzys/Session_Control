package pt.isel.ls.utils

import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.domain.errors.StorageError
import pt.isel.ls.services.tryCatch
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
                    tryCatch("test") { throw StorageError("the reason.") }
                }.exceptionOrNull()?.message,
        )
    }
}
