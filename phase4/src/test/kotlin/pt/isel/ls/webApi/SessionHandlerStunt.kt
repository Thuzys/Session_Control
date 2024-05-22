package pt.isel.ls.webApi

import org.http4k.core.Request
import org.http4k.core.Response

object SessionHandlerStunt : SessionHandlerInterface {
    override fun createSession(request: Request): Response {
        TODO("Not yet implemented")
    }

    override fun getSession(request: Request): Response {
        TODO("Not yet implemented")
    }

    override fun getSessions(request: Request): Response {
        TODO("Not yet implemented")
    }

    override fun addPlayerToSession(request: Request): Response {
        TODO("Not yet implemented")
    }

    override fun updateCapacityOrDate(request: Request): Response {
        TODO("Not yet implemented")
    }

    override fun removePlayerFromSession(request: Request): Response {
        TODO("Not yet implemented")
    }

    override fun deleteSession(request: Request): Response {
        TODO("Not yet implemented")
    }

    override fun getPlayerFromSession(request: Request): Response {
        TODO("Not yet implemented")
    }
}
