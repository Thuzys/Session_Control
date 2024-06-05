package pt.isel.ls

import org.http4k.server.Jetty
import org.http4k.server.asServer

private const val ENV_NAME = "JDBC_DATABASE_URL"

fun main() {
    val routes = routingHttpHandler(ENV_NAME)
    val port = System.getenv("PORT")?.toInt()
    checkNotNull(port) { "PORT environment variable not set" }
    val server = routes.asServer(Jetty(port)).start()
    println("Server started at ${server.port()}")

    println("Press Enter to stop the server:")
    readln()
    server.stop()

    println("Server stopped.")
}
