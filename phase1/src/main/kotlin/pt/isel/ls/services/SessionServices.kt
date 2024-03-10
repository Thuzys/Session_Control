package pt.isel.ls.services

import pt.isel.ls.domain.Domain
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.associatedTo
import pt.isel.ls.storage.Storage

/**
 * Represents the services made by the application.
 * @param dataMem the memory container to storage all the data.
 * @throws IllegalStateException containing the message of the error.
 */
class SessionServices(private val dataMem: Storage) {
    /**
     * Create a new player and storage the same.
     * @param name the name of the player.
     * @param email the email (is unique to each player) to be associated to the player.
     * @return [UInt] a unique key to be associated to the new [Player].
     * @throws IllegalStateException containing the message of the error.
     */
    fun createPlayer(
        name: String,
        email: String,
    ): UInt =
        try {
            dataMem.create(name associatedTo Email(email))
        } catch (error: IllegalArgumentException) {
            error("unable to create a new player due: ${error.message}")
        }

    /**
     * returns the details of player.
     * @param uuid the identifier of each player.
     * @return a [Player] containing all the information wanted or null if nothing is found.
     */
    fun getPlayerDetails(uuid: UInt): Domain? = dataMem.read(uuid, Player.hash)
}
