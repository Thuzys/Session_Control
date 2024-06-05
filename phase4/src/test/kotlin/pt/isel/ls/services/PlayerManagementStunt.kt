package pt.isel.ls.services

import org.eclipse.jetty.util.security.Password
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.domain.info.PlayerAuthentication
import pt.isel.ls.domain.info.email_password
import pt.isel.ls.domain.info.name_username
import java.util.UUID

object PlayerManagementStunt : PlayerServices {
    val playerId = 1u
    val playerToken: UUID = UUID.randomUUID()
    val playerEmail = Email("test@email.com")
    private const val PLAYER_NAME = "Test"
    val password = Password("password")

    private val player =
        Player(
            playerId,
            PLAYER_NAME,
            email = playerEmail,
            token = playerToken,
            username = PLAYER_NAME,
            password = password,
        )

    override fun createPlayer(
        nameUsername: name_username,
        emailPassword: email_password,
    ): PlayerAuthentication =
        if (nameUsername.first.isNotBlank() && emailPassword.first.isNotBlank() && emailPassword.second.isNotBlank()) {
            PlayerAuthentication(playerId, playerToken)
        } else {
            throw ServicesError("Unable to create a new Player due to invalid name or email.")
        }

    override fun getPlayerDetails(pid: UInt): Player =
        if (pid == playerId) {
            player
        } else {
            throw ServicesError("Unable to get the details of a Player due to nonexistent pid.")
        }

    override fun isValidToken(token: String): Boolean {
        return token == playerToken.toString()
    }

    override fun getPlayerDetailsBy(username: String): Player {
        if (username == PLAYER_NAME) {
            return player
        } else {
            throw ServicesError("Unable to get the details of a Player due to nonexistent userName.")
        }
    }

    override fun login(
        username: String,
        password: String,
    ): PlayerAuthentication {
        return PlayerAuthentication(playerId, playerToken)
    }

    override fun logout(pid: UInt) {
        if (pid != playerId) {
            throw ServicesError("Unable to logout due invalid pid.")
        }
    }
}
