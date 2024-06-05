package pt.isel.ls.utils

import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.webApi.response.unauthorizedResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class UnauthorizedResponseTest {
    @Test
    fun `make unauthorized response`() {
        val response = unauthorizedResponse("test")
        val expected =
            Response(Status.UNAUTHORIZED)
                .header("Content-Type", "application/json")
                .body("{\"msg\":\"Unauthorized, test.\"}")
        assertEquals(expected, response)
    }
}
