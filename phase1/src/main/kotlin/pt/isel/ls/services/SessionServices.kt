package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Domain
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.addPlayer
import pt.isel.ls.domain.associatedTo
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.storage.DataMem
import pt.isel.ls.utils.tryCatch

/**
 * Represents the services made by the application.
 *
 * This class provides methods for creating, reading and updating [Player], [Session], and [Game] objects.
 * It also provides methods for adding a player to a session and searching for a game by the developer and genres.
 *
 * @property dataMem The session data memory.
 * @throws ServicesError containing the message of the error.
 */
class SessionServices(private val dataMem: DataMem) : Services {
    override fun createPlayer(
        name: String,
        email: String,
    ): UInt =
        tryCatch("Unable to create a new player") {
            dataMem.create(name associatedTo Email(email))
        }

    override fun getPlayerDetails(uuid: UInt): Player =
        tryCatch("Unable to get the details of a Player due") {
            dataMem.read(uInt = uuid, type = Player.hash) as Player
        }

    override fun addPlayer(
        player: UInt,
        session: UInt,
    ) = tryCatch("Unable to add player to session") {
        val playerToAdd = dataMem.read(uInt = player, type = Player.hash) as Player
        val whereSession = dataMem.read(uInt = session, type = Session.hash) as Session
        val updatedSession = whereSession.addPlayer(playerToAdd)
        dataMem.update(uInt = session, newItem = updatedSession)
    }

    override fun getSessionDetails(uuid: UInt): Session =
        tryCatch("Unable to get the details of a Session due") {
            dataMem.read(uInt = uuid, type = Session.hash) as Session
        }

    override fun createSession(
        gid: UInt,
        date: LocalDateTime,
        capacity: UInt,
    ): UInt =
        tryCatch("Unable to create a new session due to") {
            dataMem.create(Session(gid = gid, date = date, capacity = capacity))
        }

    override fun createGame(
        name: String,
        dev: String,
        genres: Collection<String>,
    ): UInt =
        tryCatch("Unable to create a new game due to") {
            dataMem.create(name associatedTo Pair(dev, genres))
        }

    override fun getGameDetails(uuid: UInt): Game =
        tryCatch("Unable to find the game due to") {
            dataMem.read(uInt = uuid, type = Game.hash) as Game
        }

    override fun <T : Domain> searchBy(
        type: Int,
        offset: UInt,
        limit: UInt,
        filter: (T) -> Boolean,
    ): Collection<T> =
        dataMem.readBy(type = type, offset = offset, limit = limit) {
            it.filter(filter)
        }
}
