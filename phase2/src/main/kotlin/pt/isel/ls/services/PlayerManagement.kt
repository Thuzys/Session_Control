package pt.isel.ls.services

import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.associatedTo
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.storage.PlayerStorageInterface
import java.util.UUID

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
        name: String,
        email: String,
    ): Pair<UInt, UUID> =
        tryCatch("Unable to create a new Player due") {
            (name associatedTo Email(email)).let { mem.create(it) to it.token }
        }

    override fun getPlayerDetails(pid: UInt): Player =
        tryCatch("Unable to get the details of a Player due") {
            mem.read(pid)
        }

    override fun isValidToken(token: String): Boolean =
        tryCatch("Unable to check if the token is valid due") {
            mem.readBy(token = token) != null
        }
}
