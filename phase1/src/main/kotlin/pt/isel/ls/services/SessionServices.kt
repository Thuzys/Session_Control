package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Domain
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.storage.SessionDataMem
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.addPlayer
import pt.isel.ls.domain.associatedTo

/**
 *
 * Represents the services made by the application.
 *
 * @param dataMem the memory container to storage all the data.
 * @throws IllegalStateException containing the message of the error.
 */
class SessionServices(private val dataMem: SessionDataMem) {
    /**
     *
     * Create a new player and storage the same.
     *
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
     *
     * Returns the details of player.
     *
     * @param uuid the identifier of each player.
     * @return a [Player] containing all the information wanted or null if nothing is found.
     */
    fun getPlayerDetails(uuid: UInt): Domain = dataMem.read(uuid, Player.hash)

    /**
     *
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

    /**
     *
     * Returns the details of a Session.
     *
     * @param uuid the identifier of the Session.
     * @return a [Session] containing all the information wanted.
     */

    fun getSessionDetails(uuid: UInt): Domain {
        try {
            return dataMem.read(uuid, Session.hash)
        } catch (error: IllegalArgumentException) {
            error("error: ${error.message}")
        }
    }

    /**
     * Retrieves sessions based on the specified parameters.
     *
     * @param gid The identifier of the game for which sessions are being retrieved.
     * @param date The date and time of the sessions to retrieve (optional).
     * @param state The state of the sessions to retrieve (optional).
     * @param playerId The identifier of the player to filter sessions by (optional).
     * @return A collection of sessions that match the specified parameters.
     */

    fun getSessions(
        gid: UInt,
        date: LocalDateTime? = null,
        state: SessionState? = null,
        playerId: UInt? = null,
    ): Collection<Session> =
        dataMem.readBy(Session.hash) {
            it.filter { session ->
                session.gid == gid &&
                    (date == null || session.date == date) &&
                    (state == null || getSessionState(session) == state) &&
                    (playerId == null || session.players.any { player -> player.uuid == playerId })
            }
        }

    /**
     * Determines the state of a session.
     *
     * @param session The session for which the state is being determined.
     * @return The state of the session (OPEN or CLOSE).
     */

    private fun getSessionState(session: Session): SessionState {
        return if (session.players.size.toUInt() < session.capacity) SessionState.OPEN else SessionState.CLOSE
    }
    
    /*
     * Create a new game and storage the same.
     * @param name the name of the game.
     * @param dev the developer of the game.
     * @param genres the genres of the game.
     * @return [UInt] a unique key to be associated to the new [Game].
     * @throws IllegalStateException containing the message of the error.
     */
    fun createGame(
        name: String,
        dev: String,
        genres: Collection<String>,
    ): UInt =
        try {
            dataMem.create(name associatedTo Pair(dev, genres))
        } catch (error: IllegalArgumentException) {
            error("unable to create a new game due to: ${error.message}")
        }

    /**
     * returns the details of player.
     * @param uuid the identifier of each player.
     * @return a [Player] containing all the information wanted or null if nothing is found.
     */
    fun getGameDetails(uuid: UInt): Domain =
        try {
            dataMem.read(uuid, Game.hash)
        } catch (error: IllegalArgumentException) {
            error("unable to find the game due to: ${error.message}")
        }

    /**
     * Search for a game by the developer and genres.
     * @param dev the developer of the game.
     * @param genres the genres of the game.
     * @return a collection of [Game] containing all the information wanted or null if nothing is found.
     */
    fun searchGameByDevAndGenres(
        dev: String,
        genres: Collection<String>,
    ): Collection<Game> =
        dataMem.readBy(Game.hash) {
            it.filter { game ->
                game.dev == dev && game.genres.containsAll(genres)
            }
        }
}
