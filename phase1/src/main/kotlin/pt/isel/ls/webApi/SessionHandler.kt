package pt.isel.ls.webApi

import org.http4k.core.Request
import org.http4k.core.Response
import pt.isel.ls.services.SessionManagement

class SessionHandler(private val sessionManagement: SessionManagement) : SessionHandlerInterface {
    fun createSession(request: Request): Response {
        TODO()
    }
}
