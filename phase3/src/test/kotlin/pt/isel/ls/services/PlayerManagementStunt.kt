package pt.isel.ls.services

import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.errors.ServicesError
import java.util.UUID

object PlayerManagementStunt : PlayerServices {
    val playerId = 1u
    val playerToken: UUID = UUID.randomUUID()
    val playerEmail = Email("test@email.com")
    val playerName = "Test"

    override fun createPlayer(
        name: String,
        email: String,
        userName: String?,
    ): Pair<UInt, UUID> =
        if (name.isNotBlank() && email.isNotBlank()) {
            Pair(playerId, playerToken)
        } else {
            throw ServicesError("Unable to create a new Player due to invalid name or email.")
        }

    override fun getPlayerDetails(pid: UInt): Player =
        if (pid == playerId) {
            Player(pid, playerName, email = playerEmail, token = playerToken)
        } else {
            throw ServicesError("Unable to get the details of a Player due to nonexistent pid.")
        }

    override fun isValidToken(token: String): Boolean {
        return token == playerToken.toString()
    }
}
