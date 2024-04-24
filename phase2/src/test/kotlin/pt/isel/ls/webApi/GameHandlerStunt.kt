package pt.isel.ls.webApi

import org.http4k.core.Request
import org.http4k.core.Response

object GameHandlerStunt : GameHandlerInterface {
    override fun createGame(request: Request): Response {
        TODO()
    }

    override fun getGameDetails(request: Request): Response {
        TODO()
    }

    override fun getGameByDevAndGenres(request: Request): Response {
        TODO()
    }

    override fun getGamesByPlayer(request: Request): Response {
        TODO("Not yet implemented")
    }
}
