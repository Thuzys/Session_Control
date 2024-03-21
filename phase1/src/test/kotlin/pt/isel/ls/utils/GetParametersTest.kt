package pt.isel.ls.utils

import org.http4k.core.Method
import org.http4k.core.Request
import pt.isel.ls.webApi.getParameter
import kotlin.test.Test
import kotlin.test.assertEquals

class GetParametersTest {
    @Test
    fun `get parameters test`() {
        val request = Request(Method.GET, "/echo?text=Hello")
        val parameters = getParameter(request, "text")
        assertEquals("Hello", parameters)
    }

    @Test
    fun `get non existing parameters test`() {
        val request = Request(Method.GET, "/echo")
        val parameters = getParameter(request, "text")
        assertEquals(null, parameters)
    }
}
