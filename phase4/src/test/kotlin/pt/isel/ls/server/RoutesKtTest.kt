package pt.isel.ls.server

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.routing.RouterMatch
import org.http4k.routing.RoutingHttpHandler
import pt.isel.ls.webApi.GameHandlerStunt
import pt.isel.ls.webApi.PlayerHandlerStunt
import pt.isel.ls.webApi.SessionHandlerStunt
import kotlin.test.Test
import kotlin.test.assertIs

class RoutesKtTest {
    private fun actionOfRoutesArrangement(body: (RoutingHttpHandler) -> Unit) =
        // arrangement
        buildRoutes(PlayerHandlerStunt, GameHandlerStunt, SessionHandlerStunt)
            .let(body)

    @Test
    fun `buildRoutes returns router with get PLAYER_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.GET, PLAYER_ID_ROUTE)
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

    @Test
    fun `buildRoutes returns router with create SESSION_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.POST, SESSION_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }

    @Test
    fun `buildRoutes returns router with get SESSION_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.GET, SESSION_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }

    @Test
    fun `buildRoutes returns router with post SESSION_ID_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.PUT, SESSION_PLAYER_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }

    @Test
    fun `buildRoutes returns router with get SESSION_ID_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.GET, SESSION_ID_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }

    @Test
    fun `buildRoutes returns router with get GAME_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.GET, GAME_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }

    @Test
    fun `buildRoutes returns router with create GAME_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.POST, GAME_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }

    @Test
    fun `buildRoutes returns router with get GAME_ID_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.GET, GAME_ID_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }

    @Test
    fun `buildRoutes returns router with get SESSION_PLAYER_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.DELETE, SESSION_PLAYER_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }

    @Test
    fun `buildRoutes returns router with delete SESSION_DELETE_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.DELETE, SESSION_ID_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }

    @Test
    fun `buildRoutes returns router with post PLAYER_LOGIN_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.POST, PLAYER_LOGIN_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }

    @Test
    fun `buildRoutes returns router with post PLAYER_LOGOUT_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.DELETE, PLAYER_ID_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }

    @Test
    fun `buildRoutes returns router with get GAME_GENRES_ROUTE`() =
        actionOfRoutesArrangement { handler: RoutingHttpHandler ->
            val request = Request(Method.GET, GAME_GENRES_ROUTE)
            assertIs<RouterMatch.MatchingHandler>(
                handler.match(request),
                "No matching handler found for $request",
            )
        }
}
