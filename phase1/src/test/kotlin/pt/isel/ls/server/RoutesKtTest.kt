package pt.isel.ls.server

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.routing.RouterMatch
import org.http4k.routing.RoutingHttpHandler
import pt.isel.ls.webApi.PlayerHandlerStunt
import kotlin.test.Test
import kotlin.test.assertIs

class RoutesKtTest {
    private fun actionOfRoutesArrangement(body: (RoutingHttpHandler) -> Unit) =
        // arrangement
        buildRoutes(PlayerHandlerStunt)
            .let(body)

    @Test
    fun `buildRoutes returns router with get PLAYER_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.GET, PLAYER_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }

    @Test
    fun `buildRoutes returns router with create PLAYER_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.POST, PLAYER_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }
}
