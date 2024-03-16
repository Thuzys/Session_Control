package pt.isel.ls.utils

import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.domain.errors.ServicesError
import kotlin.test.Test
import kotlin.test.assertEquals

class TryResponseTest {
    @Test
    fun `try response status test`() {
        val response =
            tryResponse(Status.OK, "Hello World!") {
                Response(Status.OK).body("Hello World!")
            }
        assertEquals(response.status, Status.OK)
    }

    @Test
    fun `try response body test`() {
        val response =
            tryResponse(Status.OK, "Hello World!") {
                Response(Status.OK).body("Hello World!")
            }
        assertEquals(response.bodyString(), "Hello World!")
    }

    @Test
    fun `try response status test with exception`() {
        val response =
            tryResponse(Status.INTERNAL_SERVER_ERROR, "Hello World!") {
                throw ServicesError("an exception occurred!")
            }
        assertEquals(response.status, Status.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `try response body test with exception`() {
        val response =
            tryResponse(Status.INTERNAL_SERVER_ERROR, "Hello World!") {
                throw ServicesError("an exception occurred!")
            }
        assertEquals(response.bodyString(), "an exception occurred!")
    }

    @Test
    fun `try response test with exception and custom error`() {
        val response =
            tryResponse(Status.INTERNAL_SERVER_ERROR, "Hello World!") {
                throw ServicesError(null)
            }
        assertEquals(response.bodyString(), "Hello World!")
    }
}
