package pt.isel.ls.utils

import org.http4k.core.Method
import org.http4k.core.Request
import pt.isel.ls.webApi.toPidOrNull
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

private const val DUMMY_ROUTE = "/dummyRoute"

class ToPidOrNullTest {
    @Test
    fun `test with a valid pid in request`() {
        val request = Request(Method.GET, "$DUMMY_ROUTE?pid=1")
        assertNotNull(request.toPidOrNull())
    }

    @Test
    fun `test with an invalid pid in request`() {
        val request = Request(Method.GET, DUMMY_ROUTE)
        assertNull(request.toPidOrNull())
    }
}
