package pt.isel.ls.storage

import pt.isel.ls.domain.Domain
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session

/**
 * Represents the local storage of the application.
 *
 * This class implements the [Storage] interface and provides concrete implementations for its methods.
 * It uses hash maps to store [Player], [Game], and [Session] objects, each identified by a unique identifier.
 *
 * @property uidPlayer The unique identifier of the player.
 * @property uidGame The unique identifier of the game.
 * @property uidSession The unique identifier of the session.
 * @property hashPlayer The hash map storing [Player] objects, identified by their unique identifiers.
 * @property hashGame The hash map storing [Game] objects, identified by their unique identifiers.
 * @property hashSession The hash map storing [Session] objects, identified by their unique identifiers.
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
        offset: Int,
        limit: Int,
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
