package pt.isel.ls.storage

import pt.isel.ls.domain.Domain
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session

/**
 * Represents the local storage of the application.
 *
 * @property uidPlayer the unique identifier of the player.
 * @property uidGame the unique identifier of the game.
 * @property uidSession the unique identifier of the session.
 * @property hashPlayer the hash of the player.
 * @property hashGame the hash of the game.
 * @property hashSession the hash of the session.
 */
class LocalStorage : Storage {
    private var uidPlayer: UInt = 0u
    private var uidGame: UInt = 0u
    private var uidSession: UInt = 0u
    private val hashPlayer = HashMap<UInt, Player>()
    private val hashGame = HashMap<UInt, Game>()
    private val hashSession = HashMap<UInt, Session>()

    override fun create(newItem: Domain): UInt {
        TODO("Not yet implemented")
    }

    override fun read(
        uInt: UInt?,
        type: Int,
    ): Collection<Domain>? {
        TODO("Not yet implemented")
    }

    override fun update(
        uInt: UInt,
        newItem: Domain,
    ) {
        TODO("Not yet implemented")
    }

    override fun delete(uInt: UInt) {
        TODO("Not yet implemented")
    }
}
