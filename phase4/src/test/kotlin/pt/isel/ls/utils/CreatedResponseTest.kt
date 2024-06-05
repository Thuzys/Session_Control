package pt.isel.ls.utils

import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.webApi.response.createdResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class CreatedResponseTest {
    @Test
    fun `make created response test`() {
        val response = createdResponse("Created response test")
        val expected =
            Response(Status.CREATED)
                .body("Created response test")
                .header("Content-Type", "application/json")
        assertEquals(expected, response)
    }
}
