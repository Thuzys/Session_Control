package pt.isel.ls.storage

import pt.isel.ls.domain.Domain
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session

class LocalStorage : SessionDataMem {
    private var uidPlayer: UInt = 0u
    private val hashPlayer = HashMap<UInt, Player>()
    private val hashGame = HashMap<UInt, Game>()
    private val hashSession = HashMap<UInt, Session>()

    override fun create(newItem: Domain): UInt {
        TODO("Not yet implemented")
    }

    override fun read(uInt: UInt) {
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
