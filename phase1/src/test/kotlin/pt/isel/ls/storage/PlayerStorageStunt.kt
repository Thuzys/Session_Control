package pt.isel.ls.storage

import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player

class PlayerStorageStunt : Storage<Player> {
    private val defaultMail = Email("default@mail.com")
    private val player1 = Player(1u, "test1", defaultMail)
    private val player2 = Player(2u, "test2", defaultMail)
    private var uid: UInt = 3u
    private val players =
        hashMapOf(
            1u to player1,
            2u to player2,
        )
    override fun create(newItem: Player): UInt =
        uid++.also {
            players[it] = newItem.copy(pid = it)
        }

    override fun read(
        uInt: UInt?,
        offset: UInt,
        limit: UInt,
    ): Collection<Player>? =
        if (uInt == null) {
            players.values.drop(offset.toInt()).take(limit.toInt())
        } else {
            players[uInt]?.let { listOf(it) }
        }

    override fun delete(uInt: UInt) {
        TODO("Not needed for this test")
    }

    override fun update(
        uInt: UInt,
        newItem: Player,
    ) {
        TODO("Not needed for this test")
    }
}
