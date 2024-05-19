package pt.isel.ls.webApi

import org.http4k.core.Request
import org.http4k.core.Response

object PlayerHandlerStunt : PlayerHandlerInterface {
    override fun createPlayer(request: Request): Response {
        TODO("not needed for the test")
    }

    override fun getPlayer(request: Request): Response {
        TODO("not needed for the test")
    }

    override fun getPlayerBy(request: Request): Response {
        TODO("Not yet implemented")
    }

    override fun login(request: Request): Response {
        TODO("Not yet implemented")
    }

    override fun logout(request: Request): Response {
        TODO("Not yet implemented")
    }
}
