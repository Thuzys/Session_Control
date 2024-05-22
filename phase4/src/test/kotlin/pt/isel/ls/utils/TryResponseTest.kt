package pt.isel.ls.utils

import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.webApi.createJsonRspMessage
import pt.isel.ls.webApi.response.tryResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class TryResponseTest {
    @Test
    fun `returning status ok with try response status test`() {
        val response =
            tryResponse(Status.OK, "Hello World!") {
                Response(Status.OK).body("Hello World!")
            }
        assertEquals(response.status, Status.OK)
    }

    @Test
    fun `comparing contend of try response body test`() {
        val response =
            tryResponse(Status.OK, "Hello World!") {
                Response(Status.OK).body("Hello World!")
            }
        assertEquals(response.bodyString(), "Hello World!")
    }

    @Test
    fun `returning status internal server error on try response test with an exception`() {
        val response =
            tryResponse(Status.INTERNAL_SERVER_ERROR, "Hello World!") {
                throw ServicesError("an exception occurred!")
            }
        assertEquals(response.status, Status.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `comparing contend of try response body test with exception message`() {
        val response =
            tryResponse(Status.INTERNAL_SERVER_ERROR, "Hello World.") {
                throw ServicesError("an exception occurred.")
            }
        val expected =
            createJsonRspMessage("Hello World.", "an exception occurred.")
        assertEquals(expected, response.bodyString())
    }

    @Test
    fun `comparing contend of try response test with an exception without message`() {
        val response =
            tryResponse(Status.INTERNAL_SERVER_ERROR, "Hello World") {
                throw ServicesError(null)
            }
        assertEquals("{\"msg\":\"Hello World.\"}", response.bodyString())
    }
}
