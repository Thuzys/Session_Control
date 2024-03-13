package pt.isel.ls.storage

import pt.isel.ls.domain.Player

interface PlayerDataInterface {
    fun storePlayer(newPlayer: Player): UInt

    fun readPlayer(pid: UInt): Player
}
