package pt.isel.ls.utils

import org.http4k.core.Status
import pt.isel.ls.webApi.response.makeResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class MakeResponseTest {
    @Test
    fun `make response status test`() {
        val response = makeResponse(Status.OK, "OK")
        assertEquals(response.status, Status.OK)
    }

    @Test
    fun `make response body test`() {
        val response = makeResponse(Status.OK, "OK")
        assertEquals(response.bodyString(), "OK")
    }
}
