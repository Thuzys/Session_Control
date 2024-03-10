package pt.isel.ls.services

import pt.isel.ls.domain.Domain
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.addPlayer
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
            error("Unable to create a new player due: ${error.message}")
        }

    /**
     * returns the details of player.
     * @param uuid the identifier of each player.
     * @return a [Player] containing all the information wanted or null if nothing is found.
     */
    fun getPlayerDetails(uuid: UInt): Domain? = dataMem.read(uuid, Player.hash)

    /**
     * Adds a player to a session.
     *
     * Retrieves the player information based on the given player identifier (uuid) and adds it to the session
     * identified by the given session identifier.
     *
     * @param player The identifier of the player to add.
     * @param session The identifier of the session to which the player will be added.
     * @throws IllegalArgumentException if the player or session is not found, or if there's an issue adding the player to the session.
     */
    fun addPlayer(
        player: UInt,
        session: UInt,
    ) {
        try {
            val playerToAdd = dataMem.read(player, Player.hash) as? Player ?: throw IllegalArgumentException()
            val whereSession = dataMem.read(session, Session.hash) as? Session ?: throw IllegalArgumentException()
            val updatedSession = whereSession.addPlayer(playerToAdd)
            dataMem.update(session, updatedSession)
        } catch (error: IllegalArgumentException) {
            error("Unable to add player to session: ${error.message}")
        }
    }
}
