package pt.isel.ls.service

import pt.isel.ls.domain.Domain
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.storage.SessionDataMem

class SessionServices(private val dataMem: SessionDataMem) {
    fun createPlayer(
        name: String,
        email: String,
    ): UInt {
        val newPlayer = Player(name = name, email = Email(email))
        return dataMem.create(newPlayer)
    }

    fun getPlayerDetails(uuid: UInt): Domain {
        return dataMem.read(uuid, Player.hash)
    }
}
