package pt.isel.ls.utils

import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.webApi.response.okResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class OkResponseTest {
    @Test
    fun `make ok response`() {
        val response = okResponse("test")
        val expected =
            Response(Status.OK)
                .header("Content-Type", "application/json")
                .body("test")
        assertEquals(expected, response)
    }
}
