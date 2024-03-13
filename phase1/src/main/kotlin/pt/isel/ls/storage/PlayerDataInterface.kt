package pt.isel.ls.storage

import pt.isel.ls.domain.Player

/**
 * The interface that defines the operations to be performed on the Player's data.
 *
 * @property storePlayer the operation to store a new player.
 * @property readPlayer the operation to read the details of a player.
 */
interface PlayerDataInterface {
    /**
     * Stores a new player.
     *
     * @param newPlayer the [Player] to be stored.
     * @return a [UInt] as a unique key to be associated with the new [Player].
     */
    fun storePlayer(newPlayer: Player): UInt

    /**
     * Reads the details of a player.
     *
     * @param pid the identifier of each player.
     * @return a [Player] containing all the information wanted.
     */
    fun readPlayer(pid: UInt): Player
}
