package pt.isel.ls.utils

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.UriTemplate
import org.http4k.routing.RoutedRequest
import pt.isel.ls.webApi.toSidOrNull
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

private const val DUMMY_ROUTE = "/dummyRoute"

class ToSidOrNullTest {
    @Test
    fun `test with a valid sid in request`() {
        val request =
            RoutedRequest(
                Request(Method.GET, "$DUMMY_ROUTE/1"),
                UriTemplate.from("$DUMMY_ROUTE/{sid}"),
            )
        assertNotNull(request.toSidOrNull())
    }

    @Test
    fun `test with an invalid sid in request`() {
        val request =
            RoutedRequest(
                Request(Method.GET, "$DUMMY_ROUTE/dummy"),
                UriTemplate.from("$DUMMY_ROUTE/{sid}"),
            )
        assertNull(request.toSidOrNull())
    }
}
