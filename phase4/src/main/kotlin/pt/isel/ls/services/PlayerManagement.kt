package pt.isel.ls.services

import org.eclipse.jetty.util.security.Password
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.associateWith
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.domain.info.CreatePlayerEmailPasswordParam
import pt.isel.ls.domain.info.CreatePlayerNameParam
import pt.isel.ls.domain.info.PlayerAuthentication
import pt.isel.ls.storage.PlayerStorageInterface

/**
 * Represents the services made by the application.
 *
 * This class provides methods for creating and reading [Player] objects.
 *
 * @property mem The player data memory.
 * @throws ServicesError containing the message of the error.
 */
class PlayerManagement(private val mem: PlayerStorageInterface) : PlayerServices {
    override fun createPlayer(
        nameUsername: CreatePlayerNameParam,
        emailPassword: CreatePlayerEmailPasswordParam,
    ): PlayerAuthentication =
        tryCatch("Unable to create a new Player due") {
            val (name, username) = nameUsername
            requireValidParam(name.isNotBlank()) { "Name must not be blank." }
            val condition = !username.isNullOrBlank() || username == null
            requireValidParam(condition) { "Username cannot be empty." }
            val player = nameUsername associateWith emailPassword
            val pid = mem.create(player)
            PlayerAuthentication(pid, player.token)
        }

    override fun getPlayerDetails(pid: UInt): Player =
        tryCatch("Unable to get the details of a Player due") {
            mem.read(pid)
        }

    override fun isValidToken(token: String): Boolean =
        tryCatch("Unable to check if the token is valid due") {
            mem.readBy(token = token) != null
        }

    override fun getPlayerDetailsBy(username: String): Player {
        return tryCatch("Unable to get the details of a Player due") {
            requireValidParam(username.isNotBlank()) { "username cannot be empty" }
            mem.readBy(username = username)?.firstOrNull() ?: throw ServicesError("Player not found.")
        }
    }

    override fun login(
        username: String,
        password: String,
    ): PlayerAuthentication =
        tryCatch("Unable to login due") {
            mem.readBy(username = username)?.firstOrNull()?.let {
                if (Password(password) == it.password) {
                    checkNotNullService(it.pid) { "Player id is null." }
                    mem.update(it)
                    PlayerAuthentication(it.pid, it.token)
                } else {
                    throw ServicesError("Invalid password.")
                }
            } ?: throw ServicesError("Player not found.")
        }

    override fun logout(pid: UInt) =
        tryCatch("Unable to logout due") {
            mem.deleteToken(pid)
        }
}
