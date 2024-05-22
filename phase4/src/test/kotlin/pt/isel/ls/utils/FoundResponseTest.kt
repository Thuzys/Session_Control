package pt.isel.ls.utils

import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.webApi.response.foundResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class FoundResponseTest {
    @Test
    fun `make found response`() {
        val response = foundResponse("test")
        val expected =
            Response(Status.FOUND)
                .header("Content-Type", "application/json")
                .body("test")
        assertEquals(expected, response)
    }
}
