package pt.isel.ls.server

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.routing.RouterMatch
import org.http4k.routing.RoutingHttpHandler
import pt.isel.ls.webApi.PlayerHandlerStunt
import kotlin.test.Test
import kotlin.test.assertIs

class RoutesKtTest {
    private fun buildRoutesTest(
        request: Request,
        body: RoutingHttpHandler.(Request) -> Unit,
    ) = buildRoutes(PlayerHandlerStunt).run { this.body(request) }

    @Test
    fun `buildRoutes returns router with get PLAYER_ROUTE`() =
        buildRoutesTest(Request(Method.GET, PLAYER_ROUTE)) { request: Request ->
            assertIs<RouterMatch.MatchingHandler>(match(request), "No matching handler found for $request")
        }

    @Test
    fun `buildRoutes returns router with create PLAYER_ROUTE`() =
        buildRoutesTest(Request(Method.POST, PLAYER_ROUTE)) { request: Request ->
            assertIs<RouterMatch.MatchingHandler>(match(request), "No matching handler found for $request")
        }
}
