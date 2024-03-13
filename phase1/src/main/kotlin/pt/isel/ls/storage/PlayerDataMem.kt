package pt.isel.ls.storage

import pt.isel.ls.domain.Player

/**
 * Represents the in-memory storage of player data.
 *
 * This class implements the [PlayerDataInterface] and uses a [Storage] of [Player] objects to perform the operations.
 *
 * @property mem The [Storage] of [Player] objects that this class uses to perform the operations.
 */
class PlayerDataMem(private val mem: Storage<Player>) : PlayerDataInterface {
    override fun storePlayer(newPlayer: Player): UInt = mem.create(newPlayer)

    override fun readPlayer(pid: UInt): Player = mem.read(pid)?.first() ?: throw NoSuchElementException("Player not found")
}
