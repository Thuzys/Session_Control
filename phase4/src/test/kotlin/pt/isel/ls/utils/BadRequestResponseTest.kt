package pt.isel.ls.utils

import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.webApi.badRequestResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class BadRequestResponseTest {
    @Test
    fun `make bad request response`() {
        val response = badRequestResponse("test")
        val expected =
            Response(Status.BAD_REQUEST)
                .header("Content-Type", "application/json")
                .body("{\"msg\":\"Bad Request, test.\"}")
        assertEquals(expected, response)
    }
}
