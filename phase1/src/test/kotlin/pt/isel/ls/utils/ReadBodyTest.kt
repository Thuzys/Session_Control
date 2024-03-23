package pt.isel.ls.utils

import org.http4k.core.Method
import org.http4k.core.Request
import pt.isel.ls.webApi.readBody
import kotlin.test.Test
import kotlin.test.assertEquals

class ReadBodyTest {
    @Test
    fun `read body test`() {
        val request = Request(Method.POST, "/echo").body("{\"msg\": \"Hello_World!\"}")
        val body = readBody(request)
        assertEquals("Hello_World!", body["msg"])
    }

    @Test
    fun `read non existing body test`() {
        val request = Request(Method.POST, "/echo")
        val body = readBody(request)
        assertEquals(null, body["msg"])
    }
}
