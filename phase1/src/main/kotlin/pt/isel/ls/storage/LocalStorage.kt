package pt.isel.ls.storage

import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session

/**
 * Represents the local storage of the application.
 *
 * This class implements the [Storage] interface and provides concrete implementations for its methods.
 * It stores [Player], [Game], and [Session] objects, each identified by a unique identifier.
 *
 */
class LocalStorage<T> : Storage<T> {
    private var uidPlayer: UInt = 0u
    private var uidGame: UInt = 0u
    private var uidSession: UInt = 0u
    private val hashPlayer = HashMap<UInt, Player>()
    private val hashGame = HashMap<UInt, Game>()
    private val hashSession = HashMap<UInt, Session>()

    override fun create(newItem: T): UInt {
        TODO("Not yet implemented")
    }

    override fun read(
        uInt: UInt?,
        offset: UInt,
        limit: UInt,
    ): Collection<T>? {
        TODO("Not yet implemented")
    }

    override fun update(
        uInt: UInt,
        newItem: T,
    ) {
        TODO("Not yet implemented")
    }

    override fun delete(uInt: UInt) {
        TODO("Not yet implemented")
    }
}
